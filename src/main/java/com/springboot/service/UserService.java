package com.springboot.service;

import java.util.Map;

import com.springboot.dto.QuoteResponseDTO;
import com.springboot.dto.ResetPwdDTO;
import com.springboot.dto.UserDTO;

public interface UserService {

	public UserDTO login(String email, String pwd);

	public Map<Integer, String> getCountries();

	// we should get states based on country thats why we are taking countryId
	public Map<Integer, String> getStates(Integer countryId);

	// we should get cities based on state thats why we are taking stateId
	public Map<Integer, String> getCities(Integer stateId);

	// for unique email check
	public boolean isEmailUnique(String email);

	// for register the user
	public boolean registerUser(UserDTO userDto);
	
	
	//for reset password
	public boolean resetPwd(ResetPwdDTO resetPwdDto);
	
	//to display quotes in dash board
	public QuoteResponseDTO getQuotation() 	;

}
