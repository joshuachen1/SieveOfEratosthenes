import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.io.IOException;

/**
 * @author Joshua Chen
 * Date: Nov 05, 2018
 *
 * Main Class that runs the Actor System.
 * Terminates Actor System when Enter Key is pressed.
 */
public class SieveActors {
    static class Start {
        public int num;

        public Start(int num) {
            this.num = num;
        }
    }
    public static void main(String[] args) {
        final ActorSystem sieveSystem = ActorSystem.create("SieveOfEratosthenes");
        final int N = 1_000_000;
        long timeIn, timeOut = 0;

        ActorRef sManager = sieveSystem.actorOf(SieveManager.props(), "Manager");

        try {
            timeIn = System.nanoTime();
            sManager.tell(new Start(N), ActorRef.noSender());
            timeOut = System.nanoTime() - timeIn;
            System.out.println("Press Enter to End Program");
            System.in.read();
        } catch (IOException e) {

        } finally {
            sieveSystem.terminate();
            System.out.printf("Time for Sieve Actors: %.2f (ms)", timeOut * 1e-6);
        }
    }
}
