package priceboard.reloaddata;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

@Component
public class BussinessDateLoader {

	private ElasticSearchClient elasticSearchClient;
	
	private InMemory memory;
	
	@Autowired
	public BussinessDateLoader(ElasticSearchClient elasticSearchClient, InMemory memory) {
		this.elasticSearchClient = elasticSearchClient;
		this.memory = memory;
	}

	@PostConstruct
	public void load() throws Exception {
		List<BusinessDate> result = elasticSearchClient.getDataByIndex("businessdate", "businessdate", BusinessDate.class, null);
		if (result.size() > 0) {
			memory.put("businessdate", "businessdate", result.get(0).getBusinessDate());
		}
	}

}
