package priceboard.event.server.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.stock.compress.Mashaller;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Market;

@Component
@EventHandlerApplyFor(priority = 3, values = { "MARKET" })
public class MarketMemoryHandler implements EventHandler {

	private InMemory memory;
	private Mashaller mashaller;

	@Autowired
	public MarketMemoryHandler(InMemory memory, Mashaller mashaller) {
		this.memory = memory;
		this.mashaller = mashaller;
	}

	@Override
	public void handle(Object source) {
		Market market = (Market) source;
		String key = market.getFloorCode();
		memory.put("MARKET", key, source);
		String compression = mashaller.compress(source);
		memory.put("MARKET_COMPRESSION", key, compression);

		List<Market> marketList = (ArrayList<Market>) memory.get("ALL_MARKET",key);
		if (marketList == null || marketList.size()<1) {
			marketList = new ArrayList<Market>();
			memory.put("ALL_MARKET", key, marketList);
			
			double curent_index =market.getMarketIndex();
			String time_current = market.getTradingTime();
			
			if (time_current == null || curent_index <1 || (time_current.substring(0, 2)).equals("15") ) {
				Calendar cal = Calendar.getInstance();
		    	cal.getTime();
		    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				market.setTradingTime(sdf.format(cal.getTime()));
			}
			marketList.add(market);
		} else {
			Market lastRecord = marketList.get(marketList.size() - 1);
			String time_current = market.getTradingTime();
			double curent_index = market.getMarketIndex();
			if (time_current != null && curent_index> 1
					&& time_diff(lastRecord.getTradingTime(), time_current) >= 1)
				marketList.add(market);
		}
	}

	public int time_diff(String time1, String time2) {
		if (Integer.parseInt(time1.substring(0, 2)) > Integer.parseInt(time2.substring(0, 2)))
			return -1;
				
		if (time1.substring(0, 2).equals(time2.substring(0, 2)))
			return (Integer.parseInt(time2.substring(3, 5)) - Integer
					.parseInt(time1.substring(3, 5)));
		else
			return 1;
	}

}
