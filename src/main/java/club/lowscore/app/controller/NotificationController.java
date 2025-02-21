package club.lowscore.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import club.lowscore.app.model.Subscription;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {
    private final List<Subscription> subscriptions = new ArrayList<>();
    
private final PushService pushService;
    
    // Constructor injection of PushService bean
    public NotificationController(PushService pushService) {
        this.pushService = pushService;
    }
    
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody Subscription subscription) {
        subscriptions.add(subscription);
        return ResponseEntity.ok("Subscription successful");
    }
    
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody String message) {
        for (Subscription subscription : subscriptions) {
            try {
                pushService.send(new Notification(
                    subscription.getEndpoint(),
                    subscription.getKeys().getP256dh(),
                    subscription.getKeys().getAuth(),
                    message
                ));
            } catch (Exception e) {
                // Handle error
            }
        }
        return ResponseEntity.ok("Notification sent");
    }
}