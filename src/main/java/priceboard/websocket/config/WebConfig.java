package priceboard.websocket.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerApplyFor;
import priceboard.event.server.handler.EventHandlerFilter;
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

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private EventHandlerFilter eventHandlerFilter;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

		registry.addHandler(echoWebSocketHandler(), "/sockjs/echo").withSockJS();
		registry.addHandler(stockWebSocketHandler(), "/realtime").withSockJS();
	}

	@Bean
	public WebSocketHandler echoWebSocketHandler() {
		return new EchoWebSocketHandler(echoService());
	}
	
	@Bean
	public StockWebSocketHandler stockWebSocketHandler() {
		return new StockWebSocketHandler(memory(), jsonParser(), eventHandlerFilter, handlers());
	}
	
	@Bean
	public List<EventHandler> handlers() {
		List<EventHandler> handlers = new ArrayList<EventHandler>();
		Map<String, Object> map = applicationContext.getBeansWithAnnotation(EventHandlerApplyFor.class);
		map.forEach((key, object) -> {
			handlers.add((EventHandler) object);
		});
		return handlers;
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
