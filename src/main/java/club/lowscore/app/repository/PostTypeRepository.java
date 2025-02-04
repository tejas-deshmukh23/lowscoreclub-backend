package club.lowscore.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import club.lowscore.app.entity.PostType;

public interface PostTypeRepository extends JpaRepository<PostType, Long>{
	
	

}
