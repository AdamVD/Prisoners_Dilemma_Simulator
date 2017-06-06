package console_UI;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import model.Evolution;

/**
 * This is a simple class to run the Prisoner's Dilemma Evolutionary Simulator from the console.
 * Important variables to the execution of the simulator will be stored within this class, the main method will be
 * runnable without the use of command line arguments.
 * @author AdamVD  avdonle@gmail.com
 */
public class EvolutionTUI {

    /** The parameters to control the evolutionary simulator */
    final static HashMap<String, Integer> prisonerInitialization = new HashMap<>();
    static{  // add values to the map with the format ("strategy_name", integer number of this type)
        prisonerInitialization.put("AlwaysExploit", 500);
        prisonerInitialization.put("TitForTat", 500);
        prisonerInitialization.put("PermanentRetaliation", 1000);
    }
    /** Max rounds, exclusive. For constant value: maxRounds = minRounds + 1 */
    final static int maxRounds = 10;
    /** Min rounds, inclusive */
    final static int minRounds = 1;
    /** Weight to calculate score. Null if should be randomized*/
    final static Double weight = .75;
    /** Random number generator seed for number of rounds and weight, can be null if not specified */
    final static Integer seed = 1;
    /** Payoff to a player that exploits while the other complies */
    final static double EXPLOIT_COMPLY = 10;
    /** Payoff to a player that complies while the other exploits */
    final static double COMPLY_EXPLOIT = 0;
    /** Payoff to players if they both comply */
    final static double COMPLY_COMPLY = 7;
    /** Payoff to players if they both exploit */
    final static double EXPLOIT_EXPLOIT = 3;

    /**
     * Main method to run the evolutionary simulator.
     * @param args unused
     */
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Evolution evo = new Evolution(prisonerInitialization, maxRounds, minRounds, seed, weight,
                EXPLOIT_COMPLY, COMPLY_EXPLOIT, COMPLY_COMPLY, EXPLOIT_EXPLOIT);
        evo.beginEvolution();
    }
}
