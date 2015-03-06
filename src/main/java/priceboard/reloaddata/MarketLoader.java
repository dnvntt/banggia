package priceboard.reloaddata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.FloorCode;
import vn.com.vndirect.priceservice.datamodel.Market;

@Component
@DependsOn("bussinessDateLoader")
public class MarketLoader {

	private ElasticSearchClient elasticSearchClient;

	private InMemory memory;

	@Autowired
	public MarketLoader(ElasticSearchClient elasticSearchClient, InMemory memory) {
		this.elasticSearchClient = elasticSearchClient;
		this.memory = memory;
	}

	@PostConstruct
	public void load() throws Exception {
		Map<String, String> searchCondition = new HashMap<String, String>();
		putBusinessDateToCondition(searchCondition);
		loadSnapshotMarketToMemory(searchCondition);

		loadMarketToMemory(searchCondition, FloorCode.HOSE.getCode());
		loadMarketToMemory(searchCondition, FloorCode.HNX.getCode());
		loadMarketToMemory(searchCondition, FloorCode.VN30.getCode());
		loadMarketToMemory(searchCondition, FloorCode.HNX30.getCode());
		loadMarketToMemory(searchCondition, FloorCode.UPCOM.getCode());
	}

	private void putBusinessDateToCondition(Map<String, String> searchCondition) {
		searchCondition.clear();
		Object businessDate = memory.get("businessdate", "businessdate");
		if (businessDate != null) {
			searchCondition.put("tradingDate", (String) businessDate);
		}
	}

	private void loadSnapshotMarketToMemory(Map<String, String> searchCondition)
			throws Exception {
		List<Market> marketList = (List<Market>) elasticSearchClient
				.getDataByIndex("market", "snapshot", Market.class,
						searchCondition);
		marketList.forEach((market) -> {
			memory.put("MARKET", market.getFloorCode(), market);
		});
	}

	private void loadMarketToMemory(Map<String, String> searchCondition,
			String floorCode) throws Exception {
		searchCondition.put("floorCode", floorCode);
		List<Market> marketList = (List<Market>) elasticSearchClient.getDataByIndex("market", "market", Market.class,searchCondition);
		
		List<Market> incorrectMarketList = new ArrayList<Market>();
		
		for (Market market : marketList) {
			if ( market.getTradingTime().substring(0, 2).equals("15")) 
				incorrectMarketList.add(market);
		}
		
		marketList.removeAll(incorrectMarketList);
		
		Collections.sort(marketList, new Comparator<Market>() {
			@Override
			public int compare(Market o1, Market o2) {
				return o1.getTradingTime().compareTo(o2.getTradingTime());
			}
		});
		memory.put("ALL_MARKET", floorCode, marketList);
	}

}
