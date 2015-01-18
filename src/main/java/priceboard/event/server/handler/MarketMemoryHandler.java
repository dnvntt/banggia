package priceboard.event.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.util.InstanceChecker;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;

@Component
@EventHandlerApplyFor(priority = 3, values = { "MARKET" })
public class MarketMemoryHandler implements EventHandler {

	private InMemory memory;

	@Autowired
	public MarketMemoryHandler(InMemory memory) {
		this.memory = memory;
	}

	@Override
	public void handle(Object source) {
		if (InstanceChecker.isMarket(source)) {
			memory.put("MARKET", ((Market) source).getFloorCode(), source);
		} 
	}

}
