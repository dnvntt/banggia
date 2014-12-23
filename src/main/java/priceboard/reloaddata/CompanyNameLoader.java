package priceboard.reloaddata;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

@Component
public class CompanyNameLoader {

	private ElasticSearchClient elasticSearchClient;
	
	private InMemory memory;
	

	@Autowired
	public CompanyNameLoader(ElasticSearchClient elasticSearchClient, InMemory memory) {
		this.elasticSearchClient = elasticSearchClient;
		this.memory = memory;
	}

	@PostConstruct
	public void load() throws Exception {
		List<Company> secInfoList = elasticSearchClient.getDataByIndex("company", "company", Company.class, null);
		memory.put("COMPANY_LIST", "COMPANY_LIST", secInfoList);
	}

}
