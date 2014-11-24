package priceboard.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketClientConnection extends ClientConnection {

	private static final Logger log = LoggerFactory.getLogger(WebSocketClientConnection.class);
	
	private final WebSocketSession session;
	
	public WebSocketClientConnection(WebSocketSession session) {
		this.session = session;
	}
	
	@Override
	public void send(String data) {
		try {
			session.sendMessage(new TextMessage(data));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

}
