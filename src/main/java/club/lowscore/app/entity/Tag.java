package club.lowscore.app.entity;

import java.util.HashSet;
import java.util.Set;

import club.lowscore.app.genericentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="t_tag")
public class Tag extends BaseEntity{
	
	@Column(name="name", length=50)
	private String tagName;
	
	@Column(name="description", length=100)
	private String description;
	
	@ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
