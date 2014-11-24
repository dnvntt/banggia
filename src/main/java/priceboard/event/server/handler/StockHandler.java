package priceboard.event.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.pusher.Pusher;
import priceboard.room.StockRoomManager;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

@Component
@EventHandlerApplyFor(priority = 10, values = {"STOCK"})
public class StockHandler implements EventHandler {

	private Pusher stockPusher;
	
	private StockRoomManager roomManager;
	
	@Autowired
	public StockHandler(Pusher stockUpdater, StockRoomManager roomManager) {
		this.stockPusher = stockUpdater;
		this.roomManager = roomManager;
	}

	
	@Override
	public void handle(Object source) {
		if (source == null) return;
		// TODO: USING Room Handler
		// TODO: USING Memory Handler
		updateRoom(source);
		stockPusher.push(source);
	}

	private void updateRoom(Object source) {
		SecInfo secInfo = (SecInfo) source;
		roomManager.addStockToRoom(secInfo.getFloorCode(), secInfo.getCode());
	}

}
