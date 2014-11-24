package priceboard.room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;


@Component
public class StockRoomManager {

	private Map<String, CopyOnWriteArrayList<String>> roomStorage = new HashMap<String, CopyOnWriteArrayList<String>>();
	private Map<String, CopyOnWriteArrayList<String>> currentRoomsOfStock = new HashMap<String, CopyOnWriteArrayList<String>>();

	
	public void addStockToRoom(String room, String stock) {
		if (!isStockinRoom(room, stock)) {
			updateRoomStorage(room, stock);
			
			updateCurrentRoomOfStock(room, stock);
		}
	}
	
	private boolean isStockinRoom(String room, String stock) {
		CopyOnWriteArrayList<String> roomList = currentRoomsOfStock.get(stock);
		if (roomList == null) {
			return false;
		}
		return roomList.contains(room);
	}
	
	private void updateRoomStorage(String room, String stock) {
		CopyOnWriteArrayList<String> stockList = roomStorage.get(room);
		if (stockList == null) {
			stockList = new CopyOnWriteArrayList<String>();
		}
		stockList.add(stock);
		roomStorage.put(room, stockList);
	}
	
	private void updateCurrentRoomOfStock(String room, String stock) {
		CopyOnWriteArrayList<String> roomList = currentRoomsOfStock.get(stock);
		if (roomList == null) {
			roomList = new CopyOnWriteArrayList<String>();
		}
		roomList.add(room);
		currentRoomsOfStock.put(stock, roomList);		
	}
	

	public List<String> getRoomOfStock(String stock) {
		List<String> rooms = currentRoomsOfStock.get(stock);
		if (rooms == null) {
			return new ArrayList<String>();
		}
		return currentRoomsOfStock.get(stock);
	}

}
