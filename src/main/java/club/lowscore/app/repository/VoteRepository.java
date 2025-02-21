package club.lowscore.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import club.lowscore.app.entity.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>{
	
	@Query("select v from Vote v where v.post.id= :postId and v.user.id= :userId")
	public Optional<Vote> findByPostIdAndUserId(Long postId, Long userId);

}
