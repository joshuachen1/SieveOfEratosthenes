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
        ActorRef[] actorRefs = new ActorRef[n];

        try {
            while (numbers.size() > 0) {
                // Smallest prime
                int p = numbers.get(0);

                
                actorRefs[p] = sieveSystem.actorOf(PrimeActor.props(numbers));
                actorRefs[p].tell(new PrimeActor.PrimeNumber(p), ActorRef.noSender());

                // Find all multiples of p within numbers and remove them
                for (int i = 0; i < numbers.size(); i++) {
                    if (numbers.get(i) % p == 0) {
                        numbers.remove(numbers.get(i));
                    }
                }
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
