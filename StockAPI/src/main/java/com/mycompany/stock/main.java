package com.mycompany.stock;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.ArrayList; 
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class main {
    static int GC = 0;

    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<String> dataQueue = new ArrayList<String>(); 
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(4);
        ScheduledExecutorService consumerPool = Executors.newScheduledThreadPool(1);
        ScheduledExecutorService consumerPool2 = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(new ProducerA(dataQueue, "NTPCEQN"), 0, 5, TimeUnit.SECONDS);
        pool.scheduleAtFixedRate(new ProducerA(dataQueue, "POWERGRIDEQN"), 0, 5, TimeUnit.SECONDS);
        pool.scheduleAtFixedRate(new ProducerA(dataQueue, "LTEQN"), 0, 5, TimeUnit.SECONDS);
        pool.scheduleAtFixedRate(new ProducerA(dataQueue, "COALINDIAEQN"), 0, 5, TimeUnit.SECONDS);

        consumerPool.scheduleAtFixedRate(new ConsumerA(dataQueue), 0, 2, TimeUnit.SECONDS);
        consumerPool2.scheduleAtFixedRate(new ConsumerA2(dataQueue), 0, 2, TimeUnit.SECONDS);
//        while (true){
//            System.gc();
//            Thread.sleep(3000);
//            GC++;
//            System.out.println("Garbage collected instance : "+ GC);
//        }
    }
}

class ProducerA implements Runnable {

    ArrayList<String> dataQueue;
    String identifier;

    public ProducerA(ArrayList<String> dataQueue, String identifier) {
        this.dataQueue = dataQueue;
        this.identifier = identifier;

    }
//    @Benchmark
//    @BenchmarkMode(Mode.Throughput)
//    @OutputTimeUnit(TimeUnit.SECONDS)
//    @Threads(1)
//
//    @Warmup(iterations = 1)//20
//    @Measurement(iterations = 5) //50
//    @Fork(value = 1)
//
//    @Timeout(time = 10)

    public static String getStats(String identifier) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://latest-stock-price.p.rapidapi.com/price?Indices=NIFTY%2050&Identifier=" + identifier))
                .header("x-rapidapi-key", "72d100906bmshac1e87b6a2e495dp1d7abejsnf12116abf87c")
                .header("x-rapidapi-host", "latest-stock-price.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        //response.body parsing
        String data = response.body();
        String leftremove = data.replace('[', ' ');
        String noboxdata = leftremove.replace(']', ' ');
        //response.body parsing
        Gson g = new Gson();
        stock s = g.fromJson(noboxdata, stock.class);
        String vo_parse = String.valueOf(s.getVolatility());
        Double growth = s.getGrowth();
        String growth_parse = String.valueOf(growth);
        String final_compute = "Stock Symbol : " + s.getSymbol() + " Volatility : " + vo_parse + " Grown by " + growth_parse + " percent since year 2020";
        return final_compute;
    }

    @Override
    public void run() {
        try {
            String data = getStats(identifier);
            if (!data.isEmpty()) {
                dataQueue.add(data);
                Timestamp producerTime = new Timestamp(System.currentTimeMillis());
                System.out.println(this.getClass().getName() + " added data to queue at timestamp : " + producerTime);
            }
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

class ConsumerA implements Runnable {

    ArrayList<String> dataQueue;

    public ConsumerA(ArrayList<String> dataQueue) {
        this.dataQueue = dataQueue;
    }

    @Override
    public void run() {
        try {
            if (!dataQueue.isEmpty()) {
                String data = dataQueue.get(dataQueue.size() - 1);
                dataQueue.remove(dataQueue.size() - 1);
                Timestamp consumerTime = new Timestamp(System.currentTimeMillis());
                System.out.println("Exchange 1 received stock info: " + data + " at timestamp: " + consumerTime);
            }
        } catch (Exception e) {
        }
    }
}

class ConsumerA2 implements Runnable {

    ArrayList<String> dataQueue;

    public ConsumerA2(ArrayList<String> dataQueue) {
        this.dataQueue = dataQueue;
    }

    @Override
    public void run() {
        try {
            if (!dataQueue.isEmpty()) {
                String data = dataQueue.get(dataQueue.size() - 1);
                dataQueue.remove(dataQueue.size() - 1);
                Timestamp consumerTime2 = new Timestamp(System.currentTimeMillis());
                System.out.println("Exchange 2 received stock info: " + data + "at timestamp: " + consumerTime2);
            }
        } catch (Exception e) {
        }
    }
}
