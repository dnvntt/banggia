package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.PutThrough;


@Component
@EventHandlerApplyFor(priority = 5, values = {"PutThrough"})
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
		}
		putThroughs.add(putThrough);
		memory.put("PutThrough", putThrough.getFloorCode(), putThroughs);
				
	}

}