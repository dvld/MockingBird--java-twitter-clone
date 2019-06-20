package com.tts.mockingbird.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HashTag {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "hashtag_id")
  private Long id;

  private String phrase;

  @ManyToMany(mappedBy = "hashtags")
  private List<Mock> mocks;

  public Long getId() {
    return id;
  }

  public String getPhrase() {
    return phrase;
  }

  public void setPhrase(String phrase) {
    this.phrase = phrase;
  }

  public List<Mock> getMocks() {
    return mocks;
  }

  public void setMocks(List<Mock> mocks) {
    this.mocks = mocks;
  }
}