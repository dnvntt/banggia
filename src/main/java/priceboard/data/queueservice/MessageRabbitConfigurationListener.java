package priceboard.data.queueservice;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerFilter;

import com.eaio.uuid.UUID;

@Configuration
public abstract class MessageRabbitConfigurationListener {
	
	protected  String nameQueue;
	protected String nameFanoutExchange;

	@Autowired
	protected MessageConverter messageConverter;

	@Autowired
	protected ConnectionFactory amqpConnectionFactory;

	@Autowired
	protected EventHandlerFilter filter;

	@Autowired
	protected ApplicationContext applicationContext;

	@Autowired
	protected AmqpAdmin amqpAdmin;

	@Autowired
	protected EventHandlerFilter eventHandlerFilter;

	@Autowired
	protected List<EventHandler> handlers;

	protected List<EventHandler> handlersOfMessage;

	@PostConstruct
	abstract  public void init();


	@Bean
	public Queue nameQueue() {
		Queue marketQueue = declareNameQueue();
		bindingToExchange(marketQueue);
		return marketQueue;
	}

	private Queue declareNameQueue() {
		Queue queue = new Queue(nameQueue + new UUID(), false);
		amqpAdmin.declareQueue(queue);
		return queue;
	}
	
	private void bindingToExchange(Queue marketQueue) {
		BindingBuilder.bind(marketQueue).to(marketFanoutExchange());
	}
	
	private FanoutExchange marketFanoutExchange() {
		return new FanoutExchange(nameFanoutExchange, false, false);
	}


	@Bean
	public SimpleMessageListenerContainer marketListenerContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(amqpConnectionFactory);
		container.setQueues(nameQueue());
		container.setMessageListener(marketListenerAdapter());
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		return container;
	}

	@Bean
	public MessageListenerAdapter marketListenerAdapter() {
		return new MessageListenerAdapter(this, messageConverter);
	}

	public void handleMessage(Object object) {
		handlersOfMessage.forEach((handler) -> {
			handler.handle(object);
		});

	}
}
