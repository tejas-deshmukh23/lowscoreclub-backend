package club.lowscore.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import club.lowscore.app.entity.Notifications;
import club.lowscore.app.entity.Notifications.NotificationType;
import club.lowscore.app.entity.Post;
import club.lowscore.app.entity.User;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {

	Optional<Notifications> findByPostAndUserAndActorAndNotificationType(Post post, User user, User actor,
			NotificationType notificationType);

//	List<Notifications> findByUserAndNotificationType(User user, NotificationType postMessage);
	
	@Query("SELECT n FROM Notifications n JOIN FETCH n.post p JOIN FETCH p.tags WHERE n.user = :user AND n.notificationType = :type")
	List<Notifications> findByUserAndNotificationType(@Param("user") User user, @Param("type") NotificationType type);
	
	

}
