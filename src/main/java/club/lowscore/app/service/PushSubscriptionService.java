package club.lowscore.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import club.lowscore.app.dto.PushSubscriptionDto;
import club.lowscore.app.entity.PushSubscription;
import club.lowscore.app.entity.User;
import club.lowscore.app.exception.PushSubscriptionNotFoundException;
import club.lowscore.app.exception.UserNotFoundException;
import club.lowscore.app.repository.PushSubscriptionRepository;
import club.lowscore.app.repository.UserRepository;

@Service
public class PushSubscriptionService {
	
	@Autowired
    private PushSubscriptionRepository pushSubscriptionRepository;
    
    @Autowired
    private UserRepository userRepository;  // You'll need this to fetch user entities

    public PushSubscription saveSubscription(Long userId, PushSubscriptionDto dto) {
//        User user = userRepository.findById(userId)
//            .orElseThrow(() -> new UserNotFoundException("User not found"));
    	
//    	1.Check if user exists
    	Optional<User> user = userRepository.findById(userId);
    	if(user.isPresent()) {
    		
//    		2.Check if subscription exists
    		Optional<PushSubscription> existingSubscription = pushSubscriptionRepository.findByEndpoint(dto.getEndpoint());
    		if(existingSubscription.isPresent()) {
    			
//    			2.a. If exists then update it
    			PushSubscription subscription = existingSubscription.get();
                subscription.setP256dhKey(dto.getKeys().getP256dh());
                subscription.setAuthKey(dto.getKeys().getAuth());
                subscription.setBrowserInfo(dto.getBrowserInfo());
                subscription.setDeviceType(dto.getDeviceType());
                subscription.setActive(true);
                return pushSubscriptionRepository.save(subscription);
    			
    			
    		}else {
    			
//    			2.b. If not exists then create new subscription
    			PushSubscription pushSubscription = new PushSubscription();
    			pushSubscription.setUser(user.get());
    			pushSubscription.setEndpoint(dto.getEndpoint());
    			pushSubscription.setP256dhKey(dto.getKeys().getP256dh());
    			pushSubscription.setAuthKey(dto.getKeys().getAuth());
    			pushSubscription.setBrowserInfo(dto.getBrowserInfo());
    			pushSubscription.setDeviceType(dto.getDeviceType());
    			
    			return pushSubscriptionRepository.save(pushSubscription);
    		}
    		
    	}else {
    		throw new UserNotFoundException("User with id :: "+userId+" not found");
    	}
        
    }
    
    public void removeSubscription(String endpoint) {
//        PushSubscription subscription = subscriptionRepository.findByEndpoint(endpoint)
//            .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));
//        subscription.setActive(false);
//        subscriptionRepository.save(subscription);
    	
    	Optional<PushSubscription> pushSubscription = pushSubscriptionRepository.findByEndpoint(endpoint);
    	if(pushSubscription.isPresent()) {
    		
    		PushSubscription subscription = pushSubscription.get();
    		subscription.setActive(false);
    		
    		pushSubscriptionRepository.save(subscription);
    		
    		
    	}else {
    		throw new PushSubscriptionNotFoundException("pushSubscription not found");
    	}
    	
    	
    }
    
    public List<PushSubscription> getAllActiveSubscriptions() {
        return pushSubscriptionRepository.findAllActiveSubscriptions();
    }

    public List<PushSubscription> getUserSubscriptions(User user) {
        return pushSubscriptionRepository.findByUserAndIsActiveTrue(user);
    }

}
