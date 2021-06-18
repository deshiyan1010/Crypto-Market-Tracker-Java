import IndicatorSignals.IndicatorGang;

public class Main {

    public static void main(String[] args) {
        IndicatorGang ig = new IndicatorGang("BTCUSDT",60);
        Thread t = new Thread(ig);
        t.start();
        System.out.println("Just doin my work now.");


    }
}
