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

    static public class PrimeNumber {
        public final int prime;

        public PrimeNumber(int prime) {
            this.prime = prime;
        }
    }

    private final ArrayList<Integer> numbers;

    public PrimeActor(ArrayList<Integer> numbers) {
        this.numbers = numbers;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PrimeNumber.class, pNum -> {
                    log().info(pNum.prime + " is prime");
                })
                .build();

    }
}
