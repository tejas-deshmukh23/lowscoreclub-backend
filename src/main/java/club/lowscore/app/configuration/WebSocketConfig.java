//package club.lowscore.app.configuration;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//import club.lowscore.app.service.PostNotificationWebSocketHandler;
//
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(new PostNotificationWebSocketHandler(), "/ws/notifications")
//                .setAllowedOrigins("*");
//    }
//}

package club.lowscore.app.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import club.lowscore.app.service.PostNotificationWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final PostNotificationWebSocketHandler postNotificationWebSocketHandler;

    @Autowired
    public WebSocketConfig(PostNotificationWebSocketHandler postNotificationWebSocketHandler) {
        this.postNotificationWebSocketHandler = postNotificationWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Use the injected (singleton) PostNotificationWebSocketHandler instance
        registry.addHandler(postNotificationWebSocketHandler, "/ws/notifications")
                .setAllowedOrigins("*");
    }
}

