package priceboard.event.server.handler;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import priceboard.reloaddata.Company;
import vn.com.vndirect.lib.commonlib.memory.InMemory;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

public class StockUpdateCompanyNameHandlerTest {
	
	private StockUpdateCompanyNameHandler stockHandler;
	
	private MarketSessionChecker marketSessionChecker;

	private InMemory memory;
	@Before
	public void setUp() {
		marketSessionChecker = Mockito.mock(MarketSessionChecker.class);
		memory = new InMemory();
		stockHandler = new StockUpdateCompanyNameHandler(memory, marketSessionChecker);
		memory.put("COMPANY_LIST", "COMPANY_LIST", new ArrayList<Company>());
	}

	
	@Test
	public void testStockUpdateCompanyNameHandlerOnlyUpdateCompanyNameWhenMarketClose() {
		Mockito.when(marketSessionChecker.isClosedSession("02")).thenReturn(true);
		SecInfo secInfo = new SecInfo();
		secInfo.setCode("VND");
		secInfo.setFloorCode("02");
		secInfo.setCompanyName("CT CPCK VNDIRECT");
		stockHandler.handle(secInfo);

		Mockito.when(marketSessionChecker.isClosedSession("10")).thenReturn(true);
		secInfo = new SecInfo();
		secInfo.setCode("SSI");
		secInfo.setFloorCode("10");
		secInfo.setCompanyName("CT CPCK SSI");
		stockHandler.handle(secInfo);
	
		List<Company> companyList = (List<Company>) memory.get("COMPANY_LIST", "COMPANY_LIST");
		Assert.assertEquals(2, companyList.size());
		Assert.assertEquals("VND", companyList.get(0).getCode());
		Assert.assertEquals("CT CPCK VNDIRECT", companyList.get(0).getCompanyName());
		Assert.assertEquals("SSI", companyList.get(1).getCode());
		Assert.assertEquals("CT CPCK SSI", companyList.get(1).getCompanyName());
	}
	
	@Test
	public void testStockUpdateCompanyNameHandlerDontUpdateCompanyNameWhenMarketOpen() {
		Mockito.when(marketSessionChecker.isClosedSession("02")).thenReturn(false);
		SecInfo secInfo = new SecInfo();
		secInfo.setCode("VND");
		secInfo.setFloorCode("02");
		secInfo.setCompanyName("CT CPCK VNDIRECT");
		stockHandler.handle(secInfo);

		Mockito.when(marketSessionChecker.isClosedSession("10")).thenReturn(false);
		secInfo = new SecInfo();
		secInfo.setCode("SSI");
		secInfo.setFloorCode("10");
		secInfo.setCompanyName("CT CPCK SSI");
		stockHandler.handle(secInfo);
	
		Object companyList = memory.get("COMPANY_LIST", "COMPANY_LIST");
		Assert.assertEquals(0, ((List)companyList).size());
	}
	
	@Test
	public void testStockUpdateCompanyNameWhenHandleCompanyObject() {
		Company company = new Company();
		company.setCode("VND");
		company.setFloorCode("02");
		company.setCompanyName("CT CPCK VNDIRECT");
		stockHandler.handle(company);
		
		company = new Company();
		company.setCode("SSI");
		company.setFloorCode("10");
		company.setCompanyName("CT CPCK SSI");
		stockHandler.handle(company);
		
		List<Company> companyList = (List<Company>) memory.get("COMPANY_LIST", "COMPANY_LIST");
		Assert.assertEquals(2, companyList.size());
		Assert.assertEquals("VND", companyList.get(0).getCode());
		Assert.assertEquals("CT CPCK VNDIRECT", companyList.get(0).getCompanyName());
		Assert.assertEquals("SSI", companyList.get(1).getCode());
		Assert.assertEquals("CT CPCK SSI", companyList.get(1).getCompanyName());
	}
	
}
