//package club.lowscore.app.entity;
//
//import club.lowscore.app.genericentity.BaseEntity;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.JoinTable;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//
//@Entity
//@Table(name="t_notifications")
//public class Notifications extends BaseEntity {
//	
//	@Column(name="logo")
//	private String logo;```	
//	
//	@Column(name="message")
//	private String message;
//	
//	@Column(name="title")
//	private String title;
//	
//	@ManyToOne
//	@JoinColumn(name="user_id", nullable=false)
//	private User user;
//	
//	@Column(name="post")
//	@JoinColumn(name="post_id", nullable=false)
//	private Post post;
//
//	public String getLogo() {
//		return logo;
//	}
//
//	public void setLogo(String logo) {
//		this.logo = logo;
//	}
//
//	public String getMessage() {
//		return message;
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}
//
//	public String getTitle() {
//		return title;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}
//
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	public Post getPost() {
//		return post;
//	}
//
//	public void setPost(Post post) {
//		this.post = post;
//	}
//
//}

package club.lowscore.app.entity;

import club.lowscore.app.genericentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="t_notifications")
public class Notifications extends BaseEntity {

    public enum NotificationType {
        LIKE, 
        COMMENT, 
        FOLLOW, 
        MENTION,
        SYSTEM_MESSAGE,
        POST_MESSAGE
    }

    @Column(name="title")
    private String title;
    
    @Column(name="message", nullable=false)
    private String message;
    
    @Column(name="logo")
    private String logo;
    
    @Column(name="is_read", nullable=false)
    private boolean isRead = false;
    
    @Enumerated(EnumType.STRING)
    @Column(name="notification_type", nullable=false)
    private NotificationType notificationType;
    
    // The user who RECEIVES the notification
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    
    // The user who TRIGGERED the notification (optional)
    @ManyToOne
    @JoinColumn(name="actor_id")
    private User actor;
    
    // The post related to this notification (optional)
    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
    
    // For aggregated notifications (e.g., "X and 5 others liked your post")
    @Column(name="aggregate_count")
    private Integer aggregateCount = 0;
    
    // URL or action path for when user clicks notification
    @Column(name="action_url")
    private String actionUrl;

    // Getters and setters
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Integer getAggregateCount() {
        return aggregateCount;
    }

    public void setAggregateCount(Integer aggregateCount) {
        this.aggregateCount = aggregateCount;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
}
