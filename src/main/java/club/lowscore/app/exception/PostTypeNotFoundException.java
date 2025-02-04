package club.lowscore.app.exception;

public class PostTypeNotFoundException extends RuntimeException{ //We can extend by Exception if checked
	
	public PostTypeNotFoundException(String message)
	{
		super(message);
	}
	
//	In this exaample the throwable cause is used that is :: 
	//In this example, if SomeOtherException occurs, a PostTypeNotFoundException is thrown, but it also carries with it the original SomeOtherException (the cause). This way, when you catch or log the PostTypeNotFoundException, you can also examine the underlying exception to understand what actually went wrong.
//	try {
//	    // some code that causes an exception
//	} catch (SomeOtherException e) {
//	    throw new PostTypeNotFoundException("Failed to find post type", e);
//	}

	public PostTypeNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
