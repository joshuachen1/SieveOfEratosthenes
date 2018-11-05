import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * @author Joshua Chen
 * Date: Nov 05, 2018
 *
 * Keeps track of the boolean array of prime numbers.
 * Starts the sieving at smallest prime: 2 to N.
 */
public class SieveManager extends AbstractActor {

    static public Props props() {
        return Props.create(SieveManager.class, () -> new SieveManager());
    }

    private boolean[] isPrime;
    private int N;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SieveActors.Start.class, start -> {
                    N = start.num;

                    this.isPrime = new boolean[N + 1];

                    for (int i = 2; i <= N; i++) {
                        isPrime[i] = true;
                    }

                    ActorRef prime2 = getContext().actorOf(PrimeActor.props(isPrime, 2, N, self()));

                    prime2.tell(new PrimeActor.Begin(), ActorRef.noSender());
                })
                .build();
    }
}
