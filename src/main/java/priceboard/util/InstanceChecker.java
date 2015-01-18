package priceboard.util;

import priceboard.reloaddata.Company;
import vn.com.vndirect.priceservice.datamodel.Market;
import vn.com.vndirect.priceservice.datamodel.PutThrough;
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;
import vn.com.vndirect.priceservice.datamodel.SecInfo;
import vn.com.vndirect.priceservice.datamodel.Transaction;


public class InstanceChecker {

	public static boolean isStock(Object source) {
		return SecInfo.class.isInstance(source);
	}
	
	public static boolean isCompany(Object source) {
		return Company.class.isInstance(source);
	}
	
	public static boolean isMarket(Object source) {
		return Market.class.isInstance(source);
	}
	public static boolean isPutThroughTransaction  (Object source) {
		return PutThroughTransaction  .class.isInstance(source);
	}
	public static boolean isPutThrough(Object source) {
		return PutThrough.class.isInstance(source);
	}
	public static boolean isTransaction(Object source) {
		return Transaction.class.isInstance(source);
	}

}
