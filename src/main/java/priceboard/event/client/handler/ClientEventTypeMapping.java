package priceboard.event.client.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import priceboard.event.EventHandler;

public class ClientEventTypeMapping {

	private static final Logger log = LoggerFactory.getLogger(ClientEventTypeMapping.class);
	
	private Map<String, EventHandler> map = new HashMap<String, EventHandler>();

	public EventHandler getHandlerByType(String type) {
		EventHandler handler = map.get(type);
		if (handler == null) {
			log.error("No EventHandler for type: " + type);
			return ((EventHandler) -> {});
		}
		return handler;
	}
	
	public void registerHandler(EventHandler handler, String type) {
		EventHandler handlerInMap = map.get(type);
		if (handlerInMap == null) {
			map.put(type, handler);
		} else {
			log.error(handler + " with type " + type + " is existed");
		}
	}

}
