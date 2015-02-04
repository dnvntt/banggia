package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.util.InstanceChecker;
import vn.com.vndirect.datafeed.util.MarketStatisMessage;
import vn.com.vndirect.lib.commonlib.memory.InMemory;


@Component
@EventHandlerApplyFor(priority = 3, values = { "MARKET_STATISTIC" })
public class StatisticMemoryHandler implements EventHandler {

	private InMemory memory;
	@Autowired
	public StatisticMemoryHandler(InMemory memory) {
		this.memory = memory;
	}

	@Override
	public void handle(Object source) {
		if (InstanceChecker.isMarketStatisMessage(source) ){
			List<MarketStatisMessage> marketStatisticList = (List<MarketStatisMessage>) memory.get("CeilingFloor", "ALL");
			
			MarketStatisMessage marketStatistic = (MarketStatisMessage) source;
			
			if (marketStatisticList == null) {
				marketStatisticList = new ArrayList<MarketStatisMessage>();
				marketStatisticList.add(marketStatistic);
				memory.put("CeilingFloor","ALL", marketStatisticList);
				return;
			}
			 
			for (int i=0; i< marketStatisticList.size();i++)
			{
				MarketStatisMessage element= marketStatisticList.get(i);
				if(element.getFloor().equals(marketStatistic.getFloor()) && element.getType().equals(marketStatistic.getType()))
					marketStatisticList.remove(i);
			}
			marketStatisticList.add(marketStatistic);
		} 
	}
}

