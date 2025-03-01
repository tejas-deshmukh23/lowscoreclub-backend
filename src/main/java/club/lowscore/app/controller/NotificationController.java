package club.lowscore.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
import club.lowscore.app.entity.Notifications;
import club.lowscore.app.entity.PushSubscription;
import club.lowscore.app.model.Subscription;
import club.lowscore.app.service.NotificationService;
import club.lowscore.app.service.PushSubscriptionService;
import jakarta.transaction.Transactional;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
	
    private final List<Subscription> subscriptions = new ArrayList<>();
    
private final PushService pushService;
private final PushSubscriptionService pushSubscriptionService;
private final RestTemplate restTemplate;

@Autowired
NotificationService notificationService;
    
	
    // Constructor injection of PushService bean
	@Autowired
    public NotificationController(PushService pushService, PushSubscriptionService pushSubscriptionService, RestTemplate restTemplate) {
        this.pushService = pushService;
        this.pushSubscriptionService = pushSubscriptionService;
        this.restTemplate = restTemplate;
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
            
            //-----------------------Here we are calling another api which will give us the users which we want to send the notification---------
            
         // Construct the relative URL to the other API (same project)
            String apiUrl = "http://localhost:8080/getUserOfPostWithTags?postId=" + postId; // Relative URL
            
         // Send a POST request to the target API (same project)
            ResponseEntity<Set<Long>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET, // Specify that this is a POST request
                null, // If you need to send any body, you can use HttpEntity with your payload
                new ParameterizedTypeReference<Set<Long>>() {}
            );
            
         // Get the response body
            Set<Long> userIds = response.getBody();
            
            //------------------------------------------------------------------------------------------------------------


            for (PushSubscription subscription : subscriptions) {
            	
            	for(Long userId : userIds) {
            		if(userId == subscription.getUser().getId()) {
            			
            			//logic to send the pushnotifications
            			
            			try {
                            pushService.send(new Notification(
                                subscription.getEndpoint(),
                                subscription.getP256dhKey(),
                                subscription.getAuthKey(),
                                jsonPayload
                            ));
                            
//<---------------------------------------------------------------Here we will call our saveNotifications api ---------------------------------------------------------------->
                            
                            String saveNotificationApiUrl = "http://localhost:8080/api/notifications/saveNotifications"; // Relative URL
                            
                            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                            params.add("actionUrl", payload.getUrl());
                            params.add("aggregateCount", "0");
                            params.add("isRead", "false");
                            params.add("logo", payload.getIcon());
                            params.add("message", payload.getMessage());
                            params.add("notificationType", "POST_MESSAGE");
                            params.add("title", payload.getTitle());
                            params.add("actorId", actorId);
                            params.add("userId", String.valueOf(subscription.getUser().getId()));
                            params.add("postId", postId);
                            
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                            // Create HttpEntity with parameters
                            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

                            // Send POST request with form data
                            try {
                                ResponseEntity<Boolean> response2 = restTemplate.exchange(
                                		saveNotificationApiUrl,
                                    HttpMethod.POST,
                                    entity,
                                    Boolean.class
                                );
                                
                                Boolean result = response2.getBody();
                                
                            }catch(Exception e) {
                            	e.printStackTrace();
                            }
                                // Process the response
                               
//<--------------------------------------------------------------------------------------------------------------------------------------------------------------------------->                            

                            
                            successCount++;
                        } catch (Exception e) {
//                            log.error("Failed to send to: " + subscription.getEndpoint(), e);
                        	e.printStackTrace();
                            failureCount++;
                            if (isSubscriptionExpired(e)) {
                                pushSubscriptionService.removeSubscription(subscription.getEndpoint());
                            }
                        }

            			
            			//----------------------------------
            			
            		}else {
            			System.out.println("Notification not sent to user : "+userId+" because he hadn't used that related tags");
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
    
    @PostMapping("/saveNotifications")
    public ResponseEntity<?> addNotifications(@RequestParam(required = false) String actionUrl, @RequestParam(required = false) String aggregateCount, @RequestParam(required = false) Boolean isRead, @RequestParam(required = false) String logo, @RequestParam(required = false) String message, @RequestParam(required = false) String notificationType, @RequestParam(required = false) String title, @RequestParam(required = false) String actorId, @RequestParam(required = false) String userId, @RequestParam(required = false) String postId )
    {
    	
    	try {
    		Boolean result = notificationService.addNotifications(actionUrl, aggregateCount, isRead, logo, message, notificationType, title, actorId, userId, postId);
    		return ResponseEntity.ok(result);//here we should return the notification in response so that we can 
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    		throw e;
    	}
    	
    	
    }
    
    
    @PostMapping("/getAllPostTypeNotifications")
    @CrossOrigin("*")
//    @Transactional
    public ResponseEntity<List<Notifications>> getAllPostTypeNotifications(@RequestParam(name = "userId", required = false) String userId){
    	try {
    		List<Notifications> list = notificationService.getAllPostTypeNotifications(userId);
    		return ResponseEntity.ok(list);
    	}catch(Exception e) {
    		e.printStackTrace();
    		throw e;
    	}
    }
    
    
}