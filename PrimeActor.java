import akka.actor.*;

/**
 * @author Joshua Chen
 *         Date: Nov 04, 2018
 */
public class PrimeActor extends AbstractActor {

    static public Props props(int currentPrime, int N, ActorRef manager) {
        return Props.create(PrimeActor.class, () -> new PrimeActor(currentPrime, N, manager));
    }

    static class End{}

    private int currentPrime;
    private int N;
    private ActorRef manager;
    private ActorRef nextPrime;
    private boolean needNextPrime;

    public PrimeActor(int currentPrime, int n, ActorRef manager) {
        this.currentPrime = currentPrime;
        N = n;
        this.manager = manager;
        this.nextPrime = null;
        needNextPrime = true;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, num -> {
                    int p = num;

                    if (p > Math.sqrt(N)) {
                        getContext().become(receiveBuilder()
                                .match(Integer.class, RemainingPrime -> {
                                    // Updates manager about new prime
                                    if (p % currentPrime != 0) {
                                        manager.tell(new SieveManager.NewPrime(p), ActorRef.noSender());
                                    }
                                    getContext().unbecome();
                                })
                                // last prime
                                .match(End.class, lastPrime -> {
                                    manager.tell(new End(), ActorRef.noSender());
                                    getContext().unbecome();
                                })
                                .build());
                    } else {
                        if (needNextPrime) {
                            nextPrime = getContext().actorOf(PrimeActor.props(p, N, manager));
                            manager.tell(new SieveManager.NewPrime(p), ActorRef.noSender());
                            needNextPrime = false;
                        }
                        getContext().become(receiveBuilder()
                                .match(Integer.class, AfterPrime -> {
                                    // Forward next num to next prime
                                    if (p % currentPrime != 0) {
                                        nextPrime.tell(num, ActorRef.noSender());
                                    }
                                    getContext().unbecome();
                                })
                                // Received an End from the previous actor/prime
                                // Notify the next actor/prime that no more numbers will be sent
                                .match(End.class, endOfNums -> {
                                    nextPrime.tell(new End(), ActorRef.noSender());
                                    getContext().unbecome();
                                })
                                .build());
                    }
                })
                .match(End.class, foo -> {
                    manager.tell(new End(), ActorRef.noSender());
                })
                .build();
    }
}