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
		
		List<EventHandler> returnHandlers = new EventHandlerFilter().filter(handlers, Arrays.asList("STOCK", "STOCK_PUSH"));
		Assert.assertEquals(1, returnHandlers.size());
		Assert.assertEquals(handler, returnHandlers.get(0));
	}
	
	
	//@Test
	public void testReadAnnotationInHandlers() throws NoSuchMethodException, SecurityException {
		List<EventHandler> handlers = new ArrayList<EventHandler>() {{
			add(new StockUpdateRoomHandler(null, null));
			add(new StockMemoryHandler(null,null));
			add(new StockRegisterHandler(null,null));
		}};
		
		List<EventHandler> returnHandlers = new EventHandlerFilter().filter(handlers, Arrays.asList("STOCK", "STOCK_PUSH"));
		System.out.println(returnHandlers.get(0).getClass());
		System.out.println(returnHandlers.get(1).getClass());
		System.out.println(returnHandlers.get(2).getClass());
		
		Assert.assertEquals(3, returnHandlers.size());
		
		Assert.assertTrue(returnHandlers.get(0).getClass() == StockMemoryHandler.class );
		Assert.assertTrue(returnHandlers.get(1).getClass() == StockMemoryHandler.class);
		Assert.assertTrue(returnHandlers.get(2).getClass() == StockUpdateRoomHandler.class);
	}
	
}
