package club.lowscore.app.exception;

public class VoteCountNotFoundException extends RuntimeException {
	
	public VoteCountNotFoundException(String message)
	{
		super(message);
	}
	
	public VoteCountNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
