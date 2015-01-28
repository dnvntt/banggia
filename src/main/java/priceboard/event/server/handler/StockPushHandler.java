package priceboard.event.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.pusher.StockPusher;

@Component
@EventHandlerApplyFor(priority = 7,values = {"STOCK_PUSH"})
public class StockPushHandler implements EventHandler {

	private StockPusher stockPusher;
	
	@Autowired
	public StockPushHandler(StockPusher stockPusher) {
		this.stockPusher = stockPusher;
	}
	
	@Override
	public void handle(Object source) {
		stockPusher.push(source);
	}
}
