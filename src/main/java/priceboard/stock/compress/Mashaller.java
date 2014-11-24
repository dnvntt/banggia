package priceboard.stock.compress;

import org.springframework.stereotype.Component;

import vn.com.vndirect.priceservice.datamodel.SecInfo;
import vn.com.web.commons.utility.DateUtils;

@Component
public class Mashaller {

	private static final String SEPARATOR = "|";
	
	public String compress(Object object) {
		SecInfo secInfo = (SecInfo) object;
		StringBuilder builder = new StringBuilder();
		append(builder, secInfo.getFloorCode());
		append(builder, DateUtils.dateToStringITA(secInfo.getTradingDate()));
		append(builder, secInfo.getTime());
		append(builder, secInfo.getCode());
		append(builder, secInfo.getCompanyName());
		append(builder, secInfo.getStockType());
		append(builder, secInfo.getTotalRoom());
		append(builder, secInfo.getCurrentRoom());
		append(builder, secInfo.getBasicPrice());
		append(builder, secInfo.getOpenPrice());
		append(builder, secInfo.getClosePrice());
		append(builder, secInfo.getCurrentPrice());
		append(builder, secInfo.getHighestPrice());
		append(builder, secInfo.getCurrentQtty());
		append(builder, secInfo.getLowestPrice());
		append(builder, secInfo.getCeilingPrice());
		append(builder, secInfo.getFloorPrice());
		append(builder, secInfo.getTotalOfferQtty());
		append(builder, secInfo.getTotalBidQtty());
		append(builder, secInfo.getMatchPrice());
		append(builder, secInfo.getMatchQtty());
		append(builder, secInfo.getMatchValue());
		append(builder, secInfo.getAveragePrice());
		append(builder, secInfo.getBidPrice01());
		append(builder, secInfo.getBidQtty01());
		append(builder, secInfo.getBidPrice02());
		append(builder, secInfo.getBidQtty02());
		append(builder, secInfo.getBidPrice03());
		append(builder, secInfo.getBidQtty03());
		append(builder, secInfo.getOfferPrice01());
		append(builder, secInfo.getOfferQtty01());
		append(builder, secInfo.getOfferPrice02());
		append(builder, secInfo.getOfferQtty02());
		append(builder, secInfo.getOfferPrice03());
		append(builder, secInfo.getOfferQtty03());
		append(builder, secInfo.getAccumulatedVal());
		append(builder, secInfo.getAccumulatedVol());
		append(builder, secInfo.getBuyForeignQtty());
		append(builder, secInfo.getSellForeignQtty());
		append(builder, secInfo.getProjectOpen());
		append(builder, "");
		return builder.toString();
	}
	
	private void append(StringBuilder builder, Object value) {
		if (value == null) {
			builder.append(SEPARATOR);
		} else {
			builder.append(value).append(SEPARATOR);
		}
	}

}
