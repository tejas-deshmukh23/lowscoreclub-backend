package club.lowscore.app.entity;

import club.lowscore.app.genericentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="t_post_tag")
public class PostTag extends BaseEntity{
	
	@ManyToOne
	@JoinColumn(name="post_id", nullable=false)
	private Post postId;
	
	@ManyToOne
	@JoinColumn(name="tag_id", nullable=false)
	private Tag tagId;

	public Post getPostId() {
		return postId;
	}

	public void setPostId(Post postId) {
		this.postId = postId;
	}

	public Tag getTagId() {
		return tagId;
	}

	public void setTagId(Tag tagId) {
		this.tagId = tagId;
	}
	
}
