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
import vn.com.vndirect.priceservice.datamodel.FloorCode;
import vn.com.vndirect.priceservice.datamodel.PutThrough;

@Component
@DependsOn("bussinessDateLoader")
public class AdorderLoader {
	private ElasticSearchClient elasticSearchClient;
	
	private InMemory memory;
	
	@Autowired
	public AdorderLoader(ElasticSearchClient elasticSearchClient, InMemory memory) {
		this.elasticSearchClient = elasticSearchClient;
		this.memory = memory;
	}
	@PostConstruct
	public void load() throws Exception {
		Map<String, String> searchCondition = new HashMap<String, String>();
		putBusinessDateToCondition(searchCondition);
		loadSnapshotAdorderToMemory(searchCondition,FloorCode.HOSE.getCode());
		loadSnapshotAdorderToMemory(searchCondition,FloorCode.HNX.getCode());
	}

	private void putBusinessDateToCondition(Map<String, String> searchCondition) {
		searchCondition.clear();
		Object businessDate = memory.get("businessdate", "businessdate");
		if (businessDate != null) {
			searchCondition.put("tradingDate", (String) businessDate);
		}
	}
	
	private void loadSnapshotAdorderToMemory(Map<String, String> searchCondition, String floorCode) throws Exception {
		searchCondition.put("floorCode", floorCode);
		List<PutThrough> AdorderList = (List<PutThrough>) elasticSearchClient.getDataByIndex("putthrough", "putthrough", PutThrough.class, searchCondition);
		memory.put("PutThrough",floorCode, AdorderList);
	}
	 
	
 	 
	
}
