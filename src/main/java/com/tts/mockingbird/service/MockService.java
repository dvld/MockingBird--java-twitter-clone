package com.tts.mockingbird.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tts.mockingbird.model.HashTag;
import com.tts.mockingbird.model.Mock;
import com.tts.mockingbird.model.MockDisplay;
import com.tts.mockingbird.model.User;
import com.tts.mockingbird.repository.HashTagRepository;
import com.tts.mockingbird.repository.MockRepository;

@Service
public class MockService {
	
	@Autowired
  private MockRepository mockRepo;
  
  // needed from slide 54 //
  @Autowired
  private HashTagRepository hashtagRepo;
  
  // part 3 slide 28 // changed 'List<Mock>' to 'List<MockDisplay>' //
	public List<MockDisplay> findAll() {
    List<Mock> mocks = mockRepo.findAllByOrderByCreatedAtDesc();
    // part 2 slide 59 // wrap returned mocks(tweets) in  'formatMocks(formatTweets)' function //
		return formatMocks(mocks);
	}
  
  // part 3 slide 28 // changed 'List<Mock>' to 'List<MockDisplay>' //
	public List<MockDisplay> findAllByUser(User user) {
    List<Mock> mocks = mockRepo.findAllByUserOrderByCreatedAtDesc(user);
    // part 2 slide 59 // wrap returned mocks(tweets) in  'formatMocks(formatTweets)' function //
		return formatMocks(mocks);
	}
  
  // part 3 slide 28 // changed 'List<Mock>' to 'List<MockDisplay>' //
	public List<MockDisplay> findAllByUsers(List<User> users) {
    List<Mock> mocks = mockRepo.findAllByUserInOrderByCreatedAtDesc(users);
    // part 2 slide 59 // wrap returned mocks(tweets) in  'formatMocks(formatTweets)' function //
		return formatMocks(mocks);
  }
  
  // part 2 slide 62 //
  // part 3 slide 28 // changed 'List<Mock>' to 'List<MockDisplay>' //
  public List<MockDisplay> findAllWithTag(String hashtag) {
    List<Mock> mocks = mockRepo.findByhashtagsPhraseOrderByCreatedAtDesc(hashtag);
    return formatMocks(mocks);
  }
	
	public void save(Mock mock) {
    // part 2 slide 55 // added call to 'handleHashTags' function //
    handleHashTags(mock);
		mockRepo.save(mock);
  }
  
  // part 2 slide 53 // handles hashtags found in mocks(tweets) //
  public void handleHashTags(Mock mock) {
    List<HashTag> hashtags = new ArrayList<HashTag>();
    // Pattern and Matcher brought in from 'java.util.regex' //
    Pattern pattern = Pattern.compile("#\\w+");
    Matcher matcher = pattern.matcher(mock.getMessage());
    while (matcher.find()) {
      // part 2 slide 54 // slides do not tell you to create a variable of the hashtag repository //
      String phrase = matcher.group().substring(1).toLowerCase();
      HashTag hashtag = hashtagRepo.findByPhrase(phrase);
      
      if (hashtag == null) {
        hashtag = new HashTag();
        hashtag.setPhrase(phrase);
        hashtagRepo.save(hashtag);
      }
      hashtags.add(hashtag);
    }
    mock.setHashTags(hashtags);
  }

  // part 2 slide 57 // formatting mocks(tweets) //
  // part 3 slide 19 // changed 'List<Mock> to List<MockDisplay>' //
  private List<MockDisplay> formatMocks(List<Mock> mocks) {
    addHashTagLinks(mocks);
    // part 3 slide 6 // add shortenLinks function //
    shortenLinks(mocks);
    // part 3 slide 20 //
    List<MockDisplay> displayMocks = formatTimestamps(mocks);
    return displayMocks;
  }

  // part 3 slide 21 //
  private List<MockDisplay> formatTimestamps(List<Mock> mocks) {
    List<MockDisplay> response = new ArrayList<>();
    // part 3 slide 22 //
    PrettyTime prettyTime = new PrettyTime();
    SimpleDateFormat simpleDate = new SimpleDateFormat("M/d/yy");
    Date now = new Date();

    // part 3 slide 23 //
    for (Mock mock : mocks) {
      MockDisplay mockDisplay = new MockDisplay();
      mockDisplay.setUser(mock.getUser());
      mockDisplay.setMessage(mock.getMessage());
      mockDisplay.setHashtags(mock.getHashTags());

      // part 3 slide 24 //
      long diffInMilliseconds = Math.abs(now.getTime() - mock.getCreatedAt().getTime());
      long diff = TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);

      // part 3 slide 25 //
      if (diff > 3) {
        mockDisplay.setDate(simpleDate.format(mock.getCreatedAt()));
      }
      // part 3 slide 26 //
      else {
        mockDisplay.setDate(prettyTime.format(mock.getCreatedAt()));
      }
      // part 3 slide 27 //
      response.add(mockDisplay);
    }
    return response;
  }

  // part 2 slide 58 //
  private void addHashTagLinks(List<Mock> mocks) {
    Pattern pattern = Pattern.compile("#\\w+");

    for (Mock mock : mocks) {
      String message = mock.getMessage();
      Matcher matcher = pattern.matcher(message);
      Set<String> hashtags = new HashSet<String>();

      while(matcher.find()) {
        hashtags.add(matcher.group());
      }

      for (String hashtag : hashtags) {
        message = message.replaceAll(hashtag, "<a class=\"hashtag\" href=\"/mocks/" + hashtag.substring(1).toLowerCase() + "\">" + hashtag + "</a>");
      }
      mock.setMessage(message);
    }
  }

  // part 3 slide 7 //
  private void shortenLinks(List<Mock> mocks) {
    Pattern pattern = Pattern.compile("https?[^ ]+");

    // part 3 slide 8 //
    for (Mock mock: mocks) {
      // part 3 slide 9 //
      String message = mock.getMessage();
      Matcher matcher = pattern.matcher(message);
      while (matcher.find()) {
        String link = matcher.group();
        // part 3 slide 10 //
        String shortenedLink = link;
        if (link.length() > 23) {
          shortenedLink = link.substring(0, 20) + "...";
        }
        // part 3 slide 11 //
        message = message.replace(link, "<a class=\"tag\" href=\"" + link + "\"target=\"_blank\">" + shortenedLink + "</a>");
      }
      // part 3 slide 12 //
      mock.setMessage(message);
    }
  }



}
