package Buffer;

import java.util.HashMap;
import java.util.LinkedList;


public class HistoryBuffer {
    int maxLength;

    public HistoryBuffer(int maxLength) {

        this.maxLength = maxLength;
    }

    LinkedList<HashMap<String, Object>> ll = new LinkedList<HashMap<String, Object>>();

    private int len = 0;

    public void add(HashMap a) {
//        if(len>maxLength){
//            try{
//                wait();
//            }
//            catch(Exception e){
//                System.out.println(e);
//            }
//        }
//        System.out.println("Added: "+a);
        ll.addLast(a);
        this.len += 1;
        if (this.len > this.maxLength) {
            this.remove();
        }
//        notifyAll();
    }

    public Object remove() {
//        if(len==0){
//            try{
//                wait();
//            }
//            catch(Exception e){
//                System.out.println(e);
//            }
//        }
//        System.out.println("\t\tRemoved: "+ll.removeFirst());
        this.len -= 1;
//        notifyAll();
        return ll.removeFirst();
    }

    public LinkedList<HashMap<String, Object>> fetchData() {
        return ll;
    }
}
//class Producer extends Thread {
//
//    HistoryBuffer b;
//
//    Producer(HistoryBuffer b)
//    {
//        this.b = b;
//    }
//
//    public void run()
//    {
//
//        for (int i = 0; i < 4000; i++) {
//            b.add(i);
//            try {
//                Thread.sleep(10);
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }
//}
//
//
//
//class Consumer extends Thread {
//
//    HistoryBuffer b;
//
//    Consumer(HistoryBuffer b)
//    {
//        this.b = b;
//    }
//
//    public void run()
//    {
//
//        for (int i = 0; i < 4000; i++) {
//            b.remove();
//            try {
//                Thread.sleep(10);
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//
//    }
//}

//public class Main {
//    public static void main(String args[])
//    {
//        HistoryBuffer b = new HistoryBuffer(10);
//
//        Producer p = new Producer(b);
//        Consumer c = new Consumer(b);
//
//        Thread t1 = new Thread(p);
//        Thread t2 = new Thread(c);
//
//        t2.start();
//        t1.start();
//    }
//}
