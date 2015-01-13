package priceboard.reloaddata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import priceboard.reloaddata.elasticsearch.ElasticSearchClient;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Transaction;

@Component
@DependsOn("bussinessDateLoader")
public class TransactionLoader {

	private ElasticSearchClient elasticSearchClient;

	private InMemory memory;

	@Autowired
	public TransactionLoader(ElasticSearchClient elasticSearchClient,
			InMemory memory) {
		this.elasticSearchClient = elasticSearchClient;
		this.memory = memory;
	}

	@PostConstruct
	public void load() throws Exception {
		Map<String, String> searchCondition = new HashMap<String, String>();
		putBusinessDateToCondition(searchCondition);
		loadSnapshotTransactionToMemory(searchCondition);

	}

	private void putBusinessDateToCondition(Map<String, String> searchCondition) {
		searchCondition.clear();
		Object businessDate = memory.get("businessdate", "businessdate");
		if (businessDate != null) {
			searchCondition.put("tradingDate", (String) businessDate);
		}
	}

	private void loadSnapshotTransactionToMemory(
			Map<String, String> searchCondition) throws Exception {
		List<Transaction> transactionList = (List<Transaction>) elasticSearchClient
				.getDataByIndex("transaction", "transaction",
						Transaction.class, searchCondition);
		transactionList
				.forEach((transaction) -> {
					List<Transaction> transactions = (List<Transaction>) memory
							.get("TRANSACTION", transaction.getSymbol());
					if (transactions == null) {
						transactions = new ArrayList<>();
						memory.put("TRANSACTION", transaction.getSymbol(),
								transactions);
					}
					transactions.add(transaction);
				});
	}

}
