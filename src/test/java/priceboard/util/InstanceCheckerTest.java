package priceboard.util;

import org.junit.Assert;
import org.junit.Test;

import vn.com.vndirect.datafeed.util.MarketStatisMessage;
import vn.com.vndirect.priceservice.datamodel.Market;
import vn.com.vndirect.priceservice.datamodel.PutThrough;
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;
import vn.com.vndirect.priceservice.datamodel.SecInfo;
import vn.com.vndirect.priceservice.datamodel.Transaction;

public class InstanceCheckerTest {

	@Test
	public void testCheckInstance() {
		Assert.assertTrue(InstanceChecker.isStock(new SecInfo()));
		Assert.assertFalse(InstanceChecker.isStock(new Market()));
		Assert.assertFalse(InstanceChecker.isMarket(new SecInfo()));
		Assert.assertTrue(InstanceChecker.isMarket(new Market()));
		Assert.assertTrue(InstanceChecker.isMarketStatisMessage(new MarketStatisMessage()));
		Assert.assertTrue(InstanceChecker.isPutThroughTransaction(new PutThroughTransaction()));
		Assert.assertTrue(InstanceChecker.isTransaction(new Transaction()));
		Assert.assertTrue(InstanceChecker.isPutThrough(new PutThrough()));
	}
}
