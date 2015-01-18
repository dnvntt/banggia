package priceboard.pusher;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.json.JsonParser;
import priceboard.room.ClientRoomManager;
import priceboard.room.StockRoomManager;
import vn.com.vndirect.priceservice.datamodel.Transaction;

@Component
public class TransactionPusher implements Pusher {

	private static final Logger logger = Logger
			.getLogger(TransactionPusher.class);

	private ClientRoomManager clientRoomManager;

	private StockRoomManager stockRoomManager;

	private JsonParser parser;

	@Autowired
	public TransactionPusher(ClientRoomManager clientRoomManager,
			StockRoomManager stockRoomManager, JsonParser parser) {
		this.clientRoomManager = clientRoomManager;
		this.stockRoomManager = stockRoomManager;
		this.parser = parser;
	}

	@Override
	public void push(Object source) {
		String data = getCompressionData((Transaction) source);
		String code = ((Transaction) source).getSymbol();
		pushToAllClientInThisStockRoom(code, data);
		pushToAllClientInAllRoomsOfStock(code, data);
	}

	private String getCompressionData(Transaction source) {
		return parser.buildReturnJsonStockAsString("TRANSACTION", source);
	}

	private void pushToAllClientInThisStockRoom(String code, String data) {
		pushAllClientInRoom(code, data);
	}

	private void pushToAllClientInAllRoomsOfStock(String code, String data) {
		List<String> rooms = stockRoomManager.getRoomOfStock(code);
		rooms.forEach((room) -> pushAllClientInRoom(room, data));
	}

	private void pushAllClientInRoom(String room, String data) {
		List<ClientConnection> clients = clientRoomManager
				.getClientInRoom(room);
		logger.info("Push to all client: " + clients);
		clients.parallelStream().forEach((client) -> {
			logger.info("Push to client: " + client + " with data: " + data);
			client.send(data);
		});
	}

	@Override
	public void push(ClientConnection client, Object source) {
		String data = getCompressionData((Transaction) source);
		System.out.println("Inside TransactionPusher push function with data: "+ data);
		client.send(data);
	}

}
