package club.lowscore.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.lowscore.app.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
