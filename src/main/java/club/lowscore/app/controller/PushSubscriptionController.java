package club.lowscore.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import club.lowscore.app.service.PushSubscriptionService;

@RestController
@CrossOrigin("*")
public class PushSubscriptionController {
	
	@Autowired
	PushSubscriptionService pushSubscriptionService;
	
	

}
