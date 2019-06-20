// part 2 slide 50 // creates a repository for hashtags //
package com.tts.mockingbird.repository;

import com.tts.mockingbird.model.HashTag;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagRepository extends CrudRepository<HashTag, Long> {
  HashTag findByPhrase(String phrase);
}

