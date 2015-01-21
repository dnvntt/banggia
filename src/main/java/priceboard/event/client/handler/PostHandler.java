package priceboard.event.client.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.event.EventHandler;
import priceboard.event.client.model.DataTransaction;
import priceboard.event.server.handler.EventHandlerApplyFor;
import priceboard.json.JsonParser;
import priceboard.pusher.TransactionPusher;
import priceboard.room.ClientRoomManager;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Transaction;

import com.fasterxml.jackson.databind.JsonNode;

@Component
@EventHandlerApplyFor(values = { "post" })
public class PostHandler implements EventHandler {

	private JsonParser parser;
	private InMemory memory;
 
	@Autowired
	public PostHandler(ClientRoomManager roomManager, JsonParser parser,
			InMemory memory, TransactionPusher transactionPusher) { 
		this.parser = parser;
		this.memory = memory;
	}

	@Override
	public void handle(Object source) {
		Map<String, Object> map = (Map<String, Object>) source;
		ClientConnection client = (ClientConnection) map.get("CLIENT");
		JsonNode dataNode = (JsonNode) map.get("data");

		JsonNode jsonSymbolNode = dataNode.at("/data/params/symbol");

		if (jsonSymbolNode == null) {
			return;
		}
		String symbol = jsonSymbolNode.asText();

		List<Transaction> transactionHistoryByCode = new ArrayList<Transaction>();

		transactionHistoryByCode = (List<Transaction>) memory.get("TRANSACTION", symbol);
		DataTransaction data = new DataTransaction();
		data.setName("TRANSACTION");
		data.setData(transactionHistoryByCode);
			
		String msg_return = parser.buildReturnJsonStockAsString("returnData",data);
		client.send(msg_return);
	}

}