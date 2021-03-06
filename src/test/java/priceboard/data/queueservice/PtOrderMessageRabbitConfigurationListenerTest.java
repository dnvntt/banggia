package priceboard.data.queueservice;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.AmqpAdmin;

import priceboard.event.server.handler.EventHandlerFilter;

public class PtOrderMessageRabbitConfigurationListenerTest {
	@Test
	public void test(){
		PtOrderMessageRabbitConfigurationListener ptOrderListener = new PtOrderMessageRabbitConfigurationListener("a", "b");
		AmqpAdmin amqpAdmin = new Mockito().mock(AmqpAdmin.class);
		EventHandlerFilter eventHandlerFilter =  new Mockito().mock(EventHandlerFilter.class);
		ptOrderListener.setAmqpAdmin(amqpAdmin);
		ptOrderListener.setEventHandlerFilter(eventHandlerFilter);
		ptOrderListener.init();
	}
}
