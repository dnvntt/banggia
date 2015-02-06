package priceboard.data.queueservice;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.AmqpAdmin;

import priceboard.event.server.handler.EventHandlerFilter;

public class MarketStatisticMessageRabbitConfigurationListenerTest {
	@Test
	public void test(){
		MarketStatisticMessageRabbitConfigurationListener marketStatisticListener = new MarketStatisticMessageRabbitConfigurationListener("a", "b");
		AmqpAdmin amqpAdmin = new Mockito().mock(AmqpAdmin.class);
		EventHandlerFilter eventHandlerFilter =  new Mockito().mock(EventHandlerFilter.class);
		marketStatisticListener.setAmqpAdmin(amqpAdmin);
		marketStatisticListener.setEventHandlerFilter(eventHandlerFilter);
		marketStatisticListener.init();
	}
}
