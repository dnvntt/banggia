package priceboard.reloaddata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;

@Component
@DependsOn("bussinessDateLoader")
public class PtorderLoader {
	private ElasticSearchClient elasticSearchClient;

	private InMemory memory;

	@Autowired
	public PtorderLoader(ElasticSearchClient elasticSearchClient,
			InMemory memory) {
		this.elasticSearchClient = elasticSearchClient;
		this.memory = memory;
	}

	@PostConstruct
	public void load() throws Exception {
		Map<String, String> searchCondition = new HashMap<String, String>();
		putBusinessDateToCondition(searchCondition);
		loadSnapshotPtorderToMemory(searchCondition);
	}

	private void putBusinessDateToCondition(Map<String, String> searchCondition) {
		searchCondition.clear();
		Object businessDate = memory.get("businessdate", "businessdate");
		if (businessDate != null) {
			searchCondition.put("tradingDate", (String) businessDate);
		}
	}

	private void loadSnapshotPtorderToMemory(Map<String, String> searchCondition)
			throws Exception {
		List<PutThroughTransaction> PtorderList = (List<PutThroughTransaction>) elasticSearchClient
				.getDataByIndex("putthroughtransaction",
						"putthroughtransaction", PutThroughTransaction.class,
						searchCondition);

		memory.put("PutThroughTransaction", "", PtorderList);
	}

}
