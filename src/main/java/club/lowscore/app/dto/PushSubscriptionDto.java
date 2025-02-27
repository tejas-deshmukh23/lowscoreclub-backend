package club.lowscore.app.dto;

public class PushSubscriptionDto {
	
	private String endpoint;
    private Long expirationTime;  // Optional, can be null
    private SubscriptionKeysDto keys;
    private String browserInfo;
    private String deviceType;

    // Default constructor
    public PushSubscriptionDto() {}

    // Constructor with parameters
    public PushSubscriptionDto(String endpoint, Long expirationTime, SubscriptionKeysDto keys) {
        this.endpoint = endpoint;
        this.expirationTime = expirationTime;
        this.keys = keys;
    }

    // Getters and Setters
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public SubscriptionKeysDto getKeys() {
        return keys;
    }

    public void setKeys(SubscriptionKeysDto keys) {
        this.keys = keys;
    }

	public String getBrowserInfo() {
		return browserInfo;
	}

	public void setBrowserInfo(String browserInfo) {
		this.browserInfo = browserInfo;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

}
