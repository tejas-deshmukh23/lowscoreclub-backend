package club.lowscore.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import club.lowscore.app.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	
	public Page<Post> findAll(Pageable pageable);
	
//{ 
	
//	Without FETCH	
	
//	-- Initial query
//	SELECT * FROM posts p WHERE p.id = 1;
//
//	-- Subsequent lazy queries for each post's tags
//	SELECT t.* FROM tags t 
//	JOIN post_tags pt ON t.id = pt.tag_id 
//	WHERE pt.post_id = 1;
//
//	SELECT t.* FROM tags t 
//	JOIN post_tags pt ON t.id = pt.tag_id 
//	WHERE pt.post_id = 2;
//	-- ... and so on for each post
//	
	@Query("SELECT p FROM Post p LEFT JOIN FETCH p.tags") //here that fetch keyword is important to avoid that N+ queries
	public Page<Post> findAllWithTags(Pageable pageable);

	//With FETCH
	
//	-- Single query that gets everything at once
//	SELECT p.*, t.* 
//	FROM posts p
//	LEFT JOIN post_tags pt ON p.id = pt.post_id
//	LEFT JOIN tags t ON pt.tag_id = t.id; 
	
	//}
	
//	 // Query to get IDs for pagination
//    @Query("SELECT DISTINCT p.id FROM Post p")
//    Page<Long> findAllPostIds(Pageable pageable);
//    
//    // Query to fetch full posts with tags
//    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.tags WHERE p.id IN :ids")
//    List<Post> findAllWithTagsByIds(List<Long> ids);

}
