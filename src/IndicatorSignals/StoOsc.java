package IndicatorSignals;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.StochasticOscillatorDIndicator;
import org.ta4j.core.indicators.StochasticOscillatorKIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

public class StoOsc {

    private BarSeries series;
    private int barCount;
    public StoOsc(BarSeries series,int barCount){
        this.series = series;
        this.barCount = barCount;
    }

    public double[] stochastic(){

        ClosePriceIndicator closePrice;
        StochasticOscillatorKIndicator storisk= new StochasticOscillatorKIndicator(this.series,this.barCount);
        StochasticOscillatorDIndicator storisd= new StochasticOscillatorDIndicator(storisk);

        int lindex = this.series.getEndIndex();

        double skP = storisk.getValue(lindex-1).doubleValue();
        double skC = storisk.getValue(lindex).doubleValue();

        double sdP = storisd.getValue(lindex-1).doubleValue();
        double sdC = storisd.getValue(lindex).doubleValue();

        int cross;

        if(skP<sdP && skC>sdC){
            cross = 1;
        }
        else if(skP>sdP && skC<sdC){
            cross = -1;
        }
        else {
            cross = 0;
        }
        double[] arr = {sdC,skC,cross};
        return arr;
    }
}
