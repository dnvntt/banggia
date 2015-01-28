package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Transaction;

@Component
@EventHandlerApplyFor(priority = 3, values = {"TRANSACTION"})
public class TransactionMemoryHandler implements EventHandler {

	
	private InMemory memory;
	
	@Autowired
	public TransactionMemoryHandler(InMemory memory) {
		this.memory = memory;
	}

	@Override
	public void handle(Object source) {
		Transaction transaction = (Transaction) source;
		List<Transaction> transactions = (List<Transaction>) memory.get("TRANSACTION", transaction.getSymbol());
		if (transactions == null) {
			transactions = new ArrayList<Transaction>();
		}
		transactions.add(transaction);
		memory.put("TRANSACTION", transaction.getSymbol(), transactions);
		
	}

}