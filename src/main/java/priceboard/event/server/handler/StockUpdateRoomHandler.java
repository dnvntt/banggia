package priceboard.event.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.room.StockRoomManager;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

@Component
@EventHandlerApplyFor(priority = 9, values = {"STOCK"})
public class StockUpdateRoomHandler implements EventHandler {

	private StockRoomManager roomManager;
	
	private  MarketSessionChecker marketSessionChecker;
	
	@Autowired
	public StockUpdateRoomHandler(StockRoomManager roomManager, MarketSessionChecker marketSessionChecker) {
		this.roomManager = roomManager;
		this.marketSessionChecker = marketSessionChecker;
	}
	
	@Override
	public void handle(Object source) {
		updateRoom(source);
	}

	private void updateRoom(Object source) {
		SecInfo secInfo = (SecInfo) source;
		if (marketSessionChecker.isClosedSession(secInfo.getFloorCode())) {
			roomManager.addStockToRoom(secInfo.getFloorCode(), secInfo.getCode());
		}
	}

}
