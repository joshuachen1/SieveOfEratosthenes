import akka.actor.AbstractActor;
import akka.actor.ActorRef;

/**
 * @author Joshua Chen
 *         Date: Nov 05, 2018
 */
public class SieveManager extends AbstractActor {
    private int N = 0;
    private long timeIn, timeOut;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SieveActors.Start.class, lastNum -> {
                    N = lastNum.num;

                    timeIn = System.nanoTime();
                    ActorRef p2 = getContext().actorOf(PrimeActor.props(2, lastNum.num, self()));

                    for (int i = 3; i < N; i++) {
                        p2.tell(i, ActorRef.noSender());
                    }
                })
                .match(SieveActors.End.class, foo -> {
                    timeOut = System.nanoTime() - timeIn;
                    System.out.printf("Time for Actors: %.2f (ms)", timeOut * 1e-6);
                })
                .match(PrimeActor.NewPrime.class, newPrime -> {
                    System.out.println(newPrime + " is prime");
                })
                .build();
    }
}
