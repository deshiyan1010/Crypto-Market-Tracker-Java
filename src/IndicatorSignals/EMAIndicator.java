package IndicatorSignals;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

public class EMAIndicator{

    private BarSeries series;
    private int barCount;

    public EMAIndicator(BarSeries series,int barCount){
        this.series = series;
        this.barCount = barCount;
    }

    public double emafun(){
        ClosePriceIndicator closePrice;
        closePrice = new ClosePriceIndicator(this.series);

        SMAIndicator shortSma = new SMAIndicator(closePrice, this.barCount);

        int lindex = this.series.getEndIndex();
        return shortSma.getValue(lindex).doubleValue();

    }

}
