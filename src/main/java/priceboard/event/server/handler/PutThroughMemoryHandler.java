package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.PutThrough;
import vn.com.vndirect.priceservice.datamodel.SecInfo;


@Component
@EventHandlerApplyFor(priority =3, values = {"PutThrough"})
public class PutThroughMemoryHandler implements EventHandler {

	
	private InMemory memory;
	
	@Autowired
	public PutThroughMemoryHandler(InMemory memory) {
		this.memory = memory;
	}

	@Override
	public void handle(Object source) {
		PutThrough putThrough = (PutThrough) source;
		List<PutThrough> putThroughs = (List<PutThrough>) memory.get("PutThrough", putThrough.getFloorCode());
		if (putThroughs == null) {
			putThroughs = new ArrayList<PutThrough>();
			memory.put("PutThrough", putThrough.getFloorCode(), putThroughs);
		}
		
		SecInfo stock = (SecInfo) memory.get("STOCK", putThrough.getStockSymbol());
		putThrough.setBasicPrice(stock.getBasicPrice());
		putThrough.setCeilingPrice(stock.getCeilingPrice());
		putThrough.setFloorPrice(stock.getFloorPrice());
		
		putThroughs.add(putThrough);
	}

}
