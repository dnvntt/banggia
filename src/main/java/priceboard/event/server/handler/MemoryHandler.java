package priceboard.event.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
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
		if (SecInfo.class.isInstance(source)) {
			memory.put("STOCK", ((SecInfo) source).getCode(), source);
		}
	}

}
