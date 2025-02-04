package club.lowscore.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.lowscore.app.entity.PostTag;

public interface PostTagRepository extends JpaRepository<PostTag, Long>{
	
	

}
