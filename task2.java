import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class task2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean repeat;
        do {
            long totalStartTime = System.currentTimeMillis();

            CompletableFuture<List<Double>> generateNumbers = CompletableFuture.supplyAsync(() -> {
                long taskStart = System.currentTimeMillis();
                System.out.println("Введiть числа через пробiл або натиснiть Enter для генерацiї випадкових чисел:");
                String input = scanner.nextLine().trim();
                List<Double> numbers;
                if (input.isEmpty()) {
                    System.out.println("Генеруємо випадкову послiдовнiсть чисел...");
                    Random random = new Random();
                    numbers = random.doubles(20, 0, 100).boxed().collect(Collectors.toList());
                } else {
                    System.out.println("Приймаємо введенi числа...");
                    numbers = Arrays.stream(input.split(" "))
                            .map(Double::parseDouble)
                            .collect(Collectors.toList());
                }
                long taskEnd = System.currentTimeMillis();
                System.out.println("Час генерацiї чисел: " + (taskEnd - taskStart) + " мс");
                return numbers;
            });

            CompletableFuture<Double> minOddIndices = generateNumbers.thenApplyAsync(numbers -> {
                long taskStart = System.currentTimeMillis();
                System.out.println("Обчислюємо мiнiмум серед елементiв з непарними iндексами...");
                Double result = IntStream.range(0, numbers.size())
                        .filter(i -> i % 2 == 0)
                        .mapToObj(numbers::get)
                        .min(Double::compare)
                        .orElse(Double.NaN);
                long taskEnd = System.currentTimeMillis();
                System.out.println("Час обчислення мiнiмуму: " + (taskEnd - taskStart) + " мс");
                return result;
            });

            CompletableFuture<Double> maxEvenIndices = generateNumbers.thenApplyAsync(numbers -> {
                long taskStart = System.currentTimeMillis();
                System.out.println("Обчислюємо максимум серед елементiв з парними iндексами...");
                Double result = IntStream.range(0, numbers.size())
                        .filter(i -> i % 2 != 0)
                        .mapToObj(numbers::get)
                        .max(Double::compare)
                        .orElse(Double.NaN);
                long taskEnd = System.currentTimeMillis();
                System.out.println("Час обчислення максимуму: " + (taskEnd - taskStart) + " мс");
                return result;
            });

            generateNumbers.thenAcceptAsync(numbers -> {
                long taskStart = System.currentTimeMillis();
                System.out.println("Початкова послiдовнiсть: " + numbers);
                long taskEnd = System.currentTimeMillis();
                System.out.println("Час виводу послiдовностi: " + (taskEnd - taskStart) + " мс");
            });

            CompletableFuture<Void> result = minOddIndices.thenCombineAsync(maxEvenIndices, (min, max) -> min + max)
                    .thenAcceptAsync(sum -> {
                        long taskStart = System.currentTimeMillis();
                        System.out.println("Результат: min + max = " + sum);
                        long taskEnd = System.currentTimeMillis();
                        System.out.println("Час обчислення результату: " + (taskEnd - taskStart) + " мс");
                    });

            result.thenRunAsync(() -> {
                long totalEndTime = System.currentTimeMillis();
                System.out.println("Загальний час виконання: " + (totalEndTime - totalStartTime) + " мс");
            });

            result.join();

            System.out.print("Бажаєте повторити? (T/F): ");
            repeat = scanner.nextBoolean();
            scanner.nextLine();
        } while (repeat);

        scanner.close();
    }
}
