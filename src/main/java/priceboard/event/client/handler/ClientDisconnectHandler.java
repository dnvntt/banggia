package priceboard.event.client.handler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerApplyFor;
import priceboard.room.ClientRoomManager;

@Component
@EventHandlerApplyFor(priority = 1,values = {"DISCONNECT"})
public class ClientDisconnectHandler implements EventHandler {

	private static final Logger logger = Logger.getLogger(ClientDisconnectHandler.class);
	
	private ClientRoomManager clientRoomManager;
	
	@Autowired
	public ClientDisconnectHandler(ClientRoomManager roomManager) {
		this.clientRoomManager = roomManager;
	}
	
	@Override
	public void handle(Object source) {
		logger.info("Disconnect client: " + source);
		clientRoomManager.removeClientFromAllRoom((ClientConnection) source);
	}

}
