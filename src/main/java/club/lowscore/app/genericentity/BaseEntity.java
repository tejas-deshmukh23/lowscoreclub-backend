package club.lowscore.app.genericentity;

import java.io.Serializable;
//import java.util.Date;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseEntity implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="createtime", updatable=false, nullable=false)
	private LocalDateTime createTime;
	
	@Column(name="updatetime")
	private LocalDateTime updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	// Lifecycle Callback Methods
    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now(); // Set the creation time
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now(); // Set the update time
    }
    
//    Explanation:
//    	@PrePersist:
//
//    	This method is executed automatically before the entity is persisted (saved for the first time).
//    	It sets the createTime field to the current timestamp.
//    	@PreUpdate:
//
//    	This method is executed automatically before the entity is updated.
//    	It sets the updateTime field to the current timestamp.
	
}
