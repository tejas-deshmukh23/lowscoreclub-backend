package club.lowscore.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import club.lowscore.app.dto.NotificationPayload;
import club.lowscore.app.dto.NotificationResult;
import club.lowscore.app.dto.PushSubscriptionDto;
import club.lowscore.app.entity.PushSubscription;
import club.lowscore.app.model.Subscription;
import club.lowscore.app.service.PushSubscriptionService;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
	
    private final List<Subscription> subscriptions = new ArrayList<>();
    
private final PushService pushService;
private final PushSubscriptionService pushSubscriptionService;
//private final RestTemplate restTemplate;
    
    // Constructor injection of PushService bean
    public NotificationController(PushService pushService, PushSubscriptionService pushSubscriptionService) {
        this.pushService = pushService;
        this.pushSubscriptionService = pushSubscriptionService;
    }
    
//    @PostMapping("/subscribe")
//    public ResponseEntity<String> subscribe(@RequestBody Subscription subscription) {
//        subscriptions.add(subscription);
//        return ResponseEntity.ok("Subscription successful");
//    }
    
   
    
    @PostMapping("/subscribe")
    @CrossOrigin("*")
    public ResponseEntity<?> subscribe(@RequestBody(required=false) PushSubscriptionDto pushSubscriptionDto, @RequestParam(name="userId", required=false) String userId) {
        try {
            PushSubscription savedSubscription = pushSubscriptionService.saveSubscription(Long.parseLong(userId), pushSubscriptionDto);
//            log.info("New subscription saved for endpoint: {}", subscription.getEndpoint());
            
            return ResponseEntity.ok(savedSubscription);
        } catch (Exception e) {
//            log.error("Error saving subscription", e);
        	e.printStackTrace();
            return ResponseEntity.badRequest()
                .body("Failed to save subscription: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@RequestParam String endpoint) {
        try {
        	pushSubscriptionService.removeSubscription(endpoint);
            return ResponseEntity.ok("Unsubscribed successfully");
        } catch (Exception e) {
//            log.error("Unsubscription failed", e);
//        	System.out.println()
        	e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to unsubscribe: " + e.getMessage());
        }
    }
    
//    @PostMapping("/send")
//    @CrossOrigin("*")
//    public ResponseEntity<String> sendNotification(@RequestBody String message) {
//        for (Subscription subscription : subscriptions) {
//            try {
//                pushService.send(new Notification(
//                    subscription.getEndpoint(),
//                    subscription.getKeys().getP256dh(),
//                    subscription.getKeys().getAuth(),
//                    message
//                ));
//            } catch (Exception e) {
//            	
//            	e.printStackTrace();
//                // Handle error
//            }
//        }
//        return ResponseEntity.ok("Notification sent");
//    }
    
    //actor id is the id of the actor who sends the notification
    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationPayload payload, @RequestParam(required=false) String actorId, @RequestParam(required = false) String postId ) {
        try {
            List<PushSubscription> subscriptions = pushSubscriptionService.getAllActiveSubscriptions();
            int successCount = 0;
            int failureCount = 0;
            
            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(payload);

            for (PushSubscription subscription : subscriptions) {
                try {
                    pushService.send(new Notification(
                        subscription.getEndpoint(),
                        subscription.getP256dhKey(),
                        subscription.getAuthKey(),
                        jsonPayload
                    ));
                    
                    //here we will add the notifications to store in the db by calling that addNotifications function
                    
                    postRepository.
                    
                    successCount++;
                } catch (Exception e) {
//                    log.error("Failed to send to: " + subscription.getEndpoint(), e);
                	e.printStackTrace();
                    failureCount++;
                    if (isSubscriptionExpired(e)) {
                        pushSubscriptionService.removeSubscription(subscription.getEndpoint());
                    }
                }
            }
            
            return ResponseEntity.ok(new NotificationResult(successCount, failureCount));
        } catch (Exception e) {
//            log.error("Notification sending failed", e);
        	e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to send notifications");
        }
    }
    
    private boolean isSubscriptionExpired(Exception e) {
        String message = e.getMessage().toLowerCase();
        return message.contains("410") || 
               message.contains("404") || 
               message.contains("expired");
    }
}