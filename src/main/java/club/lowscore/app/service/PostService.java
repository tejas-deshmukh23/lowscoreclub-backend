package club.lowscore.app.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import club.lowscore.app.dto.ResponsePost;
import club.lowscore.app.entity.Post;
import club.lowscore.app.entity.PostTag;
import club.lowscore.app.entity.PostType;
import club.lowscore.app.entity.Tag;
import club.lowscore.app.entity.User;
import club.lowscore.app.exception.PostNotFoundException;
import club.lowscore.app.exception.PostTypeNotFoundException;
import club.lowscore.app.exception.TagNotFoundException;
import club.lowscore.app.exception.UserNotFoundException;
import club.lowscore.app.repository.PostRepository;
import club.lowscore.app.repository.PostTagRepository;
import club.lowscore.app.repository.PostTypeRepository;
import club.lowscore.app.repository.TagRepository;
import club.lowscore.app.repository.UserRepository;
import club.lowscore.app.util.StringUtil;
import jakarta.transaction.Transactional;

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

	public Long addpost(String description, String tagId, String userId, String postTypeId, String parentQuestionId) {
		
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
		
		if(StringUtil.notEmpty(parentQuestionId)) {
			Optional<Post> parentPost = postRepository.findById(Long.parseLong(parentQuestionId));
			if(parentPost.isPresent()) {
				post.setParentQuestionId(parentPost.get());
			}else {
				throw new PostNotFoundException("Post with id : "+parentQuestionId+" not found");
			}
		}
		
		
		postRepository.save(post);
		
		// Convert the JSON string to a List<Integer> using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> tagIds = new ArrayList<>();
        if(StringUtil.notEmpty(tagId)) {
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
       
		return post.getId();
		
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

	public List<ResponsePost> getResponseOfQuestion(Post questionPost) {
		
		List<Post> responseOfQuestion =  postRepository.findAllByParentQuestionId(questionPost);
		
		List<ResponsePost> listResponsePost = new ArrayList<>();
		for(Post post : responseOfQuestion) {
			listResponsePost.add(new ResponsePost(post.getId(), post.getUser().getUserName(), post.getPostDetails(), post.getCreateTime(), post.getUser().getId()));
		}
		
		return listResponsePost;
	}

	@Transactional
	//From this function we are returning the users of the post which has the tags same as the post tags which we are passing in this function's argument
	public Set<Long> getUserOfPostWithTags(String postId) {
		
		Optional<Post> post = postRepository.findById(Long.parseLong(postId));
		
		if(post.isEmpty()) {
			throw new PostNotFoundException("Post with id : "+postId+" not found");
		}
		
		List<List<PostTag>> postTagList = new ArrayList<>();
		Set<Long> uniquePostIds = new HashSet<>();//we are using set because set doesn't stores the duplicate elements
		Set<Long> uniqueUserIds = new HashSet<>();
		
		// Lazy loading should work now, as the session is still open
	    Set<Tag> tags = post.get().getTags();  // This will now be initialized properly
	    
		for(Tag t : tags) {
			postTagList.add(postTagRepository.findByTagId(t));
		}
		
		for(List<PostTag> postTags : postTagList ) {
			for(PostTag postTag : postTags) {
//				uniquePostIds.add(postTag.getPostId().getId());
				//instead of taking that post ids we will get directly the userIds to whom we will be sending the notifications
				uniqueUserIds.add(postTag.getPostId().getUser().getId());
			}
		}
		
		return uniqueUserIds;
		
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
	
public Integer getResponsesOfQuestionCount(Post questionPost) {
		
		List<Post> responseOfQuestion =  postRepository.findAllByParentQuestionId(questionPost);
		
		
		
		Integer count = responseOfQuestion.size();
		
		return count;
	}

}