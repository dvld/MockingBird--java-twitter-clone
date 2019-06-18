package com.tts.mockingbird.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tts.mockingbird.model.Mock;
import com.tts.mockingbird.model.User;
import com.tts.mockingbird.service.MockService;
import com.tts.mockingbird.service.UserService;

@Controller
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private MockService mockService;

  // @RequestMapping(value="/users/{username}", method = RequestMethod.GET)
  @GetMapping("/users/{username}")
	public String getUser(@PathVariable("username") String username, Model model) {

		User user = userService.findByUsername(username);
		List<Mock> mocks = mockService.findAllByUser(user);

    User loggedInUser = userService.getLoggedInUser();
    List<User> following = loggedInUser.getFollowing();
    boolean isFollowing = false;

    for (User followedUser : following) {
      if (followedUser.getUsername().equals(username)) {
        isFollowing = true;
      }
    }

    boolean isSelfPage = loggedInUser.getUsername().equals(username);

    model.addAttribute("isSelfPage", isSelfPage);

    model.addAttribute("following", isFollowing);

    model.addAttribute("mockList", mocks);
    model.addAttribute("user", user);

		return "user";
  }

  @GetMapping("/users")
  public String getUsers(Model model) {
    List<User> users = userService.findAll();
    model.addAttribute("users", users);
    SetMockCounts(users, model);
    
    User loggedInUser = userService.getLoggedInUser();
    List<User> usersFollowing = loggedInUser.getFollowing();
    SetFollowingStatus(users, usersFollowing, model);

    return "users";
  }

  private void SetFollowingStatus(List<User> users, List<User> usersFollowing, Model model) {
    HashMap<String, Boolean> followingStatus = new HashMap<>();
    String username = userService.getLoggedInUser().getUsername();

    for (User user : users) {
      if (usersFollowing.contains(user)) {
        followingStatus.put(user.getUsername(), true);
      } else if (!user.getUsername().equals(username)) {
        followingStatus.put(user.getUsername(), false);
      }
    }

    model.addAttribute("followingStatus", followingStatus);
  }

  private void SetMockCounts(List<User> users, Model model) {
    HashMap<String, Integer> mockCounts = new HashMap<>();

    for (User user : users) {
      List<Mock> mocks = mockService.findAllByUser(user);
      mockCounts.put(user.getUsername(), mocks.size());
    }

    model.addAttribute("mockCounts", mockCounts);
  }
}
