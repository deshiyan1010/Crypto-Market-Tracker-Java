package IndicatorSignals;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

public class RSISignal{

    private BarSeries series;
    private int barCount;
    public RSISignal(BarSeries series,int barCount){
        this.series = series;
        this.barCount = barCount;
    }

    public double rsiFun(){
        ClosePriceIndicator closePrice;
        int lindex = series.getEndIndex();
        closePrice = new ClosePriceIndicator(this.series);
        RSIIndicator rsi;
        rsi = new RSIIndicator(closePrice,this.barCount);
        double cris = rsi.getValue(lindex).doubleValue();
        return  cris;

    }

}




