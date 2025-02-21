package club.lowscore.app.exception;

public class VoteTypeNotFoundException extends RuntimeException {
	
	public VoteTypeNotFoundException(String message)
	{
		super(message);
	}
	
	public VoteTypeNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
