package priceboard.reloaddata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.event.server.handler.EventHandlerFilter;
import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

@Component
@DependsOn("bussinessDateLoader")
public class StockLoader {

	private ElasticSearchClient elasticSearchClient;

	@Autowired
	private List<EventHandler> handlers;

	@Autowired
	private EventHandlerFilter eventHandlerFilter;

	private InMemory memory;
	
	private List<EventHandler> handlersOfStock;

	@Autowired
	public StockLoader(ElasticSearchClient elasticSearchClient, InMemory memory) {
		this.elasticSearchClient = elasticSearchClient;
		this.memory = memory;
	}

	@PostConstruct
	public void init() throws Exception {
		initHandler();
		load();
	}

	private void initHandler() {
		handlersOfStock = eventHandlerFilter.filter(handlers, Arrays.asList("STOCK", "ALL"));
		System.out.println(handlersOfStock);
	}
	
	public void load() throws Exception {
		Object businessDate = memory.get("businessdate", "businessdate");
		Map<String, String> map = new HashMap<String, String>();
		if (businessDate != null) {
			map.put("tradingDate", (String) businessDate);
		}
		List<SecInfo> secInfoList = elasticSearchClient.getDataByIndex("secinfo", "snapshot", SecInfo.class, map);
		secInfoList.forEach((object) -> {
			handlersOfStock.forEach((handler) -> {handler.handle(object);});
		});
	}

	public void setHandlers(List<EventHandler> handlers) {
		this.handlersOfStock = handlers;
	}

}
