package priceboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

/**
 * Hello world!
 *
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
	@Bean
	public JsonParser jsonParser() {
		return new JsonParser();
	}
	
	@Bean
	public InMemory memory() {
		return new InMemory();
	}
}