package priceboard.event.client.handler;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.client.ClientConnection;
import priceboard.event.EventHandler;
import priceboard.room.ClientRoomManager;

@Component
public class ClientDisconnectHandler implements EventHandler {

	private ClientRoomManager clientRoomManager;
	
	private ClientEventTypeMapping clientEventTypeMapping;
	
	@Autowired
	public ClientDisconnectHandler(ClientRoomManager roomManager, ClientEventTypeMapping clientEventTypeMapping) {
		this.clientRoomManager = roomManager;
		this.clientEventTypeMapping = clientEventTypeMapping;
	}
	
	@PostConstruct
	public void registerHander() {
		clientEventTypeMapping.registerHandler(this, "DISCONNECT");
	}

	@Override
	public void handle(Object source) {
		clientRoomManager.removeClientFromAllRoom((ClientConnection) source);
	}

}
