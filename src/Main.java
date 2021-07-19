import MarketDetails.ChartHistory;
import com.binance.client.model.enums.CandlestickInterval;

public class Main {

    public static void main(String[] args) {
//        //Indicator Thread
//        System.out.println("Start");
//        HistoryBuffer buffer = new HistoryBuffer(60);
//        IndicatorGang ig = new IndicatorGang("BTCUSDT",1,buffer, CandlestickInterval.ONE_MINUTE);
//        Thread t = new Thread(ig);
//        t.start();
//        while(true) {
//            try {
//                Thread.sleep(4000);
//            } catch (InterruptedException e) {
//                System.out.println(e);
//            }
//            System.out.println("Printing: "+(buffer.fetchData().getFirst().get("EMA")));

        //Movement Thread
        System.out.println("Start");
        ChartHistory chart = new ChartHistory("BTCUSDT",1,CandlestickInterval.ONE_MINUTE);
        Thread t = new Thread(chart);
        t.start();
        while(true) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println("Printing: "+chart.getPriceMovement().getLastBar().getClosePrice()+" "+chart.getPriceMovement().getFirstBar().getClosePrice());
        }
    }
}
