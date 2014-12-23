package priceboard.reloaddata.elasticsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import priceboard.json.JsonParser;
import priceboard.reloaddata.BusinessDate;
import priceboard.reloaddata.Category;
import priceboard.reloaddata.Company;
import vn.com.vndirect.priceservice.datamodel.Market;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

@Component
public class ElasticSearchClient {
	
	private static final int PAGING = 100;

	@Value("${elastic.address}") 
	private String elasticAddress = "10.25.1.60";
	
	@Value("${elastic.port}") 
	private int elasticPort = 9300;
	
	private JsonParser parser;

	private Settings setting;
	
	private Client client;
	
	@Autowired
	public ElasticSearchClient(JsonParser parser) {
		this.parser = parser;
	}
	
	@PostConstruct
	public void initClient() {
		setting = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch").build();
		client = new TransportClient(setting).addTransportAddress(new InetSocketTransportAddress(elasticAddress, elasticPort));
	}
	
	public <T> List<T> getDataByIndex(String index, String type, Class<T> clazz, Map<String, String> conditions) throws Exception {
		List<T> resultList = new ArrayList<T>();
		long total = count(index, type);
		
		if (total == 0) return resultList;
		SearchRequestBuilder builder = client.prepareSearch(index).setTypes(type);
		
		
		if (conditions != null && conditions.size() > 0) {
			for(Entry<String, String> entry : conditions.entrySet()) {
				builder.setQuery(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
			}
		}
		
		for(int i = 0; i < total; i+= PAGING) {
			SearchResponse response = builder.setSize(PAGING).setFrom(i).execute().actionGet();
			
			for(SearchHit hit : response.getHits().getHits()) {
				resultList.add(convertJsonToObject(clazz, hit.getSourceAsString()));
			}
		}
		return resultList;
	}
	
	private <T> T convertJsonToObject(Class<T> clazz, String sourceAsString) {
		return (T) parser.stringToObject(sourceAsString, clazz);
	}

	public long count(String index, String type) {
		CountResponse response = client.prepareCount(index).setTypes(type).execute().actionGet();
		return response.getCount();
	}
	
	public static void main(String[] args) throws Exception {
		ElasticSearchClient client = new ElasticSearchClient(new JsonParser());
		client.initClient();
		client.getDataByIndex("secinfo", "snapshot", SecInfo.class, new HashMap<String, String>() {{put("tradingDate", "20141209");}});
		List<BusinessDate> dateList = client.getDataByIndex("businessdate", "businessdate", BusinessDate.class, null);
		client.getDataByIndex("company", "company", Company.class, null);
		List<Category> categoryList = client.getDataByIndex("category", "category", Category.class, null);
		
		System.out.println("BusinessDate: " + dateList.get(0).getBusinessDate());
		System.out.println(categoryList.get(0).getCategoryName());
		System.out.println(categoryList.get(0).getCodeList());
		
		/*List<Market> marketList = client.getDataByIndex("market", "market", Market.class, new HashMap<String, String>() {{put("tradingDate", "20141209"); put("floorCode", "02");}});
		
		System.out.println(marketList.get(0).getFloorCode());
		System.out.println(marketList.get(0).getTradingDate());
		System.out.println(marketList.get(0).getTradingTime());
		System.out.println(marketList.get(0).getMarketIndex());
		*/
		List<Market> marketList = client.getDataByIndex("market", "market", Market.class, new HashMap<String, String>() {{put("tradingDate", "20141209"); put("floorCode", "02");}});
		System.out.println(marketList.get(0).getFloorCode());
		System.out.println(marketList.get(0).getTradingDate());
		System.out.println(marketList.get(0).getTradingTime());
	}
}
