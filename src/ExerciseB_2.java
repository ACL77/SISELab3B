import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ExerciseB_2 {
    public static final int NUM_ITER = 1000;
    public static final int NUM_ELEMENTS = 5;

    static class MyThread extends Thread {
        //Atomic because when using this variable, only one thread can use it at one time
        private Map<Integer, AtomicInteger> database;
        private Random numGenerator;

        MyThread(Map<Integer, AtomicInteger> database) {
            this.database = database;
            this.numGenerator = new Random();
        }

        public void run() {
            for (int i = 0; i < NUM_ITER; i++) {
                //select an element to change
                int id = numGenerator.nextInt(NUM_ELEMENTS);

                database.putIfAbsent(id,new AtomicInteger(0));
                database.get(id).incrementAndGet();
            }//for
        }//run
    }//Threadclass

    public static void main(String[] args) throws Exception {
        Map<Integer, AtomicInteger> DB = new HashMap<Integer, AtomicInteger>();
        Thread a = new MyThread(DB);
        Thread b = new MyThread(DB);

        a.start();
        b.start();

        a.join();
        b.join();

        int total = 0;

            for (int i = 0; i < NUM_ELEMENTS; i++) {
                AtomicInteger el = DB.get(i);
                if (el != null) {
                    System.out.println("Elements in bucket #" + i + ":" + el);
                    total += DB.get(i).get();
                }
            }//for
            System.out.println("Total items:" + total);
        }
    }
