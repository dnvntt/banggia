package priceboard.websocket.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import priceboard.client.ClientConnection;
import priceboard.client.WebSocketClientConnection;
import priceboard.event.EventHandler;
import priceboard.event.client.handler.ClientEventTypeMapping;
import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class StockWebSocketHandler extends TextWebSocketHandler {

	private JsonParser parser;
	
	private final InMemory memory;

	private ClientEventTypeMapping clientEventTypeMapping;
	
	@Autowired
	public StockWebSocketHandler(ClientEventTypeMapping clientEventTypeMapping, InMemory memory, JsonParser parser) {
		this.clientEventTypeMapping = clientEventTypeMapping;
		this.memory = memory;
		this.parser = parser;
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		JsonNode node = parser.parse(message.getPayload());
		if (node == null) return;
		String type = parser.parseType(node);
		EventHandler handler = clientEventTypeMapping.getHandlerByType(type);
		handleMessage(handler, session.getId(), node);
	}
	
	private void handleMessage(EventHandler handler, String sessionId, JsonNode node) {
		Map<String, Object> map = new HashMap<String, Object>();
		ClientConnection clientConnection = getClientConnectionBySessionId(sessionId);
		map.put("CLIENT", clientConnection);
		map.put("data", node);
		handler.handle(map);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String id = session.getId();
		memory.put("CLIENT_CONNECTION", id, new WebSocketClientConnection(session));
		super.afterConnectionEstablished(session);
	}
	
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		ClientConnection clientConnection = getClientConnectionBySessionId(session.getId());
		EventHandler handler = clientEventTypeMapping.getHandlerByType("DISCONNECT");
		handler.handle(clientConnection);
		super.afterConnectionClosed(session, status);
	}
	
	private ClientConnection getClientConnectionBySessionId(String sessionId) {
		return (ClientConnection) memory.get("CLIENT_CONNECTION", sessionId);
	}
	
	

}
