package priceboard.event.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.stock.compress.Mashaller;
import priceboard.util.InstanceChecker;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

@Component
@EventHandlerApplyFor(priority = 1, values = {"ALL"})
public class CompressionHandler implements EventHandler {

	private InMemory memory;
	
	private Mashaller mashaller;
	
	@Autowired
	public CompressionHandler(InMemory memory, Mashaller mashaller) {
		this.memory = memory;
		this.mashaller = mashaller;
	}

	@Override
	public void handle(Object source) {
		String compression = mashaller.compress(source);
		String key = "";
		if (InstanceChecker.isStock(source)) {
			key = ((SecInfo) source).getCode();
			memory.put("STOCK_COMPRESSION", key, compression);
			return;
		}
		
		if (InstanceChecker.isMarket(source)) {
			key = ((Market) source).getFloorCode(); 
			memory.put("MARKET_COMPRESSION", key, compression);
			return;
		}
	}


}
