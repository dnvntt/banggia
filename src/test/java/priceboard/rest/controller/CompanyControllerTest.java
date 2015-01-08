package priceboard.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;

import priceboard.json.JsonParser;
import priceboard.reloaddata.Company;
import vn.com.vndirect.lib.commonlib.memory.InMemory;

public class CompanyControllerTest {

	private	InMemory memory;
	private CompanyController companyController;
	
	@Before
	public void setup() {
		memory = new InMemory();
		companyController = new CompanyController(memory, new JsonParser());
	}
	
	@Test
	public void testCompanyControllerReturnSnapshotData() {
		ArrayList<Company> companyList = new ArrayList<Company>();
		Company company1 = new Company();
		company1.setCode("VND");
		company1.setFloorCode("02");
		company1.setCompanyName("CTCP CK VNDIRECT");
		companyList.add(company1);
		Company company2 = new Company();
		company2.setCode("SSI");
		company2.setFloorCode("10");
		company2.setCompanyName("CTCP CK SSI");
		companyList.add(company2);
		memory.put("COMPANY_LIST", "COMPANY_LIST", companyList);
		Object resultInJsonp = companyController.getCompany(new ModelMap());
		Assert.assertEquals(2, ((List) resultInJsonp).size());
		Assert.assertTrue(((List) resultInJsonp).contains(company1));
		Assert.assertTrue(((List) resultInJsonp).contains(company2));
	}
	
	@Test
	public void testCompanyControllerReturnNoSnapshotData() {
		Object resultInJsonp = companyController.getCompany(new ModelMap());
		String expectedJsonp = "jsonp()";
		Assert.assertNull(resultInJsonp);
	}
}
