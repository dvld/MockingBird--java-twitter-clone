package com.tts.mockingbird.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;

import com.tts.mockingbird.model.User;
import com.tts.mockingbird.service.UserService;

@Controller
public class AuthorizationController {
	
	@Autowired
	private UserService userService;
	
//	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	

//	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	@GetMapping("/signup")
	public String registration(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "registration";
	}
	
//	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@PostMapping("/signup")
	public String createNewUser(@Valid User user, BindingResult result, Model model) {
		User userExists = userService.findByUsername(user.getUsername());
		if (userExists != null) {
			result.rejectValue("username", "error.user", "Username is already taken");
		}
		if (!result.hasErrors()) {
			userService.saveNewUser(user);
			model.addAttribute("success", "Sign up successful!");
			model.addAttribute("user", new User());
		}
		return "login";
	}
}
