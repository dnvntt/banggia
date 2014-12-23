package priceboard.event.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.pusher.BroadcastPusher;
import vn.com.vndirect.priceservice.datamodel.Market;

@Component
@EventHandlerApplyFor(priority = 1, values = {"MARKET"})
public class MarketHandler implements EventHandler {

	private BroadcastPusher broadCastPusher;
	
	@Autowired
	public MarketHandler(BroadcastPusher broadCastPusher) {
		this.broadCastPusher = broadCastPusher;
	}

	@Override
	public void handle(Object source) {
		Market market = (Market) source;
		broadCastPusher.push(market);
	}

}
