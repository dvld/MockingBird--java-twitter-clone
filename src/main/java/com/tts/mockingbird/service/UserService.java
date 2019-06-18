package com.tts.mockingbird.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tts.mockingbird.model.Role;
import com.tts.mockingbird.model.User;
import com.tts.mockingbird.repository.RoleRepository;
import com.tts.mockingbird.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepo;
	private RoleRepository roleRepo;
	private BCryptPasswordEncoder bCrypt;

	@Autowired
	public UserService(UserRepository userRepo, RoleRepository roleRepo, BCryptPasswordEncoder bCrypt) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.bCrypt = bCrypt;
	}

	public User findByUsername(String username) {
		return userRepo.findByUsername(username);
	}
	
	public List<User> findAll() {
		return (List<User>) userRepo.findAll();
	}
	
	public void save(User user) {
		userRepo.save(user);
	}
	
	public User saveNewUser(User user) {
		user.setPassword(bCrypt.encode(user.getPassword()));
		user.setActive(1);
		Role userRole = roleRepo.findByRole("USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		return userRepo.save(user);
	}
	
	public User getLoggedInUser() {
		String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		return findByUsername(loggedInUsername);
	}
	
}
