import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * @author Joshua Chen
 *         Date: Nov 04, 2018
 */
public class PrimeActor extends AbstractActor {

    static public Props props(String message, ActorRef printerActor) {
        return Props.create(PrimeActor.class, () -> new PrimeActor(message, printerActor));
    }

    private final String message;
    private final ActorRef printerActor;

    public PrimeActor(String message, ActorRef printerActor) {
        this.message = message;
        this.printerActor = printerActor;
    }

    @Override
    public Receive createReceive() {
        return null;
    }
}
