package club.lowscore.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import club.lowscore.app.dto.CommentDto;
import club.lowscore.app.entity.Comment;
import club.lowscore.app.entity.CommentCount;
import club.lowscore.app.entity.Post;
import club.lowscore.app.entity.User;
import club.lowscore.app.exception.PostNotFoundException;
import club.lowscore.app.exception.UserNotFoundException;
import club.lowscore.app.repository.CommentCountRepository;
import club.lowscore.app.repository.CommentRepository;
import club.lowscore.app.repository.PostRepository;
import club.lowscore.app.repository.UserRepository;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentCountRepository commentCountRepository;

	public Boolean addComment(String userId, String postId, String commentDescription) {
		
		Optional<User> user =  userRepository.findById(Long.parseLong(userId));
		Optional<Post> post;
		if(user.isPresent()) {
			
			post = postRepository.findById(Long.parseLong(postId));
			if(post.isPresent()) {
				
				Comment comment = new Comment();
				comment.setUser(user.get());
				comment.setPost(post.get());
				comment.setCommentDescription(commentDescription);
				
				commentRepository.save(comment);
				
				Optional<CommentCount> commentCount = commentCountRepository.findByPostId(Long.parseLong(postId));
				if(commentCount.isPresent()) {
					Long newCommentCount;
					try {
						newCommentCount = commentCount.get().getCommentCount()+1;
					}catch(NullPointerException e) {
						newCommentCount=(long)1;
					}
					
					commentCount.get().setCommentCount(newCommentCount);
					
					commentCountRepository.save(commentCount.get());
					
				}else {
					CommentCount commentCount2 = new CommentCount();
					commentCount2.setPost(post.get());
					commentCount2.setCommentCount((long)1);
					
					commentCountRepository.save(commentCount2);
				}
				
				
			}else {
				throw new PostNotFoundException("post with id :: "+postId+" not found");
			}
			
		}else {
			throw new UserNotFoundException("user with id :: "+userId+" not found");
		}
		
		return true;
		
	}

	public List<CommentDto> getComments(String userId, String postId) {
		
		Optional<User> user =  userRepository.findById(Long.parseLong(userId));
		Optional<Post> post;
		
		if(user.isPresent()) {
			
			post = postRepository.findById(Long.parseLong(postId));
			if(post.isPresent()) {
				
				PageRequest pageRequest = PageRequest.of(0, 200); // Page 0 (first Page) with the limit of 200
				List<Comment> comments = commentRepository.findByUserIdAndPostId(Long.parseLong(userId), Long.parseLong(postId), pageRequest);
				
				//Instead of using this we could have created a query to bring only the required columns
				
				List<CommentDto> commentDtos = new ArrayList<>() ;
				
				for(Comment comment : comments) {
					commentDtos.add(new CommentDto(comment.getCreateTime(), comment.getUpdateTime(), comment.getCommentDescription(), comment.getPost().getId(), comment.getUser().getId(), comment.getUser().getUserName() ));
				}
				
				return commentDtos;
				
			}else {
				throw new PostNotFoundException("Post with id :: "+postId+" not found");
			}
			
		}else {
			throw new UserNotFoundException("user with id :: "+userId+" not found");
		}
		
	}

}
