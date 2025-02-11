package club.lowscore.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import club.lowscore.app.entity.Post;
import club.lowscore.app.exception.PostTypeNotFoundException;
import club.lowscore.app.exception.TagNotFoundException;
import club.lowscore.app.exception.UserNotFoundException;
import club.lowscore.app.service.PostService;
import club.lowscore.app.util.GlobalExceptionHandler;

@RestController
@CrossOrigin("*")
public class PostController {
	
	@Autowired
	PostService postService;
	
	@PostMapping("/addpost")
	public ResponseEntity<?> addPost(@RequestParam(required=false) String description,@RequestParam(required=false) String tagId,@RequestParam(required=false) String userId, @RequestParam(required=false) String postTypeId, @RequestParam(required=false) String parentQuestionId){
		
		try {
			postService.addpost(description, tagId, userId, postTypeId, parentQuestionId);
			return ResponseEntity.ok("Post successfully created");
		}catch (PostTypeNotFoundException | TagNotFoundException | UserNotFoundException e) {
            // Don't handle exceptions here, the GlobalExceptionHandler will take care of it
            throw e; // Just re-throw the exception
        } catch (Exception e) {
            // For unexpected exceptions
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage(), e);
        }
	}
	
	@GetMapping("/getAllQuestions")
	public ResponseEntity<List<Post>> getAllQuestions(@RequestParam(required=false) String page, @RequestParam(required=false) String limit){
		
		try {
			List<Post> posts = postService.getAllQuestions(page, limit);
			return ResponseEntity.ok(posts);
		}catch (NumberFormatException e) {
	        // Handle invalid number format for page and limit
	        return ResponseEntity.badRequest().body(null); // You can return a custom error message
	    } catch (Exception e) {
	        // Handle unexpected errors
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(null); // You can return a custom error message here
	    }
	}
	
	@PostMapping("/getResponsesOfQuestion")
	public ResponseEntity

}
