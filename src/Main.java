import Buffer.IndicatorBuffer;
import IndicatorSignals.IndicatorGang;
import com.binance.client.model.enums.CandlestickInterval;

public class Main {

    public static void main(String[] args) {
        System.out.println("Start");
        IndicatorBuffer buffer = new IndicatorBuffer(60);
        IndicatorGang ig = new IndicatorGang("BTCUSDT",1,buffer, CandlestickInterval.ONE_MINUTE);
        Thread t = new Thread(ig);
        t.start();
        while(true) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println("Printing: "+(buffer.fetchData().getLast().get("EMA")));

        }
    }
}
