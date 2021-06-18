package MarketDetails;
import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.examples.constants.PrivateConfig;
import com.binance.client.model.enums.CandlestickInterval;


public class PriceChange {

    public static double[] pChange(String pair,long secago) {
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY,
                options);
        long ctime;
        double oldPrice,cprice;
        ctime = syncRequestClient.getCandlestick("BTCUSDT", CandlestickInterval.ONE_MINUTE, (Long)null, (Long)null, 1).get(0).getOpenTime();
        ctime -= secago*1000;
        System.out.println(ctime);
        cprice = syncRequestClient.getSymbolPriceTicker("BTCUSDT").get(0).getPrice().doubleValue();
        oldPrice = syncRequestClient.getCandlestick("BTCUSDT", CandlestickInterval.ONE_MINUTE, (Long)ctime, (Long)null, 1).get(0).getOpen().doubleValue();
        double[] arr = {cprice,oldPrice,cprice-oldPrice};
        return arr;
    }

}
