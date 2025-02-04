package club.lowscore.app.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import club.lowscore.app.entity.User;
import club.lowscore.app.repository.UserRepository;
import club.lowscore.app.util.StringUtil;

@Service
public class SignupService {
	
	@Autowired
	UserRepository userRepository;
	
	public Boolean signup(String email, String userName, String password, String dob, String pincode)
	{
		
		if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email address already in use");
        }
        
        if (userRepository.existsByUserName(userName)) {
            throw new DataIntegrityViolationException("Username is already taken");
        }
		User user = new User();
				user.setUserName(userName);
			
			if(StringUtil.notEmpty(pincode)) {
				user.setPincode(pincode);
			}
			user.setEmail(email);
			user.setPassword(password);
			user.setDob(LocalDate.parse(dob));
			
			userRepository.save(user);
			
			return true;
//			
//		}catch(DataIntegrityViolationException exception) {
//			return 
//		}
//		catch(Exception e)
//		{
//			
//			e.printStackTrace();
//		}
		
//		return false;
	}

}
