package priceboard.websocket.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import priceboard.client.ClientConnection;
import priceboard.client.WebSocketClientConnection;
import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerFilter;
import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class StockWebSocketHandler extends TextWebSocketHandler {

	//private static final Logger logger = Logger.getLogger(StockWebSocketHandler.class);	
	private JsonParser parser;
	
	private final InMemory memory;

	private EventHandlerFilter eventHandlerFilter;

	private List<EventHandler> handlers;
	
	private Map<String, List<EventHandler>> clientEventTypeMapping = new HashMap<String, List<EventHandler>>();
	
	@Autowired
	public StockWebSocketHandler(InMemory memory, JsonParser parser, EventHandlerFilter eventHandlerFilter, List<EventHandler> handlers) {
		this.memory = memory;
		this.parser = parser;
		this.eventHandlerFilter = eventHandlerFilter;
		this.handlers = handlers;
	}

	@PostConstruct
	public void init() {
		List<EventHandler> handlersOfRegistConsumer = eventHandlerFilter.filter(handlers, Arrays.asList("registConsumer"));
		clientEventTypeMapping.put("registConsumer", handlersOfRegistConsumer);
		
		List<EventHandler> handlersOfDisconnect = eventHandlerFilter.filter(handlers, Arrays.asList("DISCONNECT"));
		clientEventTypeMapping.put("DISCONNECT", handlersOfDisconnect);
		
		//List<EventHandler> handlersOfPost = eventHandlerFilter.filter(handlers, Arrays.asList("post"));
		//clientEventTypeMapping.put("post", handlersOfPost);
		
		//List<EventHandler> handlersOfResumne = eventHandlerFilter.filter(handlers, Arrays.asList("resumne"));
		//clientEventTypeMapping.put("resumne", handlersOfResumne);
		
		//List<EventHandler> handlersOfRequestFullData = eventHandlerFilter.filter(handlers, Arrays.asList("requestFullData"));
		//clientEventTypeMapping.put("requestFullData", handlersOfRequestFullData);
		
		//List<EventHandler> handlersOfStopConsume = eventHandlerFilter.filter(handlers, Arrays.asList("stopConsume"));
		//clientEventTypeMapping.put("stopConsume", handlersOfStopConsume);
	}
	
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			JsonNode node = parser.parse(message.getPayload());
			if (node == null) return;
			String type = parser.parseType(node);
			List<EventHandler> handlers = clientEventTypeMapping.get(type);
			if (handlers == null) {
				//logger.info("No handler for type: "+  type + " - Origin message: " + message + " from client: " + session.getRemoteAddress());
				return;
			}
			handleMessage(handlers, session.getId(), node);
		
	}
	
	private void handleMessage(List<EventHandler> handlers, String sessionId, JsonNode node) {
		Map<String, Object> map = new HashMap<String, Object>();
		ClientConnection clientConnection = getClientConnectionBySessionId(sessionId);
		map.put("CLIENT", clientConnection);
		map.put("data", node);
		handlers.forEach((handler) -> {
			handler.handle(map);
		});
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
		List<EventHandler> handlers = clientEventTypeMapping.get("DISCONNECT");
		handlers.forEach((handler) -> {
			handler.handle(clientConnection);
		});
		super.afterConnectionClosed(session, status);
	}
	
	private ClientConnection getClientConnectionBySessionId(String sessionId) {
		return (ClientConnection) memory.get("CLIENT_CONNECTION", sessionId);
	}
	
}
