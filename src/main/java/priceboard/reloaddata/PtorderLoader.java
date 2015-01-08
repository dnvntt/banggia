package priceboard.reloaddata;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;

@Component
@DependsOn("bussinessDateLoader")
public class PtorderLoader {
	private ElasticSearchClient elasticSearchClient;
	
	private InMemory memory;
	
	@Autowired
	public PtorderLoader(ElasticSearchClient elasticSearchClient, InMemory memory) {
		this.elasticSearchClient = elasticSearchClient;
		this.memory = memory;
	}
	@PostConstruct
	public void load() throws Exception {
		Map<String, String> searchCondition = new HashMap<String, String>();
		putBusinessDateToCondition(searchCondition);
		loadSnapshotPtorderToMemory(searchCondition);
//		loadPtorderToMemory(searchCondition,FloorCode.HOSE.getCode());
//		loadPtorderToMemory(searchCondition,FloorCode.HNX.getCode());
//		loadPtorderToMemory(searchCondition,FloorCode.VN30.getCode());
//		loadPtorderToMemory(searchCondition,FloorCode.HNX30.getCode());
//		loadPtorderToMemory(searchCondition,FloorCode.UPCOM.getCode());
	}

	private void putBusinessDateToCondition(Map<String, String> searchCondition) {
		searchCondition.clear();
		Object businessDate = memory.get("businessdate", "businessdate");
		if (businessDate != null) {
			searchCondition.put("tradingDate", (String) businessDate);
		}
	}
	
	private void loadSnapshotPtorderToMemory(Map<String, String> searchCondition) throws Exception {
		//DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		//Date date = new Date();	
		//searchCondition.put("tradingDate", dateFormat.format(date));
		List<PutThroughTransaction> PtorderList = (List<PutThroughTransaction>) elasticSearchClient.getDataByIndex("putthroughtransaction", "putthroughtransaction", PutThroughTransaction.class, searchCondition);

		memory.put("PutThroughTransaction","", PtorderList);
	}
//	private void loadPtorderToMemory(Map<String, String> searchCondition, String floorCode) throws Exception {
//		searchCondition.put("floorCode", floorCode);
//		List<PutThroughTransaction> PtorderList = (List<PutThroughTransaction>) elasticSearchClient.getDataByIndex("putthroughtransaction", "putthroughtransaction", PutThroughTransaction.class, searchCondition);
//		memory.put("ALL_PutThroughTransaction", floorCode, PtorderList);
//	}
//	
 	 
	
}
