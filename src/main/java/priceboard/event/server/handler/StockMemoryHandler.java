package priceboard.event.server.handler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.util.InstanceChecker;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

@Component
@EventHandlerApplyFor(priority = 2, values = {"STOCK"})
public class StockMemoryHandler implements EventHandler {
	private static final Logger logger = Logger.getLogger(StockMemoryHandler.class);	
	private InMemory memory;
	@Autowired
	public StockMemoryHandler(InMemory memory) {
		this.memory = memory;
	}

	@Override
	public void handle(Object source) {
		if (InstanceChecker.isStock(source)) {
			SecInfo myStock =(SecInfo) source;
			logger.info("myStock Code:"+ myStock.getCode() + " AccumulatedVol:"+ myStock.getAccumulatedVol());
			memory.put("STOCK", ((SecInfo) source).getCode(), source);
		} 
	}

}
