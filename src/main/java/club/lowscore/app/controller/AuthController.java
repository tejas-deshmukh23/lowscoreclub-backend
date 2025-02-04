package club.lowscore.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
//AuthController.java
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import club.lowscore.app.dto.EmailRequest;
import club.lowscore.app.dto.OtpVerificationRequest;
import club.lowscore.app.service.EmailService;
import club.lowscore.app.service.LoginService;
import club.lowscore.app.service.OtpService;
import club.lowscore.app.service.SignupService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Adjust according to your frontend URL
public class AuthController {
 private final OtpService otpService;
 private final EmailService emailService;
 
 @Autowired
 SignupService signupService;
 
 @Autowired
 LoginService loginService;
 
 public AuthController(OtpService otpService, EmailService emailService) {
     this.otpService = otpService;
     this.emailService = emailService;
 }
 
 @PostMapping("/send-otp")
 public ResponseEntity<?> sendOtp(@RequestBody EmailRequest request) {
     try {
         String otp = otpService.generateOtp(request.getEmail());
         emailService.sendOtpEmail(request.getEmail(), otp);
         return ResponseEntity.ok().body(Map.of("message", "OTP sent successfully"));
     } catch (Exception e) {
    	 e.printStackTrace();
         return ResponseEntity.badRequest().body(Map.of("error", "Failed to send OTP"));
     }
 }
 
 @PostMapping("/verify-otp")
 public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request) {
     boolean isValid = otpService.validateOtp(request.getEmail(), request.getOtp());
     if (isValid) {
         return ResponseEntity.ok().body(Map.of("message", "OTP verified successfully"));
     }
     return ResponseEntity.badRequest().body(Map.of("error", "Invalid OTP"));
 }
 
 @PostMapping("/signup")
 public ResponseEntity<?> signup(@RequestParam(name="email", required=true) String email,
		 						 @RequestParam(name="username", required=true) String username,
		 						 @RequestParam(name="password", required=true) String password,
		 						 @RequestParam(name="dob", required=true) String dob,
		 						 @RequestParam(name="pincode", required=false) String pincode) {
	 
//     if (isValid) {
//         return ResponseEntity.ok().body(Map.of("message", "OTP verified successfully"));
//     }
	 
	 try {
		 	Boolean result = signupService.signup(email, username, password, dob, pincode);
		 	if(result) {
		 		return ResponseEntity.ok().body(Map.of("message","User registered successfully"));
//		 		return ResponseEntity.ok().body(Map.of("message", "OTP verified successfully"));
		 	}else {
		 		
		 	}
	 }
	 catch(DataIntegrityViolationException exception) {
	        String message = exception.getMessage().toLowerCase();
//	        if (message.contains("uk_email")) {
//	            return ResponseEntity.status(HttpStatus.CONFLICT)
//	                               .body(Map.of("error", "Email address is already in use"));
//	        } else if (message.contains("uk_user_name")) {
//	            return ResponseEntity.status(HttpStatus.CONFLICT)
//	                               .body(Map.of("error", "Username is already taken"));
//	        }
	        
	        if (message.contains("email")) {
	            return ResponseEntity.status(HttpStatus.CONFLICT)
	                                 .body(Map.of("error", "Email address is already in use"));
	        } else if (message.contains("user name")) {
	            return ResponseEntity.status(HttpStatus.CONFLICT)
	                                 .body(Map.of("error", "Username is already taken"));
	        }
	        
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                           .body(Map.of("error", "This account already exists"));
	    }
	 catch(Exception e) {
		 
//		 System.out.println("We are printing this exception from catch of authcontroller");
		 e.printStackTrace();
	 }
     return ResponseEntity.badRequest().body(Map.of("error", "data not saved"));
 }
 
 @PostMapping("/validate")
	public ResponseEntity<?> validateUser(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password,
			HttpServletRequest request, HttpServletResponse response){
		
		return loginService.validateUser(email, password, request,response);
		
	}
 
}
