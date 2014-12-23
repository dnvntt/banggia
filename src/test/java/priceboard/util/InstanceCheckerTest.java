package priceboard.util;

import org.junit.Assert;
import org.junit.Test;

import vn.com.vndirect.priceservice.datamodel.Market;
import vn.com.vndirect.priceservice.datamodel.SecInfo;

public class InstanceCheckerTest {

	@Test
	public void testCheckInstance() {
		Assert.assertTrue(InstanceChecker.isStock(new SecInfo()));
		Assert.assertFalse(InstanceChecker.isStock(new Market()));
		Assert.assertFalse(InstanceChecker.isMarket(new SecInfo()));
		Assert.assertTrue(InstanceChecker.isMarket(new Market()));
	}
}
