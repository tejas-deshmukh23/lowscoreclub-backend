package club.lowscore.app.entity;

import club.lowscore.app.genericentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="t_comment_count")
public class CommentCount extends BaseEntity {
	
	@OneToOne
	@JoinColumn(name="post_id", referencedColumnName="id", nullable=false, unique=true )
	private Post post;
	
	@Column(name="comment_count")
	private Long commentCount;
	
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public Long getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Long commentCount) {
		this.commentCount = commentCount;
	}
	
}
