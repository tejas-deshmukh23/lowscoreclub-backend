package club.lowscore.app.dto;

public class SubscriptionKeysDto {
	
	private String p256dh;
    private String auth;

    // Default constructor
    public SubscriptionKeysDto() {}

    // Constructor with parameters
    public SubscriptionKeysDto(String p256dh, String auth) {
        this.p256dh = p256dh;
        this.auth = auth;
    }

    // Getters and Setters
    public String getP256dh() {
        return p256dh;
    }

    public void setP256dh(String p256dh) {
        this.p256dh = p256dh;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

}
