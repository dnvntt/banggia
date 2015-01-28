package priceboard.event.client.handler;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerApplyFor;
import priceboard.room.ClientRoomManager;

@Component
@EventHandlerApplyFor(priority = 2,values = {"stopConsume"})
public class StopConsumeHandler implements EventHandler {

	private static final Logger logger = Logger.getLogger(StopConsumeHandler.class);
	
	private ClientRoomManager clientRoomManager;
	
	@Autowired
	public StopConsumeHandler(ClientRoomManager roomManager) {
		this.clientRoomManager = roomManager;
	}
	
	@Override
	public void handle(Object source) {
		logger.info("stopConsume from  client: " + source);

		Map<String, Object> map = (Map<String, Object>) source;
		ClientConnection client = (ClientConnection) map.get("CLIENT");
		clientRoomManager.removeClientFromAllRoom(client);
	}

}