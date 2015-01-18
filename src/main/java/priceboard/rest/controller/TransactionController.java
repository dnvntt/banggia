package priceboard.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import priceboard.json.JsonParser;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.Transaction;

@Controller
@RequestMapping("/priceservice/transaction")
public class TransactionController {

	@Autowired
	private InMemory memory;

	@Autowired
	private JsonParser jsonParser;

	@Autowired
	public TransactionController(InMemory memory, JsonParser jsonParser) {
		this.memory = memory;
		this.jsonParser = jsonParser;
	}

	@RequestMapping(value = "/history/q=codes:{code}", method = RequestMethod.GET)
	public @ResponseBody List<Transaction> getTransactionHistory(
			@PathVariable String code, ModelMap modelMap) {
		List<Transaction> transactionHistoryByCode = new ArrayList<Transaction>();
		if (isEmpty(code))
			return transactionHistoryByCode;

		transactionHistoryByCode = (List<Transaction>) memory.get("TRANSACTION", code);
		if (transactionHistoryByCode == null)
			return new ArrayList<Transaction>();

		return transactionHistoryByCode;
	}

	private boolean isEmpty(String codes) {
		return codes == null || codes.trim().length() == 0;
	}

}
