package club.lowscore.app.dto;

public class OtpVerificationRequest {
    private String email;
    private String otp;
    
    // Default constructor
    public OtpVerificationRequest() {}
    
    // Constructor with parameters
    public OtpVerificationRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }
    
    // Getters and Setters
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getOtp() {
        return otp;
    }
    
    public void setOtp(String otp) {
        this.otp = otp;
    }
}
