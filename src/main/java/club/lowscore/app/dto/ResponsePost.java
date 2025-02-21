package club.lowscore.app.dto;

import java.time.LocalDateTime;

public class ResponsePost {
	
	Long responseId;
	String createdBy;
	String postDetails;
	LocalDateTime responseCreatedTime;
	Long createdById;
	
	public ResponsePost() {
		// TODO Auto-generated constructor stub
	}
	
	public ResponsePost(Long responseId, String createdBy, String postDetails, LocalDateTime responseCreatedTime, Long createdById) {
		super();
		this.responseId = responseId;
		this.createdBy = createdBy;
		this.postDetails = postDetails;
		this.responseCreatedTime = responseCreatedTime;
		this.createdById = createdById;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getPostDetails() {
		return postDetails;
	}
	public void setPostDetails(String postDetails) {
		this.postDetails = postDetails;
	}
	public LocalDateTime getResponseCreatedTime() {
		return responseCreatedTime;
	}
	public void setResponseCreatedTime(LocalDateTime responseCreatedTime) {
		this.responseCreatedTime = responseCreatedTime;
	}

	public Long getResponseId() {
		return responseId;
	}

	public void setResponseId(Long responseId) {
		this.responseId = responseId;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

}
