package IndicatorSignals;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

public class EMACrossOver {

    private BarSeries series;
    private int barCount1,barCount2;
    public EMACrossOver(BarSeries series,int barCount1,int barCount2){
        this.series = series;
        this.barCount1 = barCount1;
        this.barCount2 = barCount2;
    }

    public int emacrossfun(){
        ClosePriceIndicator closePrice;
        closePrice = new ClosePriceIndicator(this.series);

        if (this.barCount1>this.barCount2){
            int temp = this.barCount1;
            this.barCount1 = this.barCount2;
            this.barCount2 = this.barCount1;
        }
        SMAIndicator shortSma = new SMAIndicator(closePrice, this.barCount1);
        SMAIndicator longSma = new SMAIndicator(closePrice, this.barCount2);

        int lindex = this.series.getEndIndex();
        double shortP = shortSma.getValue(lindex-1).doubleValue();
        double shortC = shortSma.getValue(lindex).doubleValue();

        double longP = longSma.getValue(lindex-1).doubleValue();
        double longC = longSma.getValue(lindex).doubleValue();

        if(shortP<longP && shortC>longC){
            return 1;
        }
        else if(shortP>longP && shortC<longC){
            return -1;
        }
        else {
            return 0;
        }
    }
}
