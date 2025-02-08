package club.lowscore.app.entity;

import java.util.HashSet;
import java.util.Set;

import club.lowscore.app.genericentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="t_post")
public class Post extends BaseEntity{
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false) // Foreign key to the User table
	private User user;
	
	@ManyToOne
	@JoinColumn(name="posttype_id", nullable=false)
	private PostType postType;
	
	@ManyToOne
    @JoinColumn(name = "accepted_answer_id", referencedColumnName = "id") // Foreign key to another Post
    private Post acceptedAnswer;
	
	@ManyToOne
	@JoinColumn(name="parent_question_id", referencedColumnName = "id") //Foreign key to questionPost
	private Post parentQuestionId;
	
	@Column(name="postTile", length=200)
	private String postTitle;
	
	@Column(name="postDetails", length=500)
	private String postDetails;
	
	@ManyToMany
	@JoinTable(
		name="t_post_tag",
		joinColumns = @JoinColumn(name = "post_id"),
		inverseJoinColumns = @JoinColumn(name = "tag_id") //The "inverse" term indicates it's the opposite side of the relationship from where you're defining it.
	)
	private Set<Tag> tags = new HashSet<>();

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Post getAcceptedAnswer() {
		return acceptedAnswer;
	}

	public void setAcceptedAnswer(Post acceptedAnswer) {
		this.acceptedAnswer = acceptedAnswer;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getPostDetails() {
		return postDetails;
	}

	public void setPostDetails(String postDetails) {
		this.postDetails = postDetails;
	}

	public PostType getPostType() {
		return postType;
	}

	public void setPostType(PostType postType) {
		this.postType = postType;
	}

	public Post getParentQuestionId() {
		return parentQuestionId;
	}

	public void setParentQuestionId(Post parentQuestionId) {
		this.parentQuestionId = parentQuestionId;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	
}
