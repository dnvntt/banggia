package priceboard.event.client.handler;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerApplyFor;
import priceboard.json.JsonParser;
import priceboard.pusher.CeilingFloorPusher;
import priceboard.pusher.StockPusher;
import priceboard.room.ClientRoomManager;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

import com.fasterxml.jackson.databind.JsonNode;

@Component
@EventHandlerApplyFor(values = {"registConsumer"})
public class StockRegisterHandler implements EventHandler {

	private ClientRoomManager clientRoomManager;
	private static final Logger logger = Logger.getLogger(StockRegisterHandler.class);
	private StockPusher pusher;
	private CeilingFloorPusher ceilingFloorPusher;
	private JsonParser parser;
	
	@Autowired
	public StockRegisterHandler(ClientRoomManager clientRoomManager, StockPusher pusher, CeilingFloorPusher ceilingFloorPusher, JsonParser parser) {
		this.clientRoomManager = clientRoomManager;
		this.parser = parser;
		this.pusher = pusher;
		this.ceilingFloorPusher =ceilingFloorPusher;
	}

	@Override
	public void handle(Object source) {
		Map<String, Object> map = (Map<String, Object>) source;
		ClientConnection client = (ClientConnection) map.get("CLIENT");
		JsonNode dataNode = (JsonNode) map.get("data");
		List<String> codes = parser.parseDataCodes(dataNode);
		logger.info("check codes:" +codes) ;
		if (codes == null || codes.isEmpty()) 
			{
				logger.info("check codes ==null inside  handle of  registConsumer ");
				return;
			}
		
		addClientToRoom(codes, client);
		
		JsonNode jsonNameNode = dataNode.at("/data/params/name");

//		if (jsonNameNode == null) {
//			return;
//		}
		
		String nameMessage = (jsonNameNode.asText()).trim();
		logger.info("nameMessage of registConsumer:" +nameMessage+":");
		if (!intervalRegister(dataNode)) {
			logger.info("inside handle of registConsumer  ");
			if(nameMessage.equals("STOCK")) {
				logger.info("inside handle of registConsumer STOCK ");
				pushStockToClient(codes, client);
			}
			//TODO add function to process ceiling floor count
			if(nameMessage.equals("CEILING_FLOOR_COUNT")) 
			{
				logger.info("inside handle of registConsumer CEILING_FLOOR_COUNT  ");
				//pushStatisticToClient(client) ;
			}
		}
	}

	private boolean intervalRegister(JsonNode dataNode) {
		return parser.parseDataRegistInterval(dataNode);
	}

	private void addClientToRoom(List<String> codes, ClientConnection client) {
		codes.forEach((room) -> {
			clientRoomManager.addClientToRoom(room, client);
		});
	}
	
	private void pushStockToClient(List<String> codes, ClientConnection client) {
		codes.forEach((code) -> {	
			logger.info("Inside send pushStockToClient" + code);
			pusher.push(client, new SecInfo(){{setCode(code);}});   
		});
	}
	
	private void pushStatisticToClient(ClientConnection client) {
		logger.info("Inside send pushStatisticToClient");
		ceilingFloorPusher.push(client); 
	}
}
