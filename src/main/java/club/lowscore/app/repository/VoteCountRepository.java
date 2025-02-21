package club.lowscore.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import club.lowscore.app.entity.VoteCount;

public interface VoteCountRepository extends JpaRepository<VoteCount, Long> {
	
	@Query("select vc from VoteCount vc WHERE vc.post.id = :postId")
	public Optional<VoteCount> findByPostId(Long postId);

}
