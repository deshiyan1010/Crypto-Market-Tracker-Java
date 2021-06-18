package IndicatorSignals;

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

public class IndicatorGang implements Runnable{

    private BarSeries series = new BaseBarSeries("bars");
    private String pair;
    private long secinterval;
    public IndicatorGang(String pair,int secinterval){
        this.pair = pair;
        this.secinterval = secinterval;
    }
    @Override
    public void run(){
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY,
                options);
        Candlestick c;
        List<Candlestick> cl;

        double crsi,cema,stoarr[];
        int emagoldencross;

        cl = syncRequestClient.getCandlestick(this.pair, CandlestickInterval.ONE_MINUTE, (Long) null, (Long) null, 50);
        for(int i=0;i<50;i++) {
            c = cl.get(i);
            LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(c.getOpenTime()), TimeZone.getDefault().toZoneId());

            LocalDateTime ldt = LocalDateTime.parse(triggerTime.toString());
            ZoneId zoneId = ZoneId.of("Asia/Kolkata"); // Or "America/Montreal" etc.
            ZonedDateTime zdt = ldt.atZone(zoneId); // Or atOffset( myZoneOffset ) if only an offset is known rather than a full time zone.

            this.series.addBar(zdt, c.getOpen(), c.getHigh(), c.getLow(), c.getClose(), c.getVolume());
        }

        for(int i=0;i<100;i++) {
            try{
                Thread.sleep(1000*this.secinterval);}
            catch (Exception e){
                System.out.println(e);
            }
            c = syncRequestClient.getCandlestick(this.pair, CandlestickInterval.ONE_MINUTE, (Long) null, (Long) null, 1).get(0);

            LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(c.getOpenTime()), TimeZone.getDefault().toZoneId());
            LocalDateTime ldt = LocalDateTime.parse(triggerTime.toString());
            ZoneId zoneId = ZoneId.of("Asia/Kolkata"); // Or "America/Montreal" etc.
            ZonedDateTime zdt = ldt.atZone(zoneId); // Or atOffset( myZoneOffset ) if only an offset is known rather than a full time zone.


            this.series.addBar(zdt, c.getOpen(), c.getHigh(), c.getLow(), c.getClose(), c.getVolume());

            crsi = new RSISignal(this.series,6).rsiFun();
            System.out.println("\nRSI: "+crsi);

            cema = new EMAIndicator(this.series,5).emafun();
            System.out.println("EMA: "+cema);

            emagoldencross = new EMACrossOver(this.series,2,4).emacrossfun();
            System.out.println("EMA Cross Over: "+emagoldencross);

            stoarr = new StoOsc(this.series,14).stochastic();
            System.out.println("Stro Osc  K:"+stoarr[0]+" D:"+stoarr[1]+" Cross"+stoarr[2]);



        }



    }

}
