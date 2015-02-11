package priceboard.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

import priceboard.reloaddata.Company;
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
		Assert.assertTrue(InstanceChecker.isCompany(new Company()));
	}
	@Test
	public void testTimeDiff() {
		String time1="09:23:13" ;
		String time2="09:24:33" ;
		int result= time_diff(time1, time2);
		Assert.assertEquals(1, result);
		
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.println( "Time now:"+ sdf.format(cal.getTime()));
    	
	}
	
	public int time_diff(String time1, String time2) {
		if (time1.substring(0, 2).equals(time2.substring(0, 2)))
			return (Integer.parseInt(time2.substring(3, 5)) - Integer.parseInt(time1.substring(3, 5)));
		else
			return 1;
	}
}
