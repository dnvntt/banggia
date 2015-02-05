package priceboard.pusher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.json.JsonParser;
import priceboard.room.ClientRoomManager;
import priceboard.stock.compress.Mashaller;
import vn.com.vndirect.priceservice.datamodel.Transaction;

@Component
public class TransactionPusher implements Pusher {

//	private static final Logger logger = Logger.getLogger(TransactionPusher.class);

	private ClientRoomManager clientRoomManager;
	private Mashaller mashaller;
	private JsonParser parser;

	@Autowired
	public TransactionPusher(ClientRoomManager clientRoomManager,JsonParser parser,Mashaller mashaller) {
		this.clientRoomManager = clientRoomManager;
		this.parser = parser;
		this.mashaller = mashaller;
	}

	@Override
	public void push(Object source) {
		String data = getTransactionData((Transaction) source);
		String code = ((Transaction) source).getSymbol();
		pushToAllClientInThisTransaction(code, data);
		 
	}

	private String getTransactionData(Transaction source) {
		String compression = mashaller.compress(source);
		return parser.buildReturnJsonStockAsString("TRANSACTION", compression);
	}

	private void pushToAllClientInThisTransaction(String room, String data) {
		List<ClientConnection> clients = clientRoomManager.getClientInTransaction(room);
		clients.parallelStream().forEach((client) -> {
			client.send(data);
		});
	}

	@Override
	public void push(ClientConnection client, Object source) {
		String data = getTransactionData((Transaction) source);
		client.send(data);
	}

}
