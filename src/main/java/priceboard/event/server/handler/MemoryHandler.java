package priceboard.event.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.util.InstanceChecker;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;
import vn.com.vndirect.priceservice.datamodel.SecInfo;


@Component
@EventHandlerApplyFor(priority = 1, values = {"ALL"})
public class MemoryHandler implements EventHandler {

	
	private InMemory memory;
	
	@Autowired
	public MemoryHandler(InMemory memory) {
		this.memory = memory;
	}

	@Override
	public void handle(Object source) {
		if (InstanceChecker.isStock(source)) {
			memory.put("STOCK", ((SecInfo) source).getCode(), source);
		} else if (InstanceChecker.isMarket(source)) {
			memory.put("MARKET", ((Market) source).getFloorCode(), source);
		}
	}

}
