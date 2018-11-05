import akka.actor.AbstractActor;
import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import java.util.ArrayList;

/**
 * @author Joshua Chen
 *         Date: Nov 04, 2018
 */
public class PrimeActor extends AbstractLoggingActor {

    static public Props props(ArrayList<Integer> numbers) {
        return Props.create(PrimeActor.class, () -> new PrimeActor(numbers));
    }

    private final ArrayList<Integer> numbers;
    private int prime;

    public PrimeActor(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("", p -> {
                    prime = numbers.remove(0);
                    // log().info(prime + " is prime");
                    System.out.println(prime + " is prime");

                    ActorRef nextPrimeActor;

                    ArrayList<Integer> nextNumbers = new ArrayList<Integer>();

                    // Find all non-multiples of prime within numbers
                    // and add them to nextNumbers
                    int i = 0;
                    boolean sizeIncreasing = true;
                    while (i < numbers.size() || sizeIncreasing) {
                        sizeIncreasing = false;
                        int size = numbers.size();

                        if (numbers.get(i) % prime != 0) {
                            nextNumbers.add(numbers.get(i));
                        }
                        if (nextNumbers.size() > 1) {
                            // String name = "Prime-Actor-" + numbers.get(0);
                            nextPrimeActor = getContext().actorOf(PrimeActor.props(nextNumbers));
                            nextPrimeActor.tell("", ActorRef.noSender());
                        }
                        // Check if size has changed since the first call
                        if (size < numbers.size()) {
                            sizeIncreasing = true;
                        }
                        i++;
                    }
                })
                .build();

    }
}
