package club.lowscore.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import club.lowscore.app.entity.CommentCount;

public interface CommentCountRepository extends JpaRepository<CommentCount, Long> {
	
	public Optional<CommentCount> findByPostId(Long postId);

}
