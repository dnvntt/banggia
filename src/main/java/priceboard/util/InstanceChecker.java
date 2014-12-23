package priceboard.util;

import priceboard.reloaddata.Company;
import vn.com.vndirect.priceservice.datamodel.Market;
import vn.com.vndirect.priceservice.datamodel.SecInfo;


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

}
