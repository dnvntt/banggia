package priceboard.event.server.handler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import priceboard.room.StockRoomManager;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.FloorCode;
import vn.com.vndirect.priceservice.datamodel.Market;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

public class ClearDataTest {
	
	private InMemory memory;
	private ClearDataHandler clearData;
	private StockRoomManager stockManager;
	
	
	@Before
	public void setup() {
		memory = new InMemory();
		stockManager = new StockRoomManager();
		clearData = new ClearDataHandler(memory, stockManager);
	}
	
	@Test
	public void testClearDataWhenAppIsResetInTradingDay() {
		memory.put("STOCK", "VND", new SecInfo());
		clearData.clearIfNeed();
		Object vnd = memory.get("STOCK", "VND");
		Assert.assertNotNull(vnd);
	}
	
	@Test
	public void testClearDataWhenAppIsResetInWeekendDay() {
		memory.put("STOCK", "VND", new SecInfo());
		clearData.clearIfNeed();
		Object vnd = memory.get("STOCK", "VND");
		Assert.assertNotNull(vnd);
	}
	
	
	@Test
	public void testClearStockDataWhenBeginOfTradingDay() {
		memory.put("businessdate", "businessdate", "05012015");
		stockManager.addStockToRoom(FloorCode.HNX.getCode(), "VND");
		stockManager.addStockToRoom(FloorCode.HOSE.getCode(), "SSI");
		stockManager.addStockToRoom(FloorCode.UPCOM.getCode(), "STL");
		clearData.load();
		memory.put("STOCK", "SSI", new SecInfo());
		memory.put("STOCK", "STL", new SecInfo());
		memory.put("STOCK", "VND", new SecInfo());
		clearData.clearIfNeed();
		Object vnd = memory.get("STOCK", "VND");
		Assert.assertNull("VND is not cleared", vnd);
		Object ssi = memory.get("STOCK", "SSI");
		Assert.assertNull("SSI is not cleared", ssi);
		Object stl = memory.get("STOCK", "STL");
		Assert.assertNull("STL is not cleared", stl);
	}
	
	@Test
	public void testDontClearStockDataWhenBeginOfTradingDayAndCallClearData2Times() {
		memory.put("businessdate", "businessdate", "05012015");
		stockManager.addStockToRoom(FloorCode.HNX.getCode(), "VND");
		stockManager.addStockToRoom(FloorCode.HOSE.getCode(), "SSI");
		stockManager.addStockToRoom(FloorCode.UPCOM.getCode(), "STL");
		clearData.load();
		memory.put("STOCK", "SSI", new SecInfo());
		memory.put("STOCK", "STL", new SecInfo());
		memory.put("STOCK", "VND", new SecInfo());
		clearData.clearIfNeed();
		Object vnd = memory.get("STOCK", "VND");
		Assert.assertNull("VND is not cleared", vnd);
		Object ssi = memory.get("STOCK", "SSI");
		Assert.assertNull("SSI is not cleared", ssi);
		Object stl = memory.get("STOCK", "STL");
		Assert.assertNull("STL is not cleared", stl);
		
		memory.put("STOCK", "SSI", new SecInfo());
		memory.put("STOCK", "STL", new SecInfo());
		memory.put("STOCK", "VND", new SecInfo());
		clearData.clearIfNeed();
		 
		
		vnd = memory.get("STOCK", "VND");
		Assert.assertNotNull("VND is cleared", vnd);
		ssi = memory.get("STOCK", "SSI");
		Assert.assertNotNull("SSI is cleared", ssi);
		stl = memory.get("STOCK", "STL");
		Assert.assertNotNull("STL is cleared", stl);
	}
	
	@Test
	public void testClearMarketDataWhenBeginOfTradingDay() {
		memory.put("businessdate", "businessdate", "05012015");
		Market market1 = new Market();
		market1.setFloorCode("02");
		market1.setMarketIndex(123.4);
		
		Market market2 = new Market();
		market2.setFloorCode("10");
		market2.setMarketIndex(533.4);
		
		 
		clearData.load();
		//Assert.assertEquals(clearData.isClearData(), true);
		memory.put("MARKET", market1.getFloorCode(),market1);
		memory.put("MARKET", market2.getFloorCode(),market2);
		clearData.clearIfNeed();
		Object hnx = memory.get("MARKET", "02");
		Assert.assertNull("HNX is not cleared", hnx);
		Object hose = memory.get("MARKET", "10");
		Assert.assertNull("HOSE is not cleared", hose);
		 
	}
	
	@Test
	public void testClearMarketDataWhenBeginOfTradingDay2times() {
		memory.put("businessdate", "businessdate", "05012015");
		Market market1 = new Market();
		market1.setFloorCode("02");
		market1.setMarketIndex(123.4);
		
		Market market2 = new Market();
		market2.setFloorCode("10");
		market2.setMarketIndex(533.4);
		
		 
		clearData.load();
		
		memory.put("MARKET", market1.getFloorCode(),market1);
		memory.put("MARKET", market2.getFloorCode(),market2);
		
		clearData.clearIfNeed();
		
		market1.setFloorCode("02");
		market1.setMarketIndex(123.4);
		
		 
		market2.setFloorCode("10");
		market2.setMarketIndex(533.4);
		memory.put("MARKET", market1.getFloorCode(),market1);
		memory.put("MARKET", market2.getFloorCode(),market2);
		
		clearData.clearIfNeed();
		
		Object hnx = memory.get("MARKET", "02");
		Assert.assertNotNull("HNX is not cleared", hnx);
		Object hose = memory.get("MARKET", "10");
		Assert.assertNotNull("HOSE is not cleared", hose);
		 
	}
	
	
	

}
