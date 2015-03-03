package priceboard.event.client.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerApplyFor;
import priceboard.room.ClientRoomManager;

@Component
@EventHandlerApplyFor(priority = 1,values = {"DISCONNECT"})
public class ClientDisconnectHandler implements EventHandler {

	
	private ClientRoomManager clientRoomManager;
	
	@Autowired
	public ClientDisconnectHandler(ClientRoomManager roomManager) {
		this.clientRoomManager = roomManager;
	}
	
	@Override
	public void handle(Object source) {
		clientRoomManager.removeClient((ClientConnection) source);
	}

}
