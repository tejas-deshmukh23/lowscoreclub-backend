package club.lowscore.app.entity;

import club.lowscore.app.genericentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="t_vote_count")
public class VoteCount extends BaseEntity {
	
	@OneToOne
	@JoinColumn(name="post_id", referencedColumnName="id", nullable=false, unique=true )
	private Post post;
	
	@Column(name="like_count")
	private Long likeCount;
	
	@Column(name="dislike_count")
	private Long dislikeCount;
	
	
	
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public Long getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Long likeCount) {
		this.likeCount = likeCount;
	}
	public Long getDislikeCount() {
		return dislikeCount;
	}
	public void setDislikeCount(Long dislikeCount) {
		this.dislikeCount = dislikeCount;
	}
	
}
