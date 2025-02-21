package club.lowscore.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import club.lowscore.app.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	public Optional<Comment> findByUserIdAndPostId(Long userId, Long postId);
	
	@Query("select c from Comment c where c.user.id = :userId and c.post.id = :postId")
	public List<Comment> findByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId, Pageable pageable);
	
}
