package MarketDetails;

import Buffer.HistoryBuffer;
import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.examples.constants.PrivateConfig;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.market.Candlestick;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.TimeZone;

public class ChartHistory extends Thread{

    private BarSeries series = new BaseBarSeries("bars"),seriestemp = new BaseBarSeries("barstemp");
    private String pair;
    private long secInterval;
    private HistoryBuffer buffer;
    private CandlestickInterval chartInterval;

    public ChartHistory(String pair, int secInterval, CandlestickInterval chartInterval){
        this.chartInterval = chartInterval;
        this.pair = pair;
        this.buffer = buffer;
        this.secInterval = secInterval;
//        this.series.setMaximumBarCount(10);
    }

    @Override
    public void run(){
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY,
                options);
        Candlestick c;
        List<Candlestick> cl;


        cl = syncRequestClient.getCandlestick(this.pair, this.chartInterval, (Long) null, (Long) null, 50);


        for (int i = 0; i < 50; i++) {
            c = cl.get(i);
            LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(c.getOpenTime()), TimeZone.getDefault().toZoneId());

            LocalDateTime ldt = LocalDateTime.parse(triggerTime.toString());
            ZoneId zoneId = ZoneId.of("Asia/Kolkata");
            ZonedDateTime zdt = ldt.atZone(zoneId);

            this.series.addBar(zdt, c.getOpen(), c.getHigh(), c.getLow(), c.getClose(), c.getVolume());
        }

        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(1000 * this.secInterval);
            } catch (Exception e) {
                System.out.println(e);
            }
            c = syncRequestClient.getCandlestick(this.pair, this.chartInterval, (Long) null, (Long) null, 1).get(0);

            LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(c.getCloseTime()), TimeZone.getDefault().toZoneId());
            LocalDateTime ldt = LocalDateTime.parse(triggerTime.toString());
            ZoneId zoneId = ZoneId.of("Asia/Kolkata");
            ZonedDateTime zdt = ldt.atZone(zoneId);
            try {
                this.series.addBar(zdt, c.getOpen(), c.getHigh(), c.getLow(), c.getClose(), c.getVolume());
            }
            catch (IllegalArgumentException e){
                for(int x=0;x<this.series.getBarCount()-1;x++)
                    this.seriestemp.addBar(this.series.getBar(x));
                this.series = this.seriestemp;
                this.seriestemp = new BaseBarSeries("barstemp");
                this.series.addBar(zdt, c.getOpen(), c.getHigh(), c.getLow(), c.getClose(), c.getVolume());
            }
        }
    }
    public BarSeries getPriceMovement(){
        return this.series;
    }
}