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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerFilter;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

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
	private AmqpAdmin amqpAdmin;

	@Autowired
	private EventHandlerFilter eventHandlerFilter;

	@Autowired
	private List<EventHandler> handlers;
	
	private List<EventHandler> handlersOfStock;

	@PostConstruct
	public void init() {
		handlersOfStock = eventHandlerFilter.filter(handlers, Arrays.asList("STOCK", "STOCK_PUSH", "ALL"));
		/*new Thread(() -> {
			while(true) {
				SecInfo secInfo = createMockData(); 
				handleMessage(secInfo);
			}
		}).start();*/
		
	}

	private SecInfo createMockData() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		SecInfo secInfo = new SecInfo();
		List<String> arr = Arrays.asList("HAG", "SSI", "VND");
		int random = new Random().nextInt(100);
		secInfo.setCode(arr.get(random % (arr.size())));
		secInfo.setBasicPrice(10.5);
		secInfo.setFloorPrice(10);;
		secInfo.setCeilingPrice(12);
		secInfo.setFloorCode("10");
		secInfo.setAccumulatedVal(12000 + random);
		secInfo.setAccumulatedVal(120 + random);
		secInfo.setMatchPrice(10 + random);
		secInfo.setMatchQtty(4000 + random);
		secInfo.setBidPrice01(12 + random);
		secInfo.setBidQtty01(200 + random);
		secInfo.setBidPrice02(11 + random);
		secInfo.setBidQtty02(10 + random);
		secInfo.setBidQtty03(200 + random);
		secInfo.setBidPrice03(11 + random);
		secInfo.setOfferPrice01(10 + random);
		secInfo.setOfferQtty01(3000 + random);
		secInfo.setOfferPrice02(11 + random);
		secInfo.setOfferQtty02(3000 + random);
		secInfo.setOfferPrice03(11.5 + random);
		secInfo.setOfferQtty03(3000 + random);
		secInfo.setCompanyName("Hoang Anh Gia Lai " + random);
		secInfo.setCurrentRoom(3000 + random);
		return secInfo;
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
		handlersOfStock.forEach((handler) -> {
			handler.handle(object);
		});

	}
}
