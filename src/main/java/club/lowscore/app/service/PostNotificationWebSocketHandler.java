package club.lowscore.app.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import jakarta.annotation.PostConstruct;

@Component
public class PostNotificationWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();
	 // Use CopyOnWriteArrayList for thread safety
//    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    
    @PostConstruct
    public void init() {
        System.out.println("PostNotificationWebSocketHandler instance: " + this);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);  // Keep track of active connections
        System.out.println("New WebSocket connection established: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming messages (optional)
    }
    
    @Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// TODO Auto-generated method stub
		super.afterConnectionClosed(session, status);
		System.out.println("The after connection closed");
	}

	public void sendNotificationToAllUsers(String notification) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(notification));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
