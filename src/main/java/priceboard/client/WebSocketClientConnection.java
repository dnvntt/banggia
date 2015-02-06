package priceboard.client;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketClientConnection extends ClientConnection {
	
	private final WebSocketSession session;
	
	public WebSocketClientConnection(WebSocketSession session) {
		this.session = session;
	}
	
	@Override
	public synchronized void send(String data) {
		try {
				session.sendMessage(new TextMessage(data));
		} catch (Exception e) {
		}
	}

}
