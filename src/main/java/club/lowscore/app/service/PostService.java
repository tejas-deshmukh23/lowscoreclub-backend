package club.lowscore.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import club.lowscore.app.entity.Post;
import club.lowscore.app.entity.PostTag;
import club.lowscore.app.entity.PostType;
import club.lowscore.app.entity.Tag;
import club.lowscore.app.entity.User;
import club.lowscore.app.exception.PostTypeNotFoundException;
import club.lowscore.app.exception.TagNotFoundException;
import club.lowscore.app.exception.UserNotFoundException;
import club.lowscore.app.repository.PostRepository;
import club.lowscore.app.repository.PostTagRepository;
import club.lowscore.app.repository.PostTypeRepository;
import club.lowscore.app.repository.TagRepository;
import club.lowscore.app.repository.UserRepository;
import club.lowscore.app.util.StringUtil;

@Service
public class PostService {
	
	@Autowired
	PostRepository postRepository;
	
	@Autowired
	PostTypeRepository postTypeRepository;
	
	@Autowired
	TagRepository tagRepository;
	
	@Autowired
	PostTagRepository postTagRepository;
	
	@Autowired
	UserRepository userRepository;

	public void addpost(String description, String tagId, String userId, String postTypeId, String parentQuestionId) {
		
		Post post = new Post();
		post.setPostDetails(description);
		//here we will fetch the postType from postType object using postTypeId key
		Optional<PostType> postType = postTypeRepository.findById(Long.parseLong(postTypeId));
		if(postType.isPresent()) {
			post.setPostType(postType.get());
		}else {
			//Here we will need to handle the case where the postType will be null we can also handle this by creating an custom exception and throwing it to api
			throw new PostTypeNotFoundException("Post Type with id : "+postTypeId+" not found");
		}
		
		Optional<User> user = userRepository.findById(Long.parseLong(userId));
		if(user.isPresent()) {
			post.setUser(user.get());
		}else {
			throw new UserNotFoundException("User with id : "+userId+" not found");
		}
		
		postRepository.save(post);
		
		// Convert the JSON string to a List<Integer> using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> tagIds = new ArrayList<>();
        try {
			tagIds = objectMapper.readValue(tagId, new TypeReference<List<Long>>(){});
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Here we will set the postId and tagId in the postTag table
        
       for(Long tagId2 : tagIds) {
    	   PostTag postTag = new PostTag();
    	   Optional<Tag> tag = tagRepository.findById(tagId2);
   		if(tag.isPresent()) {
   			postTag.setTagId(tag.get());
   		}else {
   			//Here we will create a customexception and throw it to the api
   			throw new TagNotFoundException("Tag with id : "+tagId+" not found");
   		}
   		postTag.setPostId(post);
		
		postTagRepository.save(postTag);
       }
       
		
		
	}

	public List<Post> getAllQuestions(String page,String limit) {
		
		Pageable pageable = null;
		
		if(StringUtil.notEmpty(limit) && StringUtil.notEmpty(page)) {
			pageable =PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit));
//			Page<Post> postPage = postRepository.findAll(pageable);//The problem with this query is that it doesn't brings the tags from another tags table associated with that post
			Page<Post> postPage = postRepository.findAllWithTags(pageable); //this will bring the tags assciated with that particular post by using the join query
			return postPage.getContent();
		}
		
		pageable = PageRequest.of(0, 10);
		Page<Post> postPage = postRepository.findAllWithTags(pageable);
		return postPage.getContent();
	}
	
//	public List<Post> getAllQuestions(String page, String limit) {
//        Pageable pageable;
//        
//        if (StringUtil.notEmpty(limit) && StringUtil.notEmpty(page)) {
//            pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit));
//        } else {
//            pageable = PageRequest.of(0, 10);
//        }
//        
//        // First get the page of IDs
//        Page<Long> postIds = postRepository.findAllPostIds(pageable);
//        
//        // Then fetch the complete posts with their tags
//        return postRepository.findAllWithTagsByIds(postIds.getContent());
//    }

}