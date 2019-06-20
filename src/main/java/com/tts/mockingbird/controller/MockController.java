package com.tts.mockingbird.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tts.mockingbird.model.Mock;
import com.tts.mockingbird.model.MockDisplay;
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
  // part 3 slide 34 //
	public String getFeed(@RequestParam(value="filter", required=false) String filter, Model model) {
    User loggedInUser = userService.getLoggedInUser();
    List<MockDisplay> mocks = new ArrayList<>();

    // part 3 slide 35 //
    if (filter == null) {
      filter = "all";
    }

    // part 3 slide 36 //
    if (filter.equalsIgnoreCase("following")) {
      List<User> following = loggedInUser.getFollowing();
      mocks = mockService.findAllByUsers(following);
      model.addAttribute("filter", "following");
    }
    // part 3 slide 37 //
    else {
      mocks = mockService.findAll();
      model.addAttribute("filter", "all");
    }

    // part 3 slide 29 // change 'List<Mock>' to 'List<MockDisplay>' //
		// List<MockDisplay> mocks = mockService.findAll(); // commented out on part 3 slide 38 //
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
  
  // part 2 slide 63 //
  @GetMapping("/mocks/{hashtag}")
  public String getMocksByHashTag(@PathVariable("hashtag") String hashtag, Model model) {
    // part 3 slide 29 // change 'List<Mock>' to 'List<MockDisplay>' //
    List<MockDisplay> mocks = mockService.findAllWithTag(hashtag);
    model.addAttribute("mockList", mocks);
    model.addAttribute("hashtag", hashtag);
    return "hashtaggedMocks";
  }
}
