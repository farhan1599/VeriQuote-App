package com.springboot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.springboot.dto.QuoteResponseDTO;
import com.springboot.dto.ResetPwdDTO;
import com.springboot.dto.UserDTO;
import com.springboot.entity.CityEntity;
import com.springboot.entity.CountryEntity;
import com.springboot.entity.StateEntity;
import com.springboot.entity.UserEntity;
import com.springboot.repository.CityRepository;
import com.springboot.repository.CountryRepository;
import com.springboot.repository.StateRepository;
import com.springboot.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private CountryRepository countryRepo;
	@Autowired
	private StateRepository stateRepo;
	@Autowired
	private CityRepository cityRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private EmailService emailService;

	// ==============validate user for login=============================
	@Override
	public UserDTO login(String email, String pwd) {
		UserEntity entity = userRepo.findByEmailAndPwd(email, pwd);
		if (entity != null) {
			UserDTO dto = new UserDTO();
			BeanUtils.copyProperties(entity, dto);
			return dto;
		}
		return null;
	}

	// ======================getting all countries=============================
	@Override
	public Map<Integer, String> getCountries() {
		List<CountryEntity> countriesList = countryRepo.findAll();
		// convert countriesList to Map
		Map<Integer, String> countriesMap = new HashMap<>();
		countriesList.forEach(country -> countriesMap.put(country.getCountryId(), country.getCountryName()));
		return countriesMap;
	}

	// =============getting all states based on country==========================
	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		List<StateEntity> statesList = stateRepo.findByCountryCountryId(countryId);
		// convert statesList to Map
		Map<Integer, String> statesMap = new HashMap<>();
		statesList.forEach(state -> statesMap.put(state.getStateId(), state.getStateName()));
		return statesMap;

	}

	// =============getting all cities based on states==========================
	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		List<CityEntity> citiesList = cityRepo.findByStateStateId(stateId);
		// convert citiesList to Map
		Map<Integer, String> citiesMap = new HashMap<>();
		citiesList.forEach(city -> citiesMap.put(city.getCityId(), city.getCityName()));
		return citiesMap;
	}

	// =============checking unique email============================
	@Override
	public boolean isEmailUnique(String email) {
		// returns null if email is unique
		return null == userRepo.findByEmail(email);

	}

	@Override
	public boolean registerUser(UserDTO userDto) {

		// calling randomPwd()
		String randomPwd = getRandomPwd();

		// setting randomPwd to pwd
		userDto.setPwd(randomPwd);

		// setting updatedPwd as 'NO'
		userDto.setPwdUpdated("NO");

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(userDto, userEntity);

		// getting the CountryEntity(country object) from UserDTO based on countryId
		CountryEntity countryEntity = countryRepo.findById(userDto.getCountryId()).get();

		// setting the CountryEntity to UserEntity
		userEntity.setCountry(countryEntity);

		// getting the StateEntity(state object) from UserDTO based on stateId
		StateEntity stateEntity = stateRepo.findById(userDto.getStateId()).get();

		// setting the StateEntity to UserEntity
		userEntity.setState(stateEntity);

		// getting the CityEntity(city object) from UserDTO based on cityId
		CityEntity cityEntity = cityRepo.findById(userDto.getCityId()).get();

		// setting the CityEntity to UserEntity
		userEntity.setCity(cityEntity);

		UserEntity savedUser = userRepo.save(userEntity);

		if (savedUser != null) {
			String subject = "Your Registration Successful";
			String body = "Hello, "+savedUser.getUserName()+"\n\nYour Account Login Password : " + randomPwd;
			return emailService.sendEmail(userDto.getEmail(), subject, body);

		}

		return false;
	}

	@Override
	public boolean resetPwd(ResetPwdDTO resetPwdDto) {
		// from the resetPwd page we are getting UserEntity with the emailId
		UserEntity entity = userRepo.findByEmail(resetPwdDto.getEmail());

		// setting the new password
		entity.setPwd(resetPwdDto.getNewPwd());

		// setting updatedPwd as 'YES'
		entity.setPwdUpdated("YES");

		UserEntity saved = userRepo.save(entity);

		return saved != null;
	}

	@Override
	public QuoteResponseDTO getQuotation() {
		// 3rd party API url
		final String API_URL = "https://dummyjson.com/quotes/random";

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<QuoteResponseDTO> forEntity = restTemplate.getForEntity(API_URL, QuoteResponseDTO.class);

		// returning ResponseBody
		return forEntity.getBody();

	}

	// =========generating random password========================
	private String getRandomPwd() {

		final String RANDOM_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		StringBuilder pwd = new StringBuilder();
		Random random = new Random();
		while (pwd.length() < 6) { // length of the random string.
			int index = (int) (random.nextFloat() * RANDOM_CHARS.length());
			pwd.append(RANDOM_CHARS.charAt(index));
		}
		return pwd.toString();

	}

}
