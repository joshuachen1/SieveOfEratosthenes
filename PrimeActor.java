import akka.actor.*;

/**
 * @author Joshua Chen
 *         Date: Nov 04, 2018
 */
public class PrimeActor extends AbstractActor {

    static public Props props(int currentPrime, int N, ActorRef manager) {
        return Props.create(PrimeActor.class, () -> new PrimeActor(currentPrime, N, manager));
    }

    static class NewPrime {
        public int newPrime;

        public NewPrime(int newPrime) {
            this.newPrime = newPrime;
        }
    }

    private int currentPrime;
    private int N;
    private ActorRef manager;
    private ActorRef nextPrime;

    public PrimeActor(int currentPrime, int N, ActorRef manager) {
        this.currentPrime = currentPrime;
        this.N = N;
        this.manager = manager;
        nextPrime = null;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals(Integer.class, p -> getContext().become(beforeNewPrime()))
                .build();
    }

    private Receive beforeNewPrime() {
        return receiveBuilder()
                .match(Integer.class, p -> {
                    // Not divisible by current prime
                    if (p % currentPrime != 0) {
                        manager.tell(new NewPrime(p), self());

                        if (p > Math.sqrt(N)) {
                            getContext().become(restArePrimes());
                        } else {
                            nextPrime = getContext().actorOf(PrimeActor.props(p, N, manager));
                            getContext().become(afterNewPrime());
                        }
                    }
                })
                .match(SieveActors.End.class, foo -> {
                    manager.tell(SieveActors.End.class, self());
                })
                .build();
    }

    private Receive restArePrimes() {
        return receiveBuilder()
                .match(Integer.class, p -> {
                    if (p % currentPrime != 0) {
                        // Send the number to next prime
                        nextPrime.tell(p, self());
                    }
                })
                .match(SieveActors.End.class, foo -> {
                    // Received an End from the previous actor/prime
                    // Notify the next actor/prime that no more numbers will be sent
                    nextPrime.tell(SieveActors.End.class, self());
                })
                .build();
    }

    private Receive afterNewPrime() {
        return receiveBuilder()
                .match(Integer.class, p -> {
                    if (p % currentPrime != 0) {
                        // Updates manager about new prime
                       manager.tell(new NewPrime(p), self());
                    }
                })
                .match(SieveActors.End.class, foo -> {
                    // last prime
                    manager.tell(SieveActors.End.class, self());
                })
                .build();
    }
}
