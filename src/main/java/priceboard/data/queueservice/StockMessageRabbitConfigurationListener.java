package priceboard.data.queueservice;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StockMessageRabbitConfigurationListener extends
		MessageRabbitConfigurationListener {
	
	
	@Autowired
	public StockMessageRabbitConfigurationListener(
			@Value("${secinfo_external_queue}") String queueName,
			@Value("${secinfo_fanout_exchange}") String exchageName) {
		super(queueName, exchageName);
	}

	@PostConstruct
	public void init() {
		super.init();
		setMessageHandler();

		/*
		 * new Thread(() -> { while (true) { SecInfo secInfo = createMockData();
		 * handleMessage(secInfo); } }).start();
		 */
	}

	private void setMessageHandler() {
		this.handlersOfMessage = this.eventHandlerFilter.filter(handlers,
				Arrays.asList("STOCK", "STOCK_PUSH", "CLEAR_DATA", "COMMON"));
	}

	/*
	 * private SecInfo createMockData() { try { Thread.sleep(1000); } catch
	 * (InterruptedException e) { }
	 * 
	 * SecInfo secInfo = new SecInfo(); List<String> arr = Arrays.asList("HAG",
	 * "SSI", "VND"); int random = new Random().nextInt(100);
	 * secInfo.setCode(arr.get(random % (arr.size())));
	 * secInfo.setStockType("S"); secInfo.setBasicPrice(10.5);
	 * secInfo.setFloorPrice(10);; secInfo.setCeilingPrice(12);
	 * secInfo.setFloorCode("10"); secInfo.setAccumulatedVal(12000 + random);
	 * secInfo.setAccumulatedVal(120 + random); secInfo.setMatchPrice(10 +
	 * random); secInfo.setMatchQtty(4000 + random); secInfo.setBidPrice01(12 +
	 * random); secInfo.setBidQtty01(200 + random); secInfo.setBidPrice02(11 +
	 * random); secInfo.setBidQtty02(10 + random); secInfo.setBidQtty03(200 +
	 * random); secInfo.setBidPrice03(11 + random); secInfo.setOfferPrice01(10 +
	 * random); secInfo.setOfferQtty01(3000 + random);
	 * secInfo.setOfferPrice02(11 + random); secInfo.setOfferQtty02(3000 +
	 * random); secInfo.setOfferPrice03(11.5 + random);
	 * secInfo.setOfferQtty03(3000 + random);
	 * secInfo.setCompanyName("Hoang Anh Gia Lai " + random);
	 * secInfo.setCurrentRoom(3000 + random); return secInfo; }
	 */

}
