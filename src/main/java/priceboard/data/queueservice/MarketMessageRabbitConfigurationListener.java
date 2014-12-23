package priceboard.data.queueservice;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
import priceboard.event.server.handler.EventHandlerFilter;
import vn.com.vndirect.priceservice.datamodel.Market;

import com.eaio.uuid.UUID;

public class MarketMessageRabbitConfigurationListener {

	@Value("${market_external_queue}")
	private String marketQueue;

	@Value("${market_fanout_exchange}")
	private String marketFanoutExchange;

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

	@Autowired
	private List<EventHandler> handlers;

	private List<EventHandler> handlersOfMarket;

	@PostConstruct
	public void init() {
		handlersOfMarket = eventHandlerFilter.filter(handlers, Arrays.asList("MARKET", "ALL"));
		
		new Thread(() -> {
			while(true) {
				Market market = createMockData(); 
				//handleMessage(market);
			}
		}).start();
		
	}

	private Market createMockData() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		int random = new Random().nextInt(100);
		Market market = new Market();
		market.setFloorCode("02");
		market.setMarketIndex(123.4 + random);
		market.setAdvance(13 + random);
		market.setControlCode("5");
		market.setNoChange(3 + random);
		market.setStatus("5");
		return market;
	}

	@Bean
	public Queue marketQueue() {
		Queue maretQueue = declareMarketQueue();
		bindingToExchange(maretQueue);
		return maretQueue;
	}

	private Queue declareMarketQueue() {
		Queue queue = new Queue(marketQueue + new UUID(), false);
		amqpAdmin.declareQueue(queue);
		return queue;
	}
	
	private void bindingToExchange(Queue marketQueue) {
		BindingBuilder.bind(marketQueue).to(stockFanoutExchange());
	}
	
	private FanoutExchange stockFanoutExchange() {
		return new FanoutExchange(marketFanoutExchange, false, false);
	}


	@Bean
	public SimpleMessageListenerContainer marketListenerContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(amqpConnectionFactory);
		container.setQueues(marketQueue());
		container.setMessageListener(marketListenerAdapter());
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		return container;
	}

	@Bean
	public MessageListenerAdapter marketListenerAdapter() {
		return new MessageListenerAdapter(this, messageConverter);
	}

	public void handleMessage(Object object) {
		handlers.forEach((handler) -> {
			handler.handle(object);
		});

	}
}
