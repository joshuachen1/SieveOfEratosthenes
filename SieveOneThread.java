import java.util.ArrayList;
/**
 * @author Joshua Chen
 * Date: Nov 03, 2018
 */
public class SieveOneThread {
    public static void main(String[] args) {

        final int n = 1000000;
        boolean[] isPrime = new boolean[n + 1];

        long timeIn = System.nanoTime();

        // Initialize all numbers from 2 to n to true
        for (int i = 2; i <= n; i++) {
            isPrime[i] = true;
        }

        // Check primes under the square root of n
        for (int i = 0; (i * i) <= n; i++) {
            if (isPrime[i]) {
                // Mark all multiples of the prime
                for (int multipleOfI = i; (i * multipleOfI) <= n; multipleOfI++) {
                    isPrime[i * multipleOfI] = false;
                }
            }
        }

        // Display all primes from 2 to n
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) {
                System.out.println(i);
            }
        }

        long timeOut = System.nanoTime() - timeIn;

        System.out.printf("Time for 1 Thread: %.2f (ms)", timeOut * 1e-6);  // Time for 1 Thread: 694864.94 (ms)
    }

    private static ArrayList<Integer> generateNumbersTo(int lastInt) {
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 2; i <= lastInt; i++) {
            temp.add(i);
        }
        return temp;
    }
}
