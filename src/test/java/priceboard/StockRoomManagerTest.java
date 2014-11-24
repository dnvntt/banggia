package priceboard;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.room.StockRoomManager;

public class StockRoomManagerTest {
	
	private StockRoomManager stockRoomManager;
	
	@Before
	public void setUp() {
		stockRoomManager = new StockRoomManager();
	}
	
	@Test
	public void testGetRoomOfStock() {
		String stock = "VND";
		String room01 = "01";
		stockRoomManager.addStockToRoom(room01, stock);
		String room02 = "02";
		stockRoomManager.addStockToRoom(room02, stock);
		List<String> actualRooms = stockRoomManager.getRoomOfStock(stock);
		Assert.assertTrue(actualRooms.contains(room01));
		Assert.assertTrue(actualRooms.contains(room02));
	}
	
	@Test
	public void testAddRoomOfStockAndCheckDuplicatedStock() {
		String stock = "VND";
		String room01 = "01";
		stockRoomManager.addStockToRoom(room01, stock);
		stockRoomManager.addStockToRoom(room01, stock);
		List<String> actualRooms = stockRoomManager.getRoomOfStock(stock);
		Assert.assertTrue(actualRooms.contains(room01));
		Assert.assertEquals(1, actualRooms.size());
	}
}
