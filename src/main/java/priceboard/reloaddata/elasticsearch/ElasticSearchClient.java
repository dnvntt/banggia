package priceboard.reloaddata.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import priceboard.json.JsonParser;

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
		long total = count(index, type, conditions);
		
		if (total == 0) return resultList;
		SearchRequestBuilder builder = client.prepareSearch(index).setTypes(type);
		
		
		addConditionToSearchBuilder(builder, conditions);
		
		
		for(int i = 0; i < total; i+= PAGING) {
			SearchResponse response = builder.setSize(PAGING).setFrom(i).execute().actionGet();
			
			for(SearchHit hit : response.getHits().getHits()) {
				resultList.add(convertJsonToObject(clazz, hit.getSourceAsString()));
			}
		}
		
		System.out.println("Total of " + index + " : " + total);
		return resultList;
	}

	private void addConditionToSearchBuilder(SearchRequestBuilder builder, Map<String, String> conditions) {
		if (conditions != null && conditions.size() > 0) {
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			for(Entry<String, String> entry : conditions.entrySet()) {
				queryBuilder.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
			}
			builder.setQuery(queryBuilder);
			
		}
	}
	
	
	public long count(String index, String type, Map<String, String> conditions) {
		CountRequestBuilder builder = client.prepareCount(index).setTypes(type);
		addConditionToCountBuilder(builder, conditions);
		CountResponse response = builder.execute().actionGet();
		return response.getCount();
	}
	
	
	private void addConditionToCountBuilder(CountRequestBuilder builder, Map<String, String> conditions) {
		if (conditions != null && conditions.size() > 0) {
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			for(Entry<String, String> entry : conditions.entrySet()) {
				queryBuilder.must(QueryBuilders.termQuery(entry.getKey(), entry.getValue()));
			}
			builder.setQuery(queryBuilder);
		}
	}
	
	private <T> T convertJsonToObject(Class<T> clazz, String sourceAsString) {
		return (T) parser.stringToObject(sourceAsString, clazz);
	}

	
	/*public static void main(String[] args) throws Exception {
		ElasticSearchClient client = new ElasticSearchClient(new JsonParser());
		client.initClient();
		client.getDataByIndex("secinfo", "snapshot", SecInfo.class, new HashMap<String, String>() {{put("tradingDate", "20141223");}});
		List<BusinessDate> dateList = client.getDataByIndex("businessdate", "businessdate", BusinessDate.class, null);
		
		
		List<Company> secInfoList = client.getDataByIndex("company", "company", Company.class, null);
		secInfoList.forEach(company -> {
			System.out.println(company.getCode() + " " + company.getCompanyName());
		});
		
		List<Category> categoryList = client.getDataByIndex("category", "category", Category.class, null);
		
		System.out.println("BusinessDate: " + dateList.get(0).getBusinessDate());
		System.out.println(categoryList.get(0).getCategoryName());
		System.out.println(categoryList.get(0).getCodeList());
		
		List<Market> marketList = client.getDataByIndex("market", "market", Market.class, new HashMap<String, String>() {{put("tradingDate", "20141223"); put("floorCode", "02");}});
		System.out.println(marketList.get(0).getFloorCode());
		System.out.println(marketList.get(0).getTradingDate());
		System.out.println(marketList.get(0).getTradingTime());
	}*/
}
