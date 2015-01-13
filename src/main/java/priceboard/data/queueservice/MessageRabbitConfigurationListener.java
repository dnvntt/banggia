package priceboard.data.queueservice;

import java.util.List;

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

import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerFilter;

import com.eaio.uuid.UUID;

public class MessageRabbitConfigurationListener {

	protected String nameQueue;
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

	public MessageRabbitConfigurationListener(String queueName,
			String exchageName) {
		this.nameQueue = queueName;
		this.nameFanoutExchange = exchageName;
	}

	public void init() {
		Queue queue = declareNameQueue();
		FanoutExchange exchange = createFanoutExchange();
		bindingQueueToExchange(queue, exchange);
		createListenerContainer(queue);
	}

	private Queue declareNameQueue() {
		String queueName = nameQueue + new UUID();
		System.out.println(queueName);
		Queue queue = new Queue(queueName, false);
		amqpAdmin.declareQueue(queue);
		return queue;
	}

	private void bindingQueueToExchange(Queue queue, FanoutExchange exchange) {
		BindingBuilder.bind(queue).to(exchange);
	}

	private FanoutExchange createFanoutExchange() {
		return new FanoutExchange(nameFanoutExchange, false, false);
	}

	public SimpleMessageListenerContainer createListenerContainer(Queue queue) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(
				amqpConnectionFactory);
		container.setQueues(queue);
		container.setMessageListener(createListenerAdapter());
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		return container;
	}

	public MessageListenerAdapter createListenerAdapter() {
		return new MessageListenerAdapter(this, messageConverter);
	}

	public void handleMessage(Object object) {
		handlersOfMessage.forEach((handler) -> {
			handler.handle(object);
		});

	}
}
