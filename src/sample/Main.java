package sample;

import MarketDetails.ChartHistory;
import com.binance.client.model.enums.CandlestickInterval;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    final int WINDOW_SIZE = 20;
    private ScheduledExecutorService scheduledExecutorService;


    String[] pairs = {"BTCUSDT","BNBUSDT","DOGEUSDT","ETHUSDT","XRPUSDT","MATICUSDT"};
    Label[] labelarr = new Label[10];
    Button[] buttonarr = new Button[10];
    ImageView[] imgviewarr = new ImageView[10];;

    VBox layout1=new VBox();
    Scene scene1= new Scene(layout1,250,600);
    VBox layout2=new VBox();
    Scene scene2= new Scene(layout2);
    Stage primaryStage1;


    public void priceThread(String pair,Label label){
        label.setFont(Font.font("Arial",16));
        ChartHistory chart = new ChartHistory(pair, 1, CandlestickInterval.ONE_MINUTE);
        Thread t = new Thread(chart);
        t.start();

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2500);
            } catch (Exception e1) {
            }
            for (;;) {
                final String text = "Value: $ " + chart.getPriceMovement().getLastBar().getClosePrice();
                Platform.runLater(() -> label.setText(text));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException exc) {
                    exc.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void lineChart(String pair){
        ChartHistory chart = new ChartHistory(pair, 1, CandlestickInterval.ONE_MINUTE);
        chart.start();
        try{
            Thread.sleep(2500);
        }
        catch(Exception e){
            System.out.println(e);
        }

        final CategoryAxis xAxis = new CategoryAxis(); // we are gonna plot against time
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time/s");
        xAxis.setAnimated(false); // axis animations are removed
        yAxis.setLabel("Value");
        yAxis.setAnimated(false); // axis animations are removed


        //creating the line chart with two axis created above
        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(pair+" Chart");
        lineChart.setAnimated(false); // disable animations



        xAxis.setAutoRanging(true);
//        xAxis.setForceZeroInRange(false);
        yAxis.setAutoRanging(true);
        yAxis.setForceZeroInRange(false);



        //defining a series to display data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(pair+" $");

        // add series to chart
        lineChart.getData().add(series);
        // setup scene
        // this is used to display time in HH:mm:ss format
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
//            t.start();
        // setup a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        // put dummy data onto graph per second
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // get a random integer between 0-10
            Integer random = ThreadLocalRandom.current().nextInt(10);

            // Update the chart
            Platform.runLater(() -> {
                // get current time
                Date now = new Date();
                // put random number with current time
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), chart.getPriceMovement().getLastBar().getClosePrice().doubleValue()));

                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });
        }, 0, 1, TimeUnit.SECONDS);

        Button butt2 = new Button("Go Back");

        butt2.setLayoutY(10);
        butt2.setLayoutX(10);
        butt2.setOnAction(e-> {
            primaryStage1.setScene(scene1);
            layout2.getChildren().removeAll(lineChart,butt2);
        });
//        lineChart.setLayoutX(100);
//        lineChart.setLayoutY(100);
//        butt2.setLayoutX(20);
//        butt2.setLayoutY(20);
        layout2.setSpacing(10);
        layout2.getChildren().addAll(lineChart,butt2);
    }
    @Override
    public void start(Stage primaryStage1) throws Exception {

        this.primaryStage1 = primaryStage1;

//        layout1.setSpacing(10);

        for(int i=0;i<pairs.length;i++){
            buttonarr[i] = new Button(pairs[i]);
            labelarr[i] = new Label("waiting for connection...");
            imgviewarr[i] = new ImageView(pairs[i]+".png");
            imgviewarr[i].setFitWidth(50);
            imgviewarr[i].setFitHeight(50);
            int finalI = i;
            priceThread(pairs[finalI],labelarr[finalI]);
            buttonarr[i].setOnAction(e ->{
                lineChart(pairs[finalI]);
                    primaryStage1.setScene(scene2);
            });

            layout1.getChildren().addAll(imgviewarr[i],buttonarr[i],labelarr[i],new Separator());
        }
        imgviewarr[3].setFitWidth(70);
        layout1.setAlignment(Pos.CENTER);
        layout2.setAlignment(Pos.BOTTOM_RIGHT);
        primaryStage1.setScene(scene1);
        Image icon= new Image("BTCUSDT.png");
        primaryStage1.getIcons().add(icon);
        primaryStage1.setTitle("Market Tracker");
        primaryStage1.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        scheduledExecutorService.shutdownNow();
        System.exit(0);
    }

    public static void main (String[]args){
        Main.launch(args);

    }

}



//            label2.setFont(Font.font(30));
//            label3.setFont(Font.font(30));
//            img.setFitWidth(50);
//            img.setFitHeight(50);
//            img1.setFitWidth(50);
//            img1.setFitHeight(50);
//
//        ChartHistory chart = new ChartHistory("BTCUSDT", 1, CandlestickInterval.ONE_MINUTE);
//        chart.start();
//        try{
//            Thread.sleep(2500);
//        }
//        catch(Exception e){
//            System.out.println(e);
//        }
//
//        final CategoryAxis xAxis = new CategoryAxis(); // we are gonna plot against time
//        final NumberAxis yAxis = new NumberAxis();
//        xAxis.setLabel("Time/s");
//        xAxis.setAnimated(false); // axis animations are removed
//        yAxis.setLabel("Value");
//        yAxis.setAnimated(false); // axis animations are removed
//
//
//        //creating the line chart with two axis created above
//        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
//        lineChart.setTitle("BTC/BNB Chart");
//        lineChart.setAnimated(false); // disable animations
//
//
//
//        xAxis.setAutoRanging(true);
////        xAxis.setForceZeroInRange(false);
//        yAxis.setAutoRanging(true);
//        yAxis.setForceZeroInRange(false);
//
//
//
//        //defining a series to display data
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        series.setName("BTC/BNB $");
//
//        // add series to chart
//        lineChart.getData().add(series);
//        // setup scene
//        // this is used to display time in HH:mm:ss format
//        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
////            t.start();
//        // setup a scheduled executor to periodically put data into the chart
//        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//
//        // put dummy data onto graph per second
//        scheduledExecutorService.scheduleAtFixedRate(() -> {
//            // get a random integer between 0-10
//            Integer random = ThreadLocalRandom.current().nextInt(10);
//
//            // Update the chart
//            Platform.runLater(() -> {
//                // get current time
//                Date now = new Date();
//                // put random number with current time
//                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), chart.getPriceMovement().getLastBar().getClosePrice().doubleValue()));
//
//                if (series.getData().size() > WINDOW_SIZE)
//                    series.getData().remove(0);
//            });
//        }, 0, 1, TimeUnit.SECONDS);
//
//        Button butt1 = new Button("Show Line Chart");
//        Button butt2 = new Button("Go Back");
//
//
//        butt1.setOnAction(e-> primaryStage1.setScene(scene2));
//        butt2.setOnAction(e-> primaryStage1.setScene(scene1));
//        lineChart.setLayoutX(100);
//        lineChart.setLayoutY(100);
//        butt2.setLayoutX(20);
//        butt2.setLayoutY(20);
//        layout1.getChildren().addAll(butt1);
//        layout2.getChildren().addAll(lineChart,butt2);
//


