package club.lowscore.app.service;

//EmailService.java
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
 private final JavaMailSender mailSender;

 public EmailService(JavaMailSender mailSender) {
     this.mailSender = mailSender;
 }

 public void sendOtpEmail(String to, String otp) {
     SimpleMailMessage message = new SimpleMailMessage();
     message.setFrom("deshmukht100@gmail.com");
     message.setTo(to);
     message.setSubject("Your OTP for Registration");
     message.setText("Your OTP is: " + otp + "\nThis OTP is valid for 5 minutes.");
     
     mailSender.send(message);
 }
}
