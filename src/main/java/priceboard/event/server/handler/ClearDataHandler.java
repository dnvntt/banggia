package priceboard.event.server.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.event.EventHandler;
import priceboard.json.JsonParser;
import priceboard.room.ClientRoomManager;
import priceboard.room.StockRoomManager;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.FloorCode;

@Component
@DependsOn("bussinessDateLoader")
@EventHandlerApplyFor(priority = 1,values = { "CLEAR_DATA" })
public class ClearDataHandler implements EventHandler {
	private boolean isClearData;
	private InMemory memory;
	private StockRoomManager stockRoomManager;
	private ClientRoomManager clientRoomManager;
	private JsonParser parser;
	
	@Autowired
	public ClearDataHandler(InMemory memory, StockRoomManager stockRoomManager,ClientRoomManager clientRoomManager,JsonParser parser) {
		this.stockRoomManager = stockRoomManager;
		this.memory = memory;
		this.clientRoomManager= clientRoomManager;
		this.parser = parser;
		isClearData=false;
	}

	@PostConstruct
	public void load() {
		String businessDate = (String) memory.get("businessdate",
				"businessdate");
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		
		if (businessDate.equals(dateFormat.format(date).toString()))
			isClearData = false;
		else
			isClearData = true;
	}

	public boolean isClearData() {
		return isClearData;
	}

	@Scheduled(cron = "0 1 0 * * MON-FRI")
	public void reset() {
		isClearData = true; 
	}

	public void clearIfNeed() {	
		if (isClearData){
			System.out.println("Inside clear data");
			List<String> stocks = stockRoomManager
					.getStocksByRoom(FloorCode.HNX.getCode());
			
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
			
			memory.remove("CeilingFloor", "ALL");

			memory.remove("PutThroughTransaction",  FloorCode.HOSE.getCode());
			memory.remove("PutThroughTransaction",  FloorCode.HNX.getCode());
			
			memory.remove("PutThrough",   FloorCode.HOSE.getCode());
			memory.remove("PutThrough",   FloorCode.HNX.getCode());

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
			
			String data= parser.buildReturnJsonStockAsString("RESET", "true");
			List<ClientConnection> clients = clientRoomManager.getAllClient();
			clients.forEach((client) -> client.send(data));
		}

	}

	@Override
	public void handle(Object source) {
		if (isClearData) clearIfNeed();
	}
}
