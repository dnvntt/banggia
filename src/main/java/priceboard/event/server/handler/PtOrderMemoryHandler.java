package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;

@Component
@EventHandlerApplyFor(priority = 3, values = { "PTORDER" })
public class PtOrderMemoryHandler implements EventHandler {

	private InMemory memory;

	@Autowired
	public PtOrderMemoryHandler(InMemory memory) {
		this.memory = memory;
	}

	@Override
	public void handle(Object source) {

			PutThroughTransaction putThroughTransaction = (PutThroughTransaction) source;
			List<PutThroughTransaction> putThroughTransactions = (List<PutThroughTransaction>) memory.get("PutThroughTransaction", putThroughTransaction.getFloorCode());
			if (putThroughTransactions == null) {
				putThroughTransactions = new ArrayList<PutThroughTransaction>();
			}
			putThroughTransactions.add(putThroughTransaction);
			memory.put("PutThroughTransaction", putThroughTransaction.getFloorCode(), putThroughTransactions);

	}

}