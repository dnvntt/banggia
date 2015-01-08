package priceboard.event.server.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.room.StockRoomManager;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.FloorCode;

@Component
@EventHandlerApplyFor(values = { "CLEAR_DATA" })
public class ClearDataHandler implements EventHandler {
	// private ElasticSearchClient elasticSearchClient;
	private boolean isClearData;
	private InMemory memory;
	private StockRoomManager stockRoomManager;

	@Autowired
	public ClearDataHandler(InMemory memory, StockRoomManager stockRoomManager) {
		System.out.println("Loading order: create ClearLoader instance");
		this.stockRoomManager = stockRoomManager;
		this.memory = memory;
		isClearData=false;
	}

	@PostConstruct
	public void load() {
		System.out.println("Loading order: clear loading");
		String businessDate = (String) memory.get("businessdate",
				"businessdate");
		//System.out.println("businessDate in cleardataHandler:"+businessDate);
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		//TODO remove
		/*if (businessDate.equals(dateFormat.format(date).toString()))
			isClearData = false;
		else
			isClearData = true;*/
		
		isClearData = true;
		
	}

	public boolean isClearData() {
		return isClearData;
	}

	@Scheduled(cron = "1 1 1 * * MON-FRI")
	public void reset() throws Exception {
		isClearData = true; // reset at 1am of new day
	}

	public void clearIfNeed() {
		if (isClearData) {
			System.out.println("clear data previous date");
			List<String> stocks = stockRoomManager
					.getStocksByRoom(FloorCode.HNX.getCode());
			System.out.println("No stock of HNX:" + stocks.size());
			
			stocks.forEach((stock) -> {
				memory.remove("STOCK", stock);
				memory.remove("STOCK_COMPRESSION", stock);
				memory.remove("TRANSACTION", stock);
			});
			stocks = stockRoomManager.getStocksByRoom(FloorCode.HOSE.getCode());
			stocks.forEach((stock) -> {
				memory.remove("STOCK", stock);
				memory.remove("STOCK_COMPRESSION", stock);
				memory.remove("TRANSACTION", stock);
			});
			stocks = stockRoomManager
					.getStocksByRoom(FloorCode.UPCOM.getCode());
			stocks.forEach((stock) -> {
				memory.remove("STOCK", stock);
				memory.remove("STOCK_COMPRESSION", stock);
				memory.remove("TRANSACTION", stock);
			});

			memory.remove("PutThroughTransaction", "");
			memory.remove("PutThrough", "");

			memory.remove("MARKET", FloorCode.HNX.getCode());
			memory.remove("MARKET_COMPRESSION", FloorCode.HNX.getCode());
			memory.remove("ALL_MARKET", FloorCode.HNX.getCode());

			memory.remove("MARKET", FloorCode.HOSE.getCode());
			memory.remove("MARKET_COMPRESSION", FloorCode.HOSE.getCode());
			memory.remove("ALL_MARKET", FloorCode.HOSE.getCode());

			memory.remove("MARKET", FloorCode.UPCOM.getCode());
			memory.remove("MARKET_COMPRESSION", FloorCode.UPCOM.getCode());
			memory.remove("ALL_MARKET", FloorCode.UPCOM.getCode());
			
			memory.remove("MARKET", FloorCode.HNX30.getCode());
			memory.remove("MARKET_COMPRESSION", FloorCode.HNX30.getCode());
			memory.remove("ALL_MARKET", FloorCode.HNX30.getCode());
			
			memory.remove("MARKET", FloorCode.VN30.getCode());
			memory.remove("MARKET_COMPRESSION", FloorCode.VN30.getCode());
			memory.remove("ALL_MARKET", FloorCode.VN30.getCode());

			isClearData = false;
		}

	}

	@Override
	public void handle(Object source) {
		//System.out.println("clear:handler");
		clearIfNeed();
	}
}
