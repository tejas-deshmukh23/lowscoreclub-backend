package club.lowscore.app.dto;

import java.time.LocalDateTime;

//To create this class so we didn't need to return all the data of the user and the post along with the comments and also not need to fetch all the tags associated with the post
public class CommentDto {
	
	LocalDateTime createTime;
	LocalDateTime updateTime;
	String commentDescription;
	Long postId;
	Long userId;
	String userName;
	
	public CommentDto() {
		
	}

	public CommentDto(LocalDateTime createTime, LocalDateTime updateTime, String commentDescription, Long postId,
			Long userId, String userName) {
		super();
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.commentDescription = commentDescription;
		this.postId = postId;
		this.userId = userId;
		this.userName = userName;
	}

	public CommentDto(String commentDescription, Long postId, Long userId, String userName) {
		super();
		this.commentDescription = commentDescription;
		this.postId = postId;
		this.userId = userId;
		this.userName = userName;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getCommentDescription() {
		return commentDescription;
	}

	public void setCommentDescription(String commentDescription) {
		this.commentDescription = commentDescription;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	

}
