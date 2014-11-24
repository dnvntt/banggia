package priceboard.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import priceboard.event.client.handler.ClientEventTypeMapping;
import priceboard.json.JsonParser;
import priceboard.websocket.handler.DefaultEchoService;
import priceboard.websocket.handler.EchoWebSocketHandler;
import priceboard.websocket.handler.StockWebSocketHandler;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

@Configuration
@EnableWebMvc
@EnableWebSocket
@PropertySource("classpath:/config/app.properties")
@ComponentScan("priceboard")
public class WebConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

		registry.addHandler(echoWebSocketHandler(), "/sockjs/echo").withSockJS();
		registry.addHandler(stockWebSocketHandler(), "/stock/echo").withSockJS();
	}

	@Bean
	public WebSocketHandler echoWebSocketHandler() {
		return new EchoWebSocketHandler(echoService());
	}
	
	@Bean
	public StockWebSocketHandler stockWebSocketHandler() {
		return new StockWebSocketHandler(clientEventTypeMapping(), memory(), jsonParser());
	}


	@Bean
	public ClientEventTypeMapping clientEventTypeMapping() {
		return new ClientEventTypeMapping();
	}
	
	@Bean
	public InMemory memory() {
		return new InMemory();
	}
	
	@Bean
	public JsonParser jsonParser() {
		return new JsonParser();
	}

	@Bean
	public DefaultEchoService echoService() {
		return new DefaultEchoService("Did you say \"%s\"?");
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	@Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
       return new PropertySourcesPlaceholderConfigurer();
    }

}
