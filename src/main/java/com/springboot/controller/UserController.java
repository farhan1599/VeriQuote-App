package com.springboot.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.dto.QuoteResponseDTO;
import com.springboot.dto.ResetPwdDTO;
import com.springboot.dto.UserDTO;
import com.springboot.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	// ==============display login page (landing page)========================

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("user", new UserDTO());
		return "index";
	}

	// ===========submit login page=====================
	@PostMapping("/login")
	public String login(@ModelAttribute("user") UserDTO user, Model model) {
		UserDTO login = userService.login(user.getEmail(), user.getPwd());

		if (login == null) {
			model.addAttribute("errorMessage", "Invalid Credentials");
			model.addAttribute("user",new UserDTO());
			return "index";
		}

		// if credentials are valid then we'll check pwdUpdated or not
		if (login.getPwdUpdated().equals("YES")) {

			// display dashboard
			QuoteResponseDTO quotation = userService.getQuotation();

			model.addAttribute("quote", quotation);

			return "dashboard";

		} else {
			// display reset pwd page with email
			ResetPwdDTO resetPwd = new ResetPwdDTO();
			resetPwd.setEmail(login.getEmail());
			model.addAttribute("resetPwd", resetPwd);
			return "resetPwd";
		}
	}

	// ============ display registration page=====================

	@GetMapping("/registration")
	public String register(Model model) {
		UserDTO userDto = new UserDTO();
		model.addAttribute("user", userDto);

		// getting countriesMap
		// countries should come at the time of loading
		Map<Integer, String> countriesMap = userService.getCountries();
		model.addAttribute("countries", countriesMap);
		return "register";
	}

	// ================getting statesMap based on countryId===================
	/*
	 * without reloading the page we have to populate the data in state dropdown
	 * thats why we are returning map
	 */
	// this method will be called by AJAX
	@GetMapping("/states/{countryId}")
	@ResponseBody // because this method will return direct response, it'll not return UI page
	public Map<Integer, String> getStates(@PathVariable Integer countryId) {
		return userService.getStates(countryId);

	}

	// ================getting citiesMap based on stateId===================

	/*
	 * without reloading the page we have to populate the data in state dropdown
	 * thats why we are returning map
	 */
	// this method will be called by AJAX
	@GetMapping("/cities/{stateId}")
	@ResponseBody // because this method will return direct response, it'll not return UI page
	public Map<Integer, String> getCities(@PathVariable Integer stateId) {
		return userService.getCities(stateId);
	}

	// ============submit registration======================

	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") UserDTO user, Model model) {

		// checking unique email
		boolean emailUnique = userService.isEmailUnique(user.getEmail());

		// if email is unique then only register the user
		if (emailUnique) {
			boolean registeredUser = userService.registerUser(user);
			if (registeredUser) {
				model.addAttribute("successMessage", "Registration Successful, password is sent to your Email");
				
				model.addAttribute("user",new UserDTO());
			} else {
				model.addAttribute("errorMessage", "Registration Failed");
			}
		} else {
			model.addAttribute("errorMessage", "Email already exist");
			
		}

		// countries should come at the time of page re-loading
		Map<Integer, String> countriesMap = userService.getCountries();
		model.addAttribute("countries", countriesMap);
		return "register";

	}

	// =================reset password==================
	@PostMapping("/resetPwd")
	public String resetPwd(@ModelAttribute("resetPwd") ResetPwdDTO resetPwd, Model model) {

		UserDTO login = userService.login(resetPwd.getEmail(), resetPwd.getOldPwd());
		if (login == null) {
			model.addAttribute("errorMessage", "Old Password is Incorrect");
			return "resetPwd";
		}
		if (resetPwd.getNewPwd().equals(resetPwd.getConfirmPwd())) {
			userService.resetPwd(resetPwd);

			// returning dashboard
			QuoteResponseDTO quotation = userService.getQuotation();
			model.addAttribute("quote", quotation);
			return "dashboard";
		} else {
			model.addAttribute("errorMessage", "New Password and Confirm Password is not matching");
			return "resetPwd";
		}

	}

	// =============displaying random quotes======================

	@GetMapping("/getQuote")
	public String getRandomQuote(Model model) {
		QuoteResponseDTO quotation = userService.getQuotation();
		model.addAttribute("quote", quotation);
		return "dashboard";
	}
}
