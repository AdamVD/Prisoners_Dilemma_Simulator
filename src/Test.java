import Strategies.AlwaysExploit;
import Strategies.PermanentRetaliation;
import Strategies.Prisoner;

/**
 * Class for testing of the simulation program.
 * Created by Adam on 5/12/2017.
 */
public class Test {

    public static void main(String[] args) {
        Prisoner prisoner1 = new AlwaysExploit();
        Prisoner prisoner2 = new PermanentRetaliation();
        InterrogationRoom simulator = new InterrogationRoom(6, prisoner1, prisoner2, .5);

        simulator.simulateGame();

        System.out.println("Prisoner1 (Always Exploit) has a score: " + prisoner1.getScore());
        System.out.println("Prisoner2 (Strategies.PermanentRetaliation) has a score: " + prisoner2.getScore());
    }
}
