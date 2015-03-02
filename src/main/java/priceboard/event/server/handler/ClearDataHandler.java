package priceboard.event.server.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
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
	private static final Logger logger = Logger.getLogger(ClearDataHandler.class);
	
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
	
	@Scheduled(fixedDelay=300000)
	public void checkNumberClient() {
	    Runtime runtime = Runtime.getRuntime();
	    runtime.gc();
		
		double usedMemory = ((double)(runtime.totalMemory()- runtime.freeMemory()))/1024/1024;
		List<ClientConnection> allClient = clientRoomManager.getAllClient() ;
		logger.info("Number of  client: " + allClient.size());
		logger.info("Memory usage in Mbyte: " + usedMemory);
	}

	@Scheduled(cron = "0 1 0 * * MON-FRI")
	public void reset() {
		isClearData = true; 
	}

	public void clearIfNeed() {	
		if (isClearData){
			removeStockInFloorCode(FloorCode.HNX.getCode());
			removeStockInFloorCode(FloorCode.HOSE.getCode());
			removeStockInFloorCode(FloorCode.UPCOM.getCode());
			
			memory.remove("CeilingFloor", "ALL");

			memory.remove("PutThroughTransaction",  FloorCode.HOSE.getCode());
			memory.remove("PutThroughTransaction",  FloorCode.HNX.getCode());
			
			memory.remove("PutThrough",   FloorCode.HOSE.getCode());
			memory.remove("PutThrough",   FloorCode.HNX.getCode());

			removeMarket(FloorCode.HNX.getCode());
			removeMarket(FloorCode.HOSE.getCode());
			removeMarket(FloorCode.UPCOM.getCode());
			removeMarket(FloorCode.HNX30.getCode());
			removeMarket(FloorCode.VN30.getCode());

			isClearData = false;
			
			String data= parser.buildReturnJsonStockAsString("RESET", "true");
			List<ClientConnection> clients = clientRoomManager.getAllClient();
			clients.forEach((client) -> client.send(data));
		}

	}
	
	private void removeStockInFloorCode(String floorCode) {
		List<String> stocks = stockRoomManager.getStocksByRoom(floorCode);
		stocks.forEach((stock) -> {
			memory.remove("STOCK", stock);
			memory.remove("STOCK_COMPRESSION", stock);
			memory.remove("TRANSACTION", stock);
		});
	}
	private void removeMarket(String floorCode) {
		memory.remove("MARKET", floorCode);
		memory.remove("MARKET_COMPRESSION", floorCode);
		memory.remove("ALL_MARKET", floorCode);
	}

	@Override
	public void handle(Object source) {
		if (isClearData) clearIfNeed();
	}
}
