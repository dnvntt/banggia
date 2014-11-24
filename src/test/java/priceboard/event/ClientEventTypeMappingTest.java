package priceboard.event;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.event.client.handler.ClientDisconnectHandler;
import priceboard.event.client.handler.ClientEventTypeMapping;
import priceboard.event.client.handler.StockRegisterHandler;

public class ClientEventTypeMappingTest {
	
	private ClientEventTypeMapping clientEventTypeMapping;
	
	@Before
	public void setUp() {
		clientEventTypeMapping = new ClientEventTypeMapping();
		clientEventTypeMapping.registerHandler(new StockRegisterHandler(null, null, null, null), "registConsumer");
		clientEventTypeMapping.registerHandler(new ClientDisconnectHandler(null, null), "DISCONNECT");
	}
	
	@Test
	public void testClientEventMappingWithStockRegister() {
		EventHandler stockRegister = clientEventTypeMapping.getHandlerByType("registConsumer");
		Assert.assertNotNull("stockRegister is not null", stockRegister);
		Assert.assertTrue("This is not stockRegister", StockRegisterHandler.class.isInstance(stockRegister));
	}
	
	@Test
	public void testClientEventMappingWithDisConnectHandler() {
		EventHandler disconEventHandler = clientEventTypeMapping.getHandlerByType("DISCONNECT");
		Assert.assertNotNull("disconEventHandler is not null", disconEventHandler);
		Assert.assertTrue("This is not disconEventHandler", ClientDisconnectHandler.class.isInstance(disconEventHandler));
	}
	
	@Test
	public void testClientEventMappingWithHandlerNotExist() {
		EventHandler eventHandler = clientEventTypeMapping.getHandlerByType("NotExistedType");
		Assert.assertNotNull("eventHander is not null", eventHandler);
		Assert.assertTrue("This is not disconEventHandler", EventHandler.class.isInstance(eventHandler));
	}
	
}
