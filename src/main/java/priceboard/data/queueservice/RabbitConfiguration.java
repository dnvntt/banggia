package priceboard.data.queueservice;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
	/**
	 * Shared topic exchange used for publishing any market data (e.g. stock quotes) 
	 */
	protected static String MARKET_DATA_EXCHANGE_NAME = "app.stock.marketdata";

	/**
	 * The server-side consumer's queue that provides point-to-point semantics for stock requests.
	 */
	protected static String STOCK_REQUEST_QUEUE_NAME = "app.stock.request";

	/**
	 * Key that clients will use to send to the stock request queue via the default direct exchange.
	 */
	protected static String STOCK_REQUEST_ROUTING_KEY = STOCK_REQUEST_QUEUE_NAME;
	
	@Value("${amqp.port:5672}") 
	private int port = 5672;
	
	@Value("${rabbitmq.addresses}") 
	private String rabbitmqAddresses;
	
	
	@Value("${rabbitmq.username}") 
	private String username;
	
	@Value("${rabbitmq.password}") 
	private String password;
	
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(rabbitmqAddresses);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		return connectionFactory;
	}

	@Bean 
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setMessageConverter(jsonMessageConverter());
		return template;
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new JsonMessageConverter();
	}
	
	/**
	 * @return the admin bean that can declare queues etc.
	 */
	@Bean
	public AmqpAdmin amqpAdmin() {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
		return rabbitAdmin ;
	}

}
