package club.lowscore.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import club.lowscore.app.dto.NotificationPayload;
import club.lowscore.app.entity.Notifications;
import club.lowscore.app.entity.Notifications.NotificationType;
import club.lowscore.app.entity.Post;
import club.lowscore.app.entity.User;
import club.lowscore.app.exception.PostNotFoundException;
import club.lowscore.app.exception.UserNotFoundException;
import club.lowscore.app.repository.NotificationsRepository;
import club.lowscore.app.repository.PostRepository;
import club.lowscore.app.repository.UserRepository;
import club.lowscore.app.util.StringUtil;
import jakarta.transaction.Transactional;

@Service
public class NotificationService {
	
	@Autowired
	NotificationsRepository notificationsRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PostRepository postRepository;
	
	
	//To call this function to add the notifications in the notifications table
	public Boolean addNotifications(String actionUrl, String aggregateCount, Boolean isRead, String logo, String message, String notificationType, String title, String actorId, String userId, String postId)
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
		
		NotificationType notificationTypeEnum = null ;
		if(notificationType.equalsIgnoreCase("LIKE")) {
			notificationTypeEnum = NotificationType.LIKE;
		}else if(notificationType.equalsIgnoreCase("COMMENT")) {
			notificationTypeEnum = NotificationType.COMMENT;
		}else if(notificationType.equalsIgnoreCase("FOLLOW")) {
			notificationTypeEnum = NotificationType.FOLLOW;
		}else if(notificationType.equalsIgnoreCase("MENTION")) {
			notificationTypeEnum = NotificationType.MENTION;
		}else if(notificationType.equalsIgnoreCase("SYSTEM_MESSAGE")) {
			notificationTypeEnum = NotificationType.SYSTEM_MESSAGE;
		}else if(notificationType.equalsIgnoreCase("POST_MESSAGE")) {
			notificationTypeEnum = NotificationType.POST_MESSAGE;
		}
		
		
		//here we will need to check if their is any notification present in the db with the same postId, userId, actorId, NotificationTypeif present then we will not save that double
		Optional<Notifications> optionalNotifications = notificationsRepository.findByPostAndUserAndActorAndNotificationType(post.get(),user.get(), actor.get(), notificationTypeEnum);
		if(optionalNotifications.isPresent()) {
			return false;
		}
		
		Notifications notifications = new Notifications();
		notifications.setUser(user.get());
		notifications.setActor(actor.get());
		notifications.setPost(post.get());
		notifications.setActionUrl(actionUrl);
		notifications.setAggregateCount(Integer.parseInt(aggregateCount));
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


	@Transactional
	public List<Notifications> getAllPostTypeNotifications(String userId) {
		
		Optional<User> user = userRepository.findById(Long.parseLong(userId));
		if(user.isEmpty()) {
			throw new UserNotFoundException("User with id : "+userId+" not found");
		}
		
//		List<NotificationPayload> notificationDto;
		List<Notifications> notificationsList = notificationsRepository.findByUserAndNotificationType(user.get(), NotificationType.POST_MESSAGE);
		return notificationsList;
		
	}
	
}
