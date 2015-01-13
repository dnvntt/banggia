package priceboard.data.queueservice;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MarketMessageRabbitConfigurationListener extends
		MessageRabbitConfigurationListener {

	@Autowired
	public MarketMessageRabbitConfigurationListener(
			@Value("${market_external_queue}") String queueName,
			@Value("${market_fanout_exchange}") String exchageName) {
		super(queueName, exchageName);
	}

	@PostConstruct
	public void init() {
		super.init();
		setMessageHandler();

		/*
		 * new Thread(() -> { while (true) { Market market = createMockData();
		 * handleMessage(market); } }).start();
		 */

	}

	private void setMessageHandler() {
		this.handlersOfMessage = this.eventHandlerFilter.filter(handlers,
				Arrays.asList("MARKET", "CLEAR_DATA", "COMMON"));
	}

	/*
	 * private Market createMockData() { try { Thread.sleep(1000); } catch
	 * (InterruptedException e) { } int random = new Random().nextInt(100);
	 * Market market = new Market(); market.setFloorCode("02");
	 * market.setMarketIndex(123.4 + random); market.setAdvance(13 + random);
	 * market.setControlCode("5"); market.setNoChange(3 + random);
	 * market.setStatus("5"); return market; }
	 */

}
