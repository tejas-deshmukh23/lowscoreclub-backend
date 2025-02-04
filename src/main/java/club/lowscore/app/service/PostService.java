package club.lowscore.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import club.lowscore.app.entity.Post;
import club.lowscore.app.entity.PostTag;
import club.lowscore.app.entity.PostType;
import club.lowscore.app.entity.Tag;
import club.lowscore.app.exception.PostTypeNotFoundException;
import club.lowscore.app.exception.TagNotFoundException;
import club.lowscore.app.repository.PostRepository;
import club.lowscore.app.repository.PostTagRepository;
import club.lowscore.app.repository.PostTypeRepository;
import club.lowscore.app.repository.TagRepository;

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
		
		postRepository.save(post);
		
		//Here we will set the postId and tagId in the postTag table
		
		PostTag postTag = new PostTag();
		Optional<Tag> tag = tagRepository.findById(Long.parseLong(tagId));
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