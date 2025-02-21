package club.lowscore.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.lowscore.app.dto.CommentDto;
import club.lowscore.app.entity.Comment;
import club.lowscore.app.exception.PostNotFoundException;
import club.lowscore.app.exception.UserNotFoundException;
import club.lowscore.app.exception.VoteCountNotFoundException;
import club.lowscore.app.exception.VoteTypeNotFoundException;
import club.lowscore.app.service.CommentService;

@RestController
public class CommentController {
	
	@Autowired
	CommentService commentService;
	
	@PostMapping("/comment")
	@CrossOrigin("*")
	public ResponseEntity<?> addComment(@RequestParam(name="userId") String userId, @RequestParam(name="postId") String postId, @RequestParam(name="comment") String commentDescription)
	{
		try {
			
			Boolean result = commentService.addComment(userId, postId, commentDescription);
			
			return ResponseEntity.ok(result);
		}
		catch(UserNotFoundException | PostNotFoundException e ) {
			throw e;
		}
		catch(Exception e) {
			throw e;
		}
	}
	
	@PostMapping("/fetchComments")
	@CrossOrigin("*")
	public ResponseEntity<?> getComments(@RequestParam(name="userId") String userId, @RequestParam(name="postId") String postId)
	{
		try {
			
			List<CommentDto> commentDtos = commentService.getComments(userId, postId);
			return ResponseEntity.ok(commentDtos);
			
		}catch(PostNotFoundException | UserNotFoundException e ) {
			throw e;
		}
		catch(Exception e) {
			throw e;
		}
	}
	

}
