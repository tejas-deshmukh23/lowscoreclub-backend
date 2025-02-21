package club.lowscore.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import club.lowscore.app.exception.PostNotFoundException;
import club.lowscore.app.exception.UserNotFoundException;
import club.lowscore.app.exception.VoteCountNotFoundException;
import club.lowscore.app.exception.VoteTypeNotFoundException;
import club.lowscore.app.service.VoteService;

@RestController
public class VoteController {
	
	@Autowired
	VoteService voteService;
	
//	Remaining task --> to handle the exception if userUd not present
	
	@PostMapping("/like")
	@CrossOrigin("*")
	public ResponseEntity<?> likePost(@RequestParam(name="userId") String userId, @RequestParam(name="postId") String postId){
		try {
			Integer result = voteService.likePost(userId, postId);
			if(result == 0) {
				return ResponseEntity.ok(0);
			}else if(result == 1){
				return ResponseEntity.ok(1);
			}else {
				return ResponseEntity.ok(2);
			}
			
		}
		catch(PostNotFoundException | UserNotFoundException | VoteCountNotFoundException | VoteTypeNotFoundException e ) {
			throw e;
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/dislike")
	@CrossOrigin("*")
	public ResponseEntity<?> dislikePost(@RequestParam(name="userId") String userId, @RequestParam(name="postId") String postId){
		try {
			
			Integer result = voteService.dislikePost(userId, postId);
			if(result == 0) {
				return ResponseEntity.ok(0);//disliked
				
			}else if(result == 1) {	
				return ResponseEntity.ok(1);//dislike removed
			}else {
				return ResponseEntity.ok(2);//changed like to dislike
			}
			
		}
		catch(PostNotFoundException | UserNotFoundException | VoteCountNotFoundException | VoteTypeNotFoundException e ) {
			throw e;
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/getLikeCount")
	@CrossOrigin("*")
	public ResponseEntity<?> getLikesCount(@RequestParam(name="postId") String postId)
	{
		try {
			
			Long likesCount = voteService.getLikesCount(postId);
			return ResponseEntity.ok(likesCount);
			
		}catch(VoteCountNotFoundException e) {
			throw e;
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/getDislikeCount")
	@CrossOrigin("*")
	public ResponseEntity<?> getDislikeCount(@RequestParam(name="postId") String postId)
	{
		try {
			
			Long dislikeCount = voteService.getDislikeCount(postId);
			return ResponseEntity.ok(dislikeCount);
			
		}catch(VoteCountNotFoundException e) {
			throw e;
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/isVoted")
	@CrossOrigin("*")
	public ResponseEntity<?> getIsVoted(@RequestParam(name="userId") String userId, @RequestParam(name="postId") String postId){
		try {
			Long result = voteService.getIsVoted(userId, postId);
			return ResponseEntity.ok(result);
		}catch(Exception e) {
			throw e;
		}
	}

}
