package club.lowscore.app.exception;

public class PushSubscriptionNotFoundException extends RuntimeException {
	
	public PushSubscriptionNotFoundException(String message) {
		super(message);
	}
	
	public PushSubscriptionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
