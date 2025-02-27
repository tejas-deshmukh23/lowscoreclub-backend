package club.lowscore.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import club.lowscore.app.entity.Post;
import club.lowscore.app.entity.PostTag;
import club.lowscore.app.entity.Tag;

public interface PostTagRepository extends JpaRepository<PostTag, Long>{
	
    // Method to find all PostTag by Tag
    List<PostTag> findByTagId(Tag tagId);

}
