package club.lowscore.app.service;

//OtpService.java
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {
 private final Map<String, OtpData> otpMap = new HashMap<>();
 
 private static class OtpData {
     String otp;
     long expiryTime;
     
     OtpData(String otp) {
         this.otp = otp;
         // Set expiry to 5 minutes from now
         this.expiryTime = System.currentTimeMillis() + (5 * 60 * 1000);
     }
     
     boolean isExpired() {
         return System.currentTimeMillis() > expiryTime;
     }
 }
 
 public String generateOtp(String email) {
     String otp = String.format("%06d", new Random().nextInt(999999));
     otpMap.put(email, new OtpData(otp));
     return otp;
 }
 
 public boolean validateOtp(String email, String otp) {
     OtpData otpData = otpMap.get(email);
     if (otpData == null || otpData.isExpired()) {
         return false;
     }
     boolean isValid = otpData.otp.equals(otp);
     if (isValid) {
         otpMap.remove(email); // Remove OTP after successful validation
     }
     return isValid;
 }
}
