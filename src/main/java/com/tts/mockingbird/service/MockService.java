package com.tts.mockingbird.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tts.mockingbird.model.Mock;
import com.tts.mockingbird.model.User;
import com.tts.mockingbird.repository.MockRepository;

@Service
public class MockService {
	
	@Autowired
	private MockRepository mockRepo;
	
	public List<Mock> findAll() {
		List<Mock> mocks = mockRepo.findAllByOrderByCreatedAtDesc();
		return mocks;
	}
	
	public List<Mock> findAllByUser(User user) {
		List<Mock> mocks = mockRepo.findAllByUserOrderByCreatedAtDesc(user);
		return mocks;
	}
	
	public List<Mock> findAllByUsers(List<User> users) {
		List<Mock> mocks = mockRepo.findAllByUserInOrderByCreatedAtDesc(users);
		return mocks;
	}
	
	public void save(Mock mock) {
		mockRepo.save(mock);
	}
}
