package priceboard.pusher;

import priceboard.client.ClientConnection;


public interface Pusher {
	void push(Object source);
	
	void push(ClientConnection client, Object source);
}
