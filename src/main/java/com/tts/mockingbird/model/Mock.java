package com.tts.mockingbird.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Mock {

	// properties //
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mock_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	
	@NotEmpty(message = "Mock cannot be empty")
	@Length(max = 280, message = "Mock cannot have more than 280 characters")
  private String message;
  
  // part 2 slide 49 // establishes relationship between mocks(tweets) and hashtags //
  @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  @JoinTable(name = "mock_hashtag", joinColumns = @JoinColumn(name = "mock_id"),
  inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
  private List<HashTag> hashtags;
	
	@CreationTimestamp
	private Date createdAt;

	// getters and setters //
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
  }
  
  public List<HashTag> getHashTags() {
    return hashtags;
  }

  public void setHashTags(List<HashTag> hashtags) {
    this.hashtags = hashtags;
  }

	@Override
	public String toString() {
		return "Mock [id=" + id + ", user=" + user + ", message=" + message + ", createdAt=" + createdAt + "]";
	}
	
}
