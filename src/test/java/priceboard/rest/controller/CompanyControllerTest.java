package priceboard.rest.controller;

import java.util.ArrayList;

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
		Company company = new Company();
		company.setCode("VND");
		company.setFloorCode("02");
		company.setCompanyName("CTCP CK VNDIRECT");
		companyList.add(company);
		company = new Company();
		company.setCode("SSI");
		company.setFloorCode("10");
		company.setCompanyName("CTCP CK SSI");
		companyList.add(company);
		memory.put("COMPANY_LIST", "COMPANY_LIST", companyList);
		String resultInJsonp = companyController.getCompany("jsonp", new ModelMap());
		String expectedJsonp = "jsonp([{\"companyId\":0,\"companyName\":\"CTCP CK VNDIRECT\",\"code\":\"VND\",\"floorCode\":\"02\"},{\"companyId\":0,\"companyName\":\"CTCP CK SSI\",\"code\":\"SSI\",\"floorCode\":\"10\"}])";
		Assert.assertEquals(expectedJsonp, resultInJsonp);
	}
	
	@Test
	public void testCompanyControllerReturnNoSnapshotData() {
		String resultInJsonp = companyController.getCompany("jsonp", new ModelMap());
		String expectedJsonp = "jsonp()";
		Assert.assertEquals(expectedJsonp, resultInJsonp);
	}
}
