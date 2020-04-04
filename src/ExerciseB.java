import java.util.*;

public class ExerciseB {
    public static final int NUM_ITER = 1000;
    public static final int NUM_ELEMENTS = 5;

    static class MyThread extends Thread {
        private Map<Integer, Integer> database;
        private Random numGenerator;

        MyThread(Map<Integer, Integer> database) {
            this.database = database;
            this.numGenerator = new Random();
        }

        public void run() {
            for (int i = 0; i < NUM_ITER; i++) {
                //select an element to change
                int id = numGenerator.nextInt(NUM_ELEMENTS);

                // add/update the element to the database
                synchronized (database) {
                    //the access to the database is the critical part
                    //therefore, all parts accessing it must be synchronized
                    if (database.containsKey(id)) {
                        //update the element
                        Integer element = database.get(id);
                        element += 1;
                        database.put(id, element);
                    } else {
                        //create the element
                        database.put(id, 1);
                    }
                }
            }//for
        }//run
    }//Threadclass

    public static void main(String[] args) throws Exception {
        Map<Integer, Integer> DB = new HashMap<Integer, Integer>();
        //this line makes a wrapper
        //é como se os metodos dele ficassem sincronizados imediatamente
        //mas o problema nao era este....era um thread poder ler o mesmo que outro
        //e isto nao previne contra isto
        //Map syncMap = Collections.synchronizedMap(DB);
        Thread a = new MyThread(DB);
        Thread b = new MyThread(DB);

        a.start();
        b.start();

        a.join();
        b.join();

        int total = 0;

            for (int i = 0; i < NUM_ELEMENTS; i++) {
                Integer el = DB.get(i);
                if (el != null) {
                    System.out.println("Elements in bucket #" + i + ":" + el);
                    total += DB.get(i);
                }
            }//for
            System.out.println("Total items:" + total);
        }
    }
