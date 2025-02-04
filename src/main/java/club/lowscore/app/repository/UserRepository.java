package club.lowscore.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import club.lowscore.app.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByUserName(String username);
	boolean existsByEmail(String email);
    boolean existsByUserName(String userName);
	public User findFirstByEmailAndPassword(String email, String password);

}
