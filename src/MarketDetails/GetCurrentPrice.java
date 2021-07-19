package MarketDetails;


import Buffer.HistoryBuffer;
import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.examples.constants.PrivateConfig;

import java.util.List;

public class GetCurrentPrice {
    public static List getAllTickers(String pair, HistoryBuffer buffer){
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY,
                options);
        List tickers;
        tickers = syncRequestClient.getSymbolPriceTicker(null);
        return tickers;
    }
}