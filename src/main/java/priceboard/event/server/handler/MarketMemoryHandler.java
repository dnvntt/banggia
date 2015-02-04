package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.stock.compress.Mashaller;
import priceboard.util.InstanceChecker;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;
import vn.com.vndirect.priceservice.datamodel.Transaction;

@Component
@EventHandlerApplyFor(priority = 3, values = { "MARKET" })
public class MarketMemoryHandler implements EventHandler {

	private InMemory memory;
	private Mashaller mashaller;
	@Autowired
	public MarketMemoryHandler(InMemory memory, Mashaller mashaller) {
		this.memory = memory;
		this.mashaller = mashaller;
	}

	@Override
	public void handle(Object source) {
		if (InstanceChecker.isMarket(source)) {
			String key = ((Market) source).getFloorCode();
			memory.put("MARKET", key, source);
			String compression = mashaller.compress(source);
			memory.put("MARKET_COMPRESSION", key, compression);
			
			//update market History Information to memory
			List<Market> marketList = (ArrayList<Market>) memory.get("ALL_MARKET", key);
			if (marketList == null) {
				marketList = new ArrayList<Market>();
				memory.put("ALL_MARKET", key,marketList);
			}
			marketList.add((Market) source);
		} 
	}
}
