package priceboard.rest.controller;

import org.junit.Assert;
import org.junit.Test;

public class PingControllerTest {
	@Test
	public void testStockController() {
		String expect = "ping123456"+"({})";
		PingController controller = new PingController();
		String dataReturn = controller.checkPing("ping123456");
		
		Assert.assertEquals(expect,dataReturn);
	}
}
