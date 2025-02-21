package club.lowscore.app.model;

import lombok.Data;

//@Data  //this is a lombok @data which is used to automatically generate getter and setter methods, toString(), equals(), hashCode(), and a constructor for the class. 
public class Subscription {
	
	private String endpoint;
    private Keys keys;
    
    
    
    public String getEndpoint() {
		return endpoint;
	}



	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}



	public Keys getKeys() {
		return keys;
	}



	public void setKeys(Keys keys) {
		this.keys = keys;
	}



//	@Data
    public static class Keys {
        private String p256dh;
        private String auth;
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

}
