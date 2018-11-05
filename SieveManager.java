import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * @author Joshua Chen
 *         Date: Nov 05, 2018
 */
public class SieveManager extends AbstractActor {

    static public Props props() {
        return Props.create(SieveManager.class, () -> new SieveManager());
    }

    static class NewPrime {
        public int newPrime;

        public NewPrime(int newPrime) {
            this.newPrime = newPrime;
        }
    }

    private int N;
    private long timeIn, timeOut;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SieveActors.Start.class, start -> {
                    N = start.num;
                    System.out.println("N = " + N);

                    timeIn = System.nanoTime();

                    // Create first prime
                    ActorRef p2 = getContext().actorOf(PrimeActor.props(2, N, self()));
                    self().tell(new NewPrime(2), ActorRef.noSender());

                    for (int i = 3; i < N; i++) {
                        p2.tell(i, ActorRef.noSender());
                    }

                    p2.tell(new PrimeActor.End(), ActorRef.noSender());
                })
                .match(NewPrime.class, foo -> {
                    System.out.println(foo.newPrime + " is prime.");
                })
                .match(PrimeActor.End.class, foo -> {
                    timeOut = System.nanoTime() - timeIn;
                    System.out.printf("Time for Actors: %.2f (ms)", timeOut * 1e-6);
                })
                .build();
    }
}
