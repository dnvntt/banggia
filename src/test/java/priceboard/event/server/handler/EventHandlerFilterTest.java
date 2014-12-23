package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import priceboard.event.EventHandler;
import priceboard.event.client.handler.StockRegisterHandler;

public class EventHandlerFilterTest {
	
	@Test
	public void testReadAnnotationInStockHandler() throws NoSuchMethodException, SecurityException {
		StockUpdateRoomHandler handler = new StockUpdateRoomHandler(null, null);
		List<EventHandler> handlers = new ArrayList<EventHandler>() {{
			add(handler);
		}};
		
		List<EventHandler> returnHandlers = new EventHandlerFilter().filter(handlers, Arrays.asList("STOCK", "ALL"));
		Assert.assertEquals(1, returnHandlers.size());
		Assert.assertEquals(handler, returnHandlers.get(0));
	}
	
	
	@Test
	public void testReadAnnotationInHandlers() throws NoSuchMethodException, SecurityException {
		List<EventHandler> handlers = new ArrayList<EventHandler>() {{
			add(new StockUpdateRoomHandler(null, null));
			add(new MemoryHandler(null));
			add(new CompressionHandler(null, null));
			add(new StockRegisterHandler(null, null, null));
		}};
		
		List<EventHandler> returnHandlers = new EventHandlerFilter().filter(handlers, Arrays.asList("STOCK", "ALL"));
		
		Assert.assertEquals(3, returnHandlers.size());
		
		Assert.assertTrue(returnHandlers.get(0).getClass() == MemoryHandler.class || returnHandlers.get(0).getClass() == CompressionHandler.class);
		Assert.assertTrue(returnHandlers.get(1).getClass() == MemoryHandler.class || returnHandlers.get(1).getClass() == CompressionHandler.class);
		Assert.assertTrue(returnHandlers.get(2).getClass() == StockUpdateRoomHandler.class);
	}
	
}
