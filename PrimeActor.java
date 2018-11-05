import akka.actor.*;
import scala.Int;

/**
 * @author Joshua Chen
 *         Date: Nov 04, 2018
 */
public class PrimeActor extends AbstractLoggingActor {

    static public Props props(boolean[] isPrime, int localPrime, int N, ActorRef manager) {
        return Props.create(PrimeActor.class, () -> new PrimeActor(isPrime, localPrime, N, manager));
    }

    static class Begin {}
    static class End{}

    private boolean[] isPrime;
    private int localPrime;
    private int N;
    private ActorRef manager;


    public PrimeActor(boolean[] isPrime, int localPrime, int N, ActorRef manager) {
        this.isPrime = isPrime;
        this.localPrime = localPrime;
        this.N = N;
        this.manager = manager;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Begin.class, num -> {
                    boolean nextActorBegins = false;

                    if (localPrime > Math.sqrt(N)) {
                        for (int i = localPrime; i <= N; i++) {
                            if (isPrime[i]) {
                                System.out.println(i + " is prime.");
                            }
                        }
                        manager.tell(new PrimeActor.End(), ActorRef.noSender());
                    }

                    else if (localPrime != -1) {
                        for (int j = localPrime; localPrime * j <= N; j++) {
                            isPrime[localPrime * j] = false;

                            // Begins when half way through list
                            if (!nextActorBegins && localPrime * j >= N / 2) {
                                ActorRef nextActor = getContext().actorOf(PrimeActor.props(isPrime, getNextLocalPrime(localPrime), N, manager));
                                nextActor.tell(new Begin(), ActorRef.noSender());
                                nextActorBegins = true;
                            }
                        }
                    }


                }).build();
    }

    private int getNextLocalPrime(int currentLocalPrime) {
        for (int i = currentLocalPrime + 1; i <= N; i++) {
            if (isPrime[i]) {
                return i;
            }
        }
        return -1;
    }
}