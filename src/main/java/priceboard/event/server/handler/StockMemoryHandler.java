package priceboard.event.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.stock.compress.Mashaller;
import priceboard.util.InstanceChecker;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

@Component
@EventHandlerApplyFor(priority = 3, values = {"STOCK"})
public class StockMemoryHandler implements EventHandler {
	private InMemory memory;
	private Mashaller mashaller;
	
	@Autowired
	public StockMemoryHandler(InMemory memory, Mashaller mashaller) {
		this.memory = memory;
		this.mashaller = mashaller;
	}

	@Override
	public void handle(Object source) {
		if (InstanceChecker.isStock(source)) {
			String key =((SecInfo) source).getCode();
			memory.put("STOCK", key, source);
			String compression = mashaller.compress(source);
			memory.put("STOCK_COMPRESSION", key, compression);
		} 
	}
	
	

}
