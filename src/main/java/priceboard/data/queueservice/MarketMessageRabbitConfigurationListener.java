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

public class MarketMessageRabbitConfigurationListener extends
		MessageRabbitConfigurationListener {

	// @Value("${market_external_queue}")
	// private String marketQueue;
	//
	// @Value("${market_fanout_exchange}")
	// private String marketFanoutExchange;
	@Autowired
	public MarketMessageRabbitConfigurationListener(
			@Value("${market_external_queue}") String queueName,@Value("${market_fanout_exchange}") String exchageName) 
	{
		super();
		this.nameQueue = queueName;
		this.nameFanoutExchange = exchageName;
	}

	@Override
	public void init() {
		this.handlersOfMessage = this.eventHandlerFilter.filter(handlers,
				Arrays.asList("MARKET", "CLEAR_DATA", "COMMON"));

		// new Thread(() -> {
		// while(true) {
		// Market market = createMockData();
		// handleMessage(market);
		// }
		// }).start();

	}

}
