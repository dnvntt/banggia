package priceboard.event.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.pusher.BroadcastPusher;

@Component
@EventHandlerApplyFor(priority = 7,values = {"MARKET_PUSH","PTORDER_PUSH","PutThrough_PUSH"})
public class BroadcastPushHandler implements EventHandler {

	private BroadcastPusher broadCastPusher;
	
	@Autowired
	public BroadcastPushHandler(BroadcastPusher broadCastPusher) {
		this.broadCastPusher = broadCastPusher;
	}

	@Override
	public void handle(Object source) {
		broadCastPusher.push(source);
	}

}
