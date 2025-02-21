package club.lowscore.app.configuration;

import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;
import java.security.Security;

@Configuration
public class WebPushConfig {
    @Value("${vapid.public.key}")
    private String publicKey;
    
    @Value("${vapid.private.key}")
    private String privateKey;

    @Bean
    public PushService pushService() {
        Security.addProvider(new BouncyCastleProvider());
        
        try {
			return new PushService(
			    publicKey,
			    privateKey,
			    "deshmukht100@gmail.com"  // Replace with your email
			);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
}