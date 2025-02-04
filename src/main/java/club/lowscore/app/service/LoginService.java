package club.lowscore.app.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import club.lowscore.app.entity.User;
import club.lowscore.app.repository.UserRepository;
import club.lowscore.app.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class LoginService {
	
	@Autowired
	UserRepository userRepository;
	
	public ResponseEntity<?> validateUser(String email, String password, HttpServletRequest request, HttpServletResponse response) {
//      User user = loginRepository.findByUsernameAndPassword(username, password);
  	User user = userRepository.findFirstByEmailAndPassword(email, password);

      if (user != null) {
    	  
    	  
          // Check for white IP
//          if (user.getWhiteIP() != null && !user.getWhiteIP().isEmpty()) {
//              String ip = IPUtil.getIpAdrress(request); // Corrected method name
//              if (!user.getWhiteIP().contains(ip)) {
//                  return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                          .body("Your IP address " + ip + " is not in the white list. Please contact the administrator.");
//              }
//          }

          // Generate JWT Token
          Map<String, Object> tokenMap = new HashMap<>();
          tokenMap.put("loginId", user.getId());
          tokenMap.put("username", user.getUserName());
          tokenMap.put("email",user.getEmail());
          
//          tokenMap.put("role", user.getRole());

//          String token = JWTUtil.generateToken(tokenMap, 60 * 60 * 3); // 3 hours token expiry
          String token = JWTUtil.sign(tokenMap,60l*1000l*60l*3l);
          // Set token and loginId in cookies
          response.addCookie(JWTUtil.createCookie("loginId", user.getId().toString(), 60 * 60 * 3));
          response.addCookie(JWTUtil.createCookie("token", token, 60 * 60 * 3));

          // Return token and user details to the frontend
          Map<String, String> result = new HashMap<>();
          result.put("token", token);
          result.put("username", user.getUserName());
          result.put("email", user.getEmail());
//          result.put("role", user.getRole().getTitle());

          return ResponseEntity.ok(result);  // Send JWT and user info as JSON
      } else {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
      }
  }

}
