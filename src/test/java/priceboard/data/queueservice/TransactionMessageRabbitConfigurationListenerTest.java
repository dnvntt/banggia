package priceboard.data.queueservice;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.AmqpAdmin;

import priceboard.event.server.handler.EventHandlerFilter;

public class TransactionMessageRabbitConfigurationListenerTest {
	@Test
	public void test(){
		TransactionMessageRabbitConfigurationListener transactionListener = new TransactionMessageRabbitConfigurationListener("a", "b");
		AmqpAdmin amqpAdmin = new Mockito().mock(AmqpAdmin.class);
		EventHandlerFilter eventHandlerFilter =  new Mockito().mock(EventHandlerFilter.class);
		transactionListener.setAmqpAdmin(amqpAdmin);
		transactionListener.setEventHandlerFilter(eventHandlerFilter);
		transactionListener.init();
	}
}
