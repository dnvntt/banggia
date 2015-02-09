package priceboard.stock.compress;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import priceboard.util.InstanceChecker;
import vn.com.vndirect.priceservice.datamodel.Market;
import vn.com.vndirect.priceservice.datamodel.PutThrough;
import vn.com.vndirect.priceservice.datamodel.PutThroughTransaction;
import vn.com.vndirect.priceservice.datamodel.SecInfo;
import vn.com.vndirect.priceservice.datamodel.Transaction;
import vn.com.web.commons.utility.DateUtils;

@Component
public class Mashaller {

	private static final String SEPARATOR = "|";
	
	public String compress(Object source) {
		if (InstanceChecker.isStock(source)) {
			return compressStock((SecInfo) source);
		}
		if (InstanceChecker.isMarket(source)) {
			return compressMarket((Market) source);
		}
		if (InstanceChecker.isTransaction(source)) {
			return compressTransaction((Transaction) source);
		}

		if (InstanceChecker.isPutThrough(source)) {
			return compressPutThrough((PutThrough) source);
		}

		if (InstanceChecker.isPutThroughTransaction(source)) {
			return compressPtOrder((PutThroughTransaction) source);
		}

		return StringUtils.EMPTY;
	}
	
	private String compressStock(SecInfo secInfo) {
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
		append(builder, secInfo.getCurrentQtty());
		append(builder, secInfo.getHighestPrice());
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

	public String compressMarket(Market market) {
		StringBuilder builder = new StringBuilder();		
		append(builder, market.getMarketID());
		append(builder, market.getTotalTrade());
		append(builder, market.getTotalShareTraded());
		append(builder, market.getTotalValueTraded());
		append(builder, market.getAdvance());
		append(builder, market.getDecline());
		append(builder, market.getNoChange());
		append(builder, "");
		append(builder, market.getChangedIndex());
		append(builder, market.getTradingTime());
		append(builder, convertToUnixtime(market.getTradingDate()));
		append(builder, market.getFloorCode());
		append(builder, market.getMarketIndex());
		append(builder, market.getPriorMarketIndex());
		append(builder, market.getHighestIndex());
		append(builder, market.getLowestIndex());
		append(builder, market.getShareTraded());
		append(builder, market.getStatus());
		append(builder, market.getSequence());
		return builder.toString();
	}
	
	public String compressTransaction(Transaction transaction) {
		StringBuilder builder = new StringBuilder();		
		append(builder, transaction.getFloorCode());
		append(builder, transaction.getSymbol());
		append(builder, transaction.getHighest());
		append(builder, transaction.getLast());
		append(builder, transaction.getLastVol());
		append(builder, transaction.getLowest());
		append(builder, transaction.getMatchType());
		
		append(builder, transaction.getOpenPrice());
		append(builder, transaction.getTime());
		append(builder,"");
		append(builder, "");
		
		return builder.toString();
	}
	
	public String compressPutThrough(PutThrough putThrough)
	{
		StringBuilder builder = new StringBuilder();
		append(builder, putThrough.getFloorCode());
		append(builder, putThrough.getStockSymbol());
		append(builder, putThrough.getPrice());
		append(builder, putThrough.getVol());
		append(builder, putThrough.getType());
		append(builder, putThrough.getStatus());
		append(builder, putThrough.getTime());
		
		append(builder, "");
		append(builder, putThrough.getBasicPrice());
		append(builder, putThrough.getCeilingPrice());
		append(builder, putThrough.getFloorPrice());
		
		return builder.toString();
	}
	
	public String compressPtOrder(PutThroughTransaction putThroughTransaction)
	{
		StringBuilder builder = new StringBuilder();
		append(builder, putThroughTransaction.getFloorCode());
		append(builder, putThroughTransaction.getSymbol());
		append(builder, putThroughTransaction.getPrice());
		append(builder, putThroughTransaction.getVolume());
		append(builder, putThroughTransaction.getTime());
		
		append(builder, "");
		append(builder, putThroughTransaction.getBasicPrice());
		append(builder, putThroughTransaction.getCeilingPrice());
		append(builder, putThroughTransaction.getFloorPrice());
		
		return builder.toString();
	}
	
	
	private void append(StringBuilder builder, Object value) {
		if (value == null) {
			builder.append(SEPARATOR);
		} else {
			builder.append(value).append(SEPARATOR);
		}
	}
	private static String convertToUnixtime(Date date)
	{
		if (date ==null) return "";
		long unixTime = date.getTime() / 1000;
		return    String.valueOf(unixTime *1000);
	}

}
