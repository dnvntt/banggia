package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import priceboard.event.EventHandler;
import priceboard.reloaddata.Company;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.SecInfo;


@Component
@EventHandlerApplyFor(priority = 3,values = {"STOCK"})
public class StockUpdateCompanyNameHandler implements EventHandler {

	private InMemory memory; 
	
	private MarketSessionChecker marketSessionChecker;
	
	@Autowired
	public StockUpdateCompanyNameHandler(InMemory memory, MarketSessionChecker marketSessionChecker) {
		this.memory = memory;
		this.marketSessionChecker = marketSessionChecker;
	}

	@Override
	public void handle(Object source) {
		if (marketSessionChecker.isClosedSession(((SecInfo) source).getFloorCode())) {
			Company company = new Company();
			company.setCode(((SecInfo) source).getCode());
			company.setCompanyName(((SecInfo) source).getCompanyName());
			company.setFloorCode(((SecInfo) source).getFloorCode());
			updateCompanyName(company);
		}
	}

	private void updateCompanyName(Company company) {
		Object companyList = memory.get("COMPANY_LIST", "COMPANY_LIST");
		if (companyList == null) {
			companyList = new ArrayList<Company>();
			memory.put("COMPANY_LIST", "COMPANY_LIST", companyList);
		}
		if (!((List<Company>) companyList).contains(company)) {
			((List<Company>) companyList).add(company);
		}
	}

}
