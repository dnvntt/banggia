package priceboard.client;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketClientConnection extends ClientConnection {
	private static final Logger logger = Logger.getLogger(WebSocketClientConnection.class);
	
	private final WebSocketSession session;
	
	public WebSocketClientConnection(WebSocketSession session) {
		this.session = session;
	}
	
	@Override
	public void send(String data) {
		try {
			if (session.isOpen()) {
				session.sendMessage(new TextMessage(data));
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

}
