import java.io.IOException;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * @author Joshua Chen
 * Date: Nov 04, 2018
 */
public class SieveActors {
    static class Start {
        public int num;

        public Start(int num) {
            this.num = num;
        }
    }

    static class End{}

    public static void main(String[] args) throws IOException {
        final int n = 1000000;

        final ActorSystem sieveSystem = ActorSystem.create("SieveOfEratosthenes");

        ActorRef sManager = sieveSystem.actorOf(Props.create(SieveManager.class));
        sManager.tell(new Start(n), ActorRef.noSender());

        System.out.println(">>> Press ENTER to exit <<<");

        try {
            System.in.read();
        } finally {
            sieveSystem.terminate();
        }

    }
}
