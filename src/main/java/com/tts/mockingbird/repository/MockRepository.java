package com.tts.mockingbird.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.mockingbird.model.Mock;
import com.tts.mockingbird.model.User;

@Repository
public interface MockRepository extends CrudRepository<Mock, Long> {

	List<Mock> findAllByOrderByCreatedAtDesc();
	List<Mock> findAllByUserOrderByCreatedAtDesc(User user);
	List<Mock> findAllByUserInOrderByCreatedAtDesc(List<User> users);
}
