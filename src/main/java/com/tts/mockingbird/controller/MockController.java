package com.tts.mockingbird.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;

import com.tts.mockingbird.model.Mock;
import com.tts.mockingbird.model.User;
import com.tts.mockingbird.service.MockService;
import com.tts.mockingbird.service.UserService;

@Controller
public class MockController {

	@Autowired
	private UserService userService;
	
	@Autowired 
	private MockService mockService;
	
//	@RequestMapping(value = {"/", "/mocks"}, method = RequestMethod.GET)
	@GetMapping("/feed")
	public String getFeed(Model model) {
		List<Mock> mocks = mockService.findAll();
		model.addAttribute("mockList", mocks);
		return "feed";
	}
	
//	@RequestMapping(value = "/mocks/new", method = RequestMethod.GET)
	@GetMapping("/mocks/new")
	public String getMockForm(Model model) {
		model.addAttribute("mock", new Mock());
		return "newMock";
	}
	
//	@RequestMapping(value = "/mocks", method = RequestMethod.POST)
	@PostMapping("/mocks")
	public String submitMockForm(@Valid Mock mock, BindingResult result, Model model) {
		User user = userService.getLoggedInUser();
		if (!result.hasErrors()) {
			mock.setUser(user);
			mockService.save(mock);
			model.addAttribute("successMessage", "Mock successfully created!");
			model.addAttribute("mock", new Mock());
		}
		return "newMock";
	}
	
}
