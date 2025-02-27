package club.lowscore.app.dto;

public class NotificationResult {
	
	private int successCount;
    private int failureCount;
    
    public NotificationResult() {
    	
    }

	public NotificationResult(int successCount, int failureCount) {
		super();
		this.successCount = successCount;
		this.failureCount = failureCount;
	}



	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(int failureCount) {
		this.failureCount = failureCount;
	}
    
}
