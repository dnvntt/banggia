package priceboard.reloaddata;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

@Component
public class CategoryNameLoader {

	private ElasticSearchClient elasticSearchClient;

	private InMemory memory;

	@Autowired
	public CategoryNameLoader(ElasticSearchClient elasticSearchClient, InMemory memory) {
		this.elasticSearchClient = elasticSearchClient;
		this.memory = memory;
	}

	@PostConstruct
	public void load() throws Exception {
		List<Category> secInfoList = elasticSearchClient.getDataByIndex("category", "category", Category.class, null);
		memory.put("CATEGORY_LIST", "CATEGORY_LIST", secInfoList);
	}

}
