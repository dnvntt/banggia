package priceboard.data.queueservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerApplyFor;
import priceboard.event.server.handler.EventHandlerFilter;

import com.eaio.uuid.UUID;

@Configuration
public class StockMessageRabbitConfigurationListener {

	@Value("${secinfo_external_queue}")
	private String queueStock;

	@Value("${secinfo_fanout_exchange}")
	private String fanoutExchangeStock;

	@Autowired
	private MessageConverter messageConverter;

	@Autowired
	private ConnectionFactory amqpConnectionFactory;

	@Autowired
	private EventHandlerFilter filter;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private AmqpAdmin amqpAdmin;

	@Autowired
	private EventHandlerFilter eventHandlerFilter;

	private List<EventHandler> handlers = new ArrayList<EventHandler>();

	@PostConstruct
	public void init() {
		Map<String, Object> map = applicationContext.getBeansWithAnnotation(EventHandlerApplyFor.class);
		map.forEach((key, object) -> {
			handlers.add((EventHandler) object);
		});

		handlers = eventHandlerFilter.filter(handlers, Arrays.asList("STOCK", "ALL"));
	}

	@Bean
	public Queue stockQueue() {
		Queue stockQueue = declareStockQueue();
		bindingToExchange(stockQueue);
		return stockQueue;
	}

	private Queue declareStockQueue() {
		Queue stockQueue = new Queue(queueStock + new UUID(), false);
		amqpAdmin.declareQueue(stockQueue);
		return stockQueue;
	}
	
	private void bindingToExchange(Queue stockQueue) {
		BindingBuilder.bind(stockQueue).to(stockFanoutExchange());
	}
	
	private FanoutExchange stockFanoutExchange() {
		return new FanoutExchange(fanoutExchangeStock, false, false);
	}


	@Bean
	public SimpleMessageListenerContainer stockListenerContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(amqpConnectionFactory);
		container.setQueues(stockQueue());
		container.setMessageListener(stockListenerAdapter());
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		return container;
	}

	@Bean
	public MessageListenerAdapter stockListenerAdapter() {
		return new MessageListenerAdapter(this, messageConverter);
	}

	public void handleMessage(Object object) {
		System.out.println(object);
		handlers.forEach((handler) -> {
			handler.handle(object);
		});

	}
}
