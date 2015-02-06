package priceboard.data.queueservice;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.AmqpAdmin;

import priceboard.event.server.handler.EventHandlerFilter;

public class StockMessageRabbitConfigurationListenerTest {
	@Test
	public void test(){
		StockMessageRabbitConfigurationListener stockListener = new StockMessageRabbitConfigurationListener("a", "b");
		AmqpAdmin amqpAdmin = new Mockito().mock(AmqpAdmin.class);
		EventHandlerFilter eventHandlerFilter =  new Mockito().mock(EventHandlerFilter.class);
		stockListener.setAmqpAdmin(amqpAdmin);
		stockListener.setEventHandlerFilter(eventHandlerFilter);
		stockListener.init();
	}
}
