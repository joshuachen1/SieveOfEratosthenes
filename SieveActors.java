import java.io.IOException;
import java.util.ArrayList;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
/**
 * @author Joshua Chen
 * Date: Nov 04, 2018
 */
public class SieveActors {
    public static void main(String[] args) {
        final int n = 1000000;
        ArrayList<Integer> numbers = generateNumbersTo(n);

        final ActorSystem sieveSystem = ActorSystem.create("SieveOFEratosthenes");

        try {
            while (numbers.size() > 0) {
                
            }
            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
        } catch (IOException ioe) {
        } finally {
            sieveSystem.terminate();
        }


    }

    private static ArrayList<Integer> generateNumbersTo(int lastInt) {
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 2; i <= lastInt; i++) {
            temp.add(i);
        }
        return temp;
    }
}
