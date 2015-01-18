package priceboard.event.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.pusher.TransactionPusher;


@Component
@EventHandlerApplyFor(values = {"TRANSACTION_PUSH"})
public class TransactionPushHandler implements EventHandler {

	private TransactionPusher transactionPusher;
	
	@Autowired
	public TransactionPushHandler(TransactionPusher transactionPusher) {
		this.transactionPusher = transactionPusher;
	}
	
	@Override
	public void handle(Object source) {
		transactionPusher.push(source);
	}
}
