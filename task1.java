import java.util.concurrent.*;

public class task1 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        CompletableFuture<int[][]> arrayFuture = CompletableFuture.supplyAsync(() -> {          //Створення матриці
            long taskStartTime = System.currentTimeMillis();
            int[][] array = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    array[i][j] = ThreadLocalRandom.current().nextInt(1, 50);
                }
            }
            System.out.println("Матриця створена за: " + (System.currentTimeMillis() - taskStartTime) + " мс");
            return array;
        });

        CompletableFuture<Void> columnPrintFuture = arrayFuture.thenAcceptAsync(array -> {          //Вивід
            long taskStartTime = System.currentTimeMillis();
            for (int j = 0; j < 3; j++) {
                System.out.print("Стовпець " + (j + 1) + ": ");
                for (int i = 0; i < 3; i++) {
                    System.out.print(array[i][j] + ", ");
                }
                System.out.println();
            }
            System.out.println("Виконано за: " + (System.currentTimeMillis() - taskStartTime) + " мс");
        });

        columnPrintFuture.thenRunAsync(() -> {
            long taskStartTime = System.currentTimeMillis();
            System.out.println("Таска виконана");
            System.out.println("Рядок вище написаний за: " + (System.currentTimeMillis() - taskStartTime) + " мс");
        });


         try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Загальний час роботи коду: " + (System.currentTimeMillis() - startTime) + " мс");
    }
}
