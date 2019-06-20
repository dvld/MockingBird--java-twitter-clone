package com.tts.mockingbird.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MockDisplay {

  private User user;
  private String message;
  private String date;
  private List<HashTag> hashtags;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public List<HashTag> getHashtags() {
    return hashtags;
  }

  public void setHashtags(List<HashTag> hashtags) {
    this.hashtags = hashtags;
  }
}