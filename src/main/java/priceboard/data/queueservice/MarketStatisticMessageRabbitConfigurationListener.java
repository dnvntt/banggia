package priceboard.data.queueservice;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MarketStatisticMessageRabbitConfigurationListener extends
		MessageRabbitConfigurationListener {

	@Autowired
	public MarketStatisticMessageRabbitConfigurationListener(
			@Value("${market_static_queue}") String queueName, 
			@Value("${market_statistic_exchange}") String exchageName) {
		super(queueName, exchageName);
	}

	@PostConstruct
	public void init() {
		super.init();
		setMessageHandler();
	}

	private void setMessageHandler() {
		this.handlersOfMessage = this.eventHandlerFilter.filter(handlers, Arrays.asList("MARKET_STATISTIC","MARKET_STATISTIC_PUSH"));
	}

	
	@Bean
	public SimpleMessageListenerContainer marketStatisticListenerContainer() {
		return super.createListenerContainer();
	}


}
