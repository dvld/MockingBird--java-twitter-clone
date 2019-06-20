package com.tts.mockingbird.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.tts.mockingbird.model.Mock;
import com.tts.mockingbird.model.MockDisplay;
import com.tts.mockingbird.model.User;
import com.tts.mockingbird.service.MockService;
import com.tts.mockingbird.service.UserService;

@Controller
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private MockService mockService;

  @GetMapping("/profile")
  public String getProfile() {
    return "profile";
  }

  // @RequestMapping(value="/users/{username}", method = RequestMethod.GET)
  @GetMapping("/users/{username}")
	public String getUser(@PathVariable("username") String username, Model model) {

    User user = userService.findByUsername(username);
    // part 3 slide 30 // changed 'List<Mock>' to 'List<MockDisplay>' //
		List<MockDisplay> mocks = mockService.findAllByUser(user);

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
  public String getUsers(@RequestParam(value="filter", required=false) String filter, Model model) {
    List<User> users = new ArrayList<User>();
    // List<User> users = userService.findAll();
    model.addAttribute("users", users);
    SetMockCounts(users, model);
    
    User loggedInUser = userService.getLoggedInUser();
    List<User> usersFollowing = loggedInUser.getFollowing();
    List<User> usersFollowers = loggedInUser.getFollowers();
    SetFollowingStatus(users, usersFollowing, model);

    // part 3 slide 44 //
    if(filter == null) {
      filter = "all";
    }

    //part 3 slide 45 //
    if (filter.equalsIgnoreCase("followers")) {
      users = usersFollowers;
      model.addAttribute("filter", "followers");
    }
    // part 3 slide 46 //
    else if (filter.equalsIgnoreCase("following")) {
      users = usersFollowing;
      model.addAttribute("filter", "following");
    }
    // part 3 slide 47 //
    else {
      users = userService.findAll();
      model.addAttribute("filter", "all");
    }

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
      // part 3 slide 30 // changed 'List<Mock>' to 'List<MockDisplay>' //
      List<MockDisplay> mocks = mockService.findAllByUser(user);
      mockCounts.put(user.getUsername(), mocks.size());
    }

    model.addAttribute("mockCounts", mockCounts);
  }
}
