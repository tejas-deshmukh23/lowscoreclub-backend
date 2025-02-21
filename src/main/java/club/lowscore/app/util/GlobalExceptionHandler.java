package club.lowscore.app.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import club.lowscore.app.exception.PostNotFoundException;
import club.lowscore.app.exception.PostTypeNotFoundException;
import club.lowscore.app.exception.TagNotFoundException;
import club.lowscore.app.exception.UserNotFoundException;
import club.lowscore.app.exception.VoteCountNotFoundException;
import club.lowscore.app.exception.VoteTypeNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handling PostTypeNotFoundException
    @ExceptionHandler(PostTypeNotFoundException.class)
    public ResponseEntity<String> handlePostTypeNotFoundException(PostTypeNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // or INTERNAL_SERVER_ERROR based on your logic
    }

    // Handling TagNotFoundException
    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<String> handleTagNotFoundException(TagNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    //Handling UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex){
    	return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
 	// Handling general exceptions
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> handlePostNotFoundException(PostNotFoundException ex){
    	return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
 	// Handling general exceptions
    @ExceptionHandler(VoteCountNotFoundException.class)
    public ResponseEntity<String> handleVoteCountNotFoundException(VoteCountNotFoundException ex){
    	return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
 // Handling general exceptions
    @ExceptionHandler(VoteTypeNotFoundException.class)
    public ResponseEntity<String> handleVoteTypeNotFoundException(VoteTypeNotFoundException ex){
    	return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    // Handling general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
