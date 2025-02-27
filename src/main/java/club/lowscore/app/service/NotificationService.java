package club.lowscore.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import club.lowscore.app.entity.Notifications;
import club.lowscore.app.entity.Post;
import club.lowscore.app.entity.User;
import club.lowscore.app.exception.PostNotFoundException;
import club.lowscore.app.exception.UserNotFoundException;
import club.lowscore.app.repository.NotificationsRepository;
import club.lowscore.app.repository.PostRepository;
import club.lowscore.app.repository.UserRepository;
import club.lowscore.app.util.StringUtil;

@Service
public class NotificationService {
	
	@Autowired
	NotificationsRepository notificationsRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PostRepository postRepository;
	
	
	//To call this function to add the notifications in the notifications table
	public Boolean addNotifications(String actionUrl, int aggregateCount, Boolean isRead, String logo, String message, String notificationType, String title, String actorId, String userId, String postId)
	{
		if(StringUtil.nullOrEmpty(userId) ||  StringUtil.nullOrEmpty(actorId) || StringUtil.nullOrEmpty(postId)) {
			throw new IllegalArgumentException("Some ids are null");
		}
		Optional<User> user = userRepository.findById(Long.parseLong(userId));
		if(user.isEmpty()) {
			throw new UserNotFoundException("User with id :: "+userId+" not found");
		}
		
		Optional<User> actor = userRepository.findById(Long.parseLong(actorId));
		if(actor.isEmpty()) {
			throw new UserNotFoundException("Actor with id : "+actorId+" not found");
		}
		
		Optional<Post> post = postRepository.findById(Long.parseLong(postId));
		if(post.isEmpty()) {
			throw new PostNotFoundException("Post with id : "+postId+" not found");
		}
		
		Notifications notifications = new Notifications();
		notifications.setUser(user.get());
		notifications.setActor(actor.get());
		notifications.setPost(post.get());
		notifications.setActionUrl(actionUrl);
		notifications.setAggregateCount(aggregateCount);
		notifications.setIsRead(isRead);
		notifications.setLogo(logo);
		notifications.setMessage(message);
		if(notificationType.equalsIgnoreCase("LIKE")) {
			notifications.setNotificationType(Notifications.NotificationType.LIKE);
		}else if(notificationType.equalsIgnoreCase("COMMENT")) {
			notifications.setNotificationType(Notifications.NotificationType.COMMENT);
		}else if(notificationType.equalsIgnoreCase("FOLLOW")) {
			notifications.setNotificationType(Notifications.NotificationType.FOLLOW);
		}else if(notificationType.equalsIgnoreCase("MENTION")) {
			notifications.setNotificationType(Notifications.NotificationType.MENTION);
		}else if(notificationType.equalsIgnoreCase("SYSTEM_MESSAGE")) {
			notifications.setNotificationType(Notifications.NotificationType.SYSTEM_MESSAGE);
		}else if(notificationType.equalsIgnoreCase("POST_MESSAGE")) {
			notifications.setNotificationType(Notifications.NotificationType.POST_MESSAGE);
		}
		notifications.setTitle(title);
		
		notificationsRepository.save(notifications);
		
		return true;
	}
	
}
