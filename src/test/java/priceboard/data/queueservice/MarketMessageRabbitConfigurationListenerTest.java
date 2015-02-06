package priceboard.data.queueservice;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.AmqpAdmin;

import priceboard.event.server.handler.EventHandlerFilter;

public class MarketMessageRabbitConfigurationListenerTest {
	@Test
	public void test(){
		MarketMessageRabbitConfigurationListener marketListener = new MarketMessageRabbitConfigurationListener("a", "b");
		AmqpAdmin amqpAdmin = new Mockito().mock(AmqpAdmin.class);
		EventHandlerFilter eventHandlerFilter =  new Mockito().mock(EventHandlerFilter.class);
		marketListener.setAmqpAdmin(amqpAdmin);
		marketListener.setEventHandlerFilter(eventHandlerFilter);
		marketListener.init();
	}
}
