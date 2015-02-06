package priceboard.pusher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.json.JsonParser;
import priceboard.room.ClientRoomManager;
import priceboard.room.StockRoomManager;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

@Component
public class StockPusher implements Pusher {
	

	private ClientRoomManager clientRoomManager;

	private StockRoomManager stockRoomManager;

	private JsonParser parser;

	private InMemory memory;

	@Autowired
	public StockPusher(ClientRoomManager clientRoomManager,
			StockRoomManager stockRoomManager, JsonParser parser,
			InMemory memory) {
		this.clientRoomManager = clientRoomManager;
		this.stockRoomManager = stockRoomManager;
		this.parser = parser;
		this.memory = memory;
	}

	@Override
	public void push(Object source) {
		String data = getCompressionData((SecInfo) source);
		String code = ((SecInfo) source).getCode();
		pushToAllClientInThisStockRoom(code, data);
		pushToAllClientInAllRoomsOfStock(code, data);
	}

	private String getCompressionData(SecInfo source) {
		String symbol = source.getCode();
		String data = (String) memory.get("STOCK_COMPRESSION", symbol);
		return parser.buildReturnJsonStockAsString("STOCK",data);
	}

	private void pushToAllClientInThisStockRoom(String code, String data) {
		pushAllClientInRoom(code, data);
	}

	private void pushToAllClientInAllRoomsOfStock(String code, String data) {
		List<String> rooms = stockRoomManager.getRoomOfStock(code);
		rooms.forEach((room) -> pushAllClientInRoom(room, data));
	}

	private void pushAllClientInRoom(String room, String data) {
		List<ClientConnection> clients = clientRoomManager.getClientInRoom(room);
		clients.parallelStream().forEach((client) -> {
			client.send(data);	
		});
	}

	@Override
	public void push(ClientConnection client, Object source) {
		String data = getCompressionData((SecInfo) source);
		client.send(data);
	}

}
