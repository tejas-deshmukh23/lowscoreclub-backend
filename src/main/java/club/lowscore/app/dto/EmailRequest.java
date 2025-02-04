package club.lowscore.app.dto;

//EmailRequest.java
public class EmailRequest {
 private String email;
 
 // Default constructor
 public EmailRequest() {}
 
 // Constructor with parameter
 public EmailRequest(String email) {
     this.email = email;
 }
 
 // Getter and Setter
 public String getEmail() {
     return email;
 }
 
 public void setEmail(String email) {
     this.email = email;
 }
}