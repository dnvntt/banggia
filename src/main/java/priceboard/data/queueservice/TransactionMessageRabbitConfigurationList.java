package priceboard.data.queueservice;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TransactionMessageRabbitConfigurationList extends
		MessageRabbitConfigurationListener {
	
	
	@Autowired
	public TransactionMessageRabbitConfigurationList(
			@Value("${transaction_external_queue}") String queueName,
			@Value("${transaction_fanout_exchange}") String exchageName) {    
		super(queueName, exchageName);
	}

	@PostConstruct
	public void init() {
		super.init();
		setMessageHandler();
	}

	private void setMessageHandler() {
		this.handlersOfMessage = this.eventHandlerFilter.filter(handlers, Arrays.asList("TRANSACTION","CLEAR_DATA", "TRANSACTION_PUSH"));
	}
	
	@Bean
	public SimpleMessageListenerContainer stockListenerContainer() {
		return super.createListenerContainer();
	}

}
