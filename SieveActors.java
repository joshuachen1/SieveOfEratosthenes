import java.io.IOException;
import java.util.ArrayList;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * @author Joshua Chen
 * Date: Nov 04, 2018
 */
public class SieveActors {
    public static void main(String[] args) throws IOException {
        final int n = 1000000;
        ArrayList<Integer> numbers = generateNumbersTo(n);
        ArrayList<Integer> primeNumbers = new ArrayList<>();

        final ActorSystem sieveSystem = ActorSystem.create("SieveOFEratosthenes");
        ActorRef[] actorRefs = new ActorRef[n];

        // Smallest prime
        int p = numbers.get(0);

        actorRefs[p] = sieveSystem.actorOf(PrimeActor.props(numbers));
        actorRefs[p].tell("", ActorRef.noSender());

        System.out.println(">>> Press ENTER to exit <<<");

        try {
            System.in.read();
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
