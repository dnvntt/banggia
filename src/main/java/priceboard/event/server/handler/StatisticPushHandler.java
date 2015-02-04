package priceboard.event.server.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.pusher.CeilingFloorPusher;

 
@Component
@EventHandlerApplyFor(priority = 7,values = {"MARKET_STATISTIC_PUSH"})
public class StatisticPushHandler implements EventHandler {

	private CeilingFloorPusher statisticPusher;
	
	@Autowired
	public StatisticPushHandler(CeilingFloorPusher statisticPusher) {
		this.statisticPusher = statisticPusher;
	}
	
	@Override
	public void handle(Object source) {
		statisticPusher.push(source);
	}
}
