package priceboard.data.queueservice;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.AmqpAdmin;

import priceboard.event.server.handler.EventHandlerFilter;

public class PutThroughMessageRabbitConfigurationListenerTest {
	@Test
	public void test(){
		PutThroughMessageRabbitConfigurationListener putThroughListener = new PutThroughMessageRabbitConfigurationListener("a", "b");
		AmqpAdmin amqpAdmin = new Mockito().mock(AmqpAdmin.class);
		EventHandlerFilter eventHandlerFilter =  new Mockito().mock(EventHandlerFilter.class);
		putThroughListener.setAmqpAdmin(amqpAdmin);
		putThroughListener.setEventHandlerFilter(eventHandlerFilter);
		putThroughListener.init();
	}
}
