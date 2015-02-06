package priceboard.websocket.handler;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerFilter;
import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

public class StockWebSocketHandlerTest {
	private InMemory memory;
	JsonParser parser;
	StockWebSocketHandler stockWebsockethandler;
	private EventHandlerFilter eventHandlerFilter;
	private List<EventHandler> handlers;
	@Test
	public void test() {
		parser = new JsonParser();
		memory = new InMemory();
		eventHandlerFilter = new EventHandlerFilter();
		handlers = new  ArrayList<EventHandler>();
		stockWebsockethandler = new StockWebSocketHandler(memory,parser,eventHandlerFilter,handlers);
		stockWebsockethandler.init();
	}
}
