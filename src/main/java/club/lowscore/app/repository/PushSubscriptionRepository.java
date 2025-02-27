package club.lowscore.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import club.lowscore.app.entity.PushSubscription;
import club.lowscore.app.entity.User;

public interface PushSubscriptionRepository extends JpaRepository<PushSubscription, Long> {
	
//	List<PushSubscription> findByUser(User user);
//    void deleteByUserAndEndpoint(User user, String endpoint);
	
	List<PushSubscription> findByUserAndIsActiveTrue(User user);
    Optional<PushSubscription> findByEndpoint(String endpoint);
    void deleteByEndpoint(String endpoint);
    
    @Query("SELECT ps FROM PushSubscription ps WHERE ps.isActive = true")
    List<PushSubscription> findAllActiveSubscriptions();

}
