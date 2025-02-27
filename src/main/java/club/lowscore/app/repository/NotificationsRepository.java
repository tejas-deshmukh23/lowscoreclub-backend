package club.lowscore.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.lowscore.app.entity.Notifications;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
	
	

}
