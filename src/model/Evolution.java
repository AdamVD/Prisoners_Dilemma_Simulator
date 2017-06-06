package model;

import Strategies.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Class to simulate evolution of strategies within the prisoner's dilemma game.
 */
public class Evolution {

    /** The list of all prisoner objects in the simulation */
    private ArrayList<Prisoner> prisoners = new ArrayList<>();
    /** The mapping giving the number of each prisoner strategy to add to this simulation */
    private HashMap<String, Integer> prisonerPopulations;
    /** Total number of prisoners in this simulation */
    private final int numPrisoners;
    /** The random number generator for this object */
    private Random r;
    /** The maximum number of rounds in a given "game" between two prisoners */
    private final int MAX_ROUNDS;
    /** The minimum number of rounds in a given "game" between two prisoners */
    private final int MIN_ROUNDS;
    /** The current generation */
    private int generation = 0;
    /** Weight used to calculate payouts to players 0<w<=1 */
    private double weight;
    /** Should the weight be randomized? */
    private final boolean randWeight;
    /** Payoff to a player that exploits while the other complies */
    final double EXPLOIT_COMPLY;
    /** Payoff to a player that complies while the other exploits */
    final double COMPLY_EXPLOIT;
    /** Payoff to players if they both comply */
    final double COMPLY_COMPLY;
    /** Payoff to players if they both exploit */
    final double EXPLOIT_EXPLOIT;

    /**
     * Create an evolutionary simulator.
     * @param prisonerInitialization mapping of prisoner strategy class names to the number of that strategy to be created
     * @param maxRounds maximum number of rounds to occur in each "game" between two prisoners
     * @param minRounds minimum number of rounds to occur in each "game" between two prisoners, for a constant number of
     *                  rounds: maxRounds = minRounds + 1
     * @param randSeed seed for the random number generator, null if no seed
     * @param weight weight to be used in calculating the payoff to players each round, null if should be random
     * @param exploit_comply base payoff to the exploiter if the other prisoner complies
     * @param comply_exploit base payoff to the player that complied if the other prisoner exploits
     * @param comply_comply base payoff if both prisoners comply
     * @param exploit_exploit base payoff if both prisoners exploit
     * @throws ClassNotFoundException from addPrisoners()
     * @throws NoSuchMethodException from addPrisoners()
     * @throws IllegalAccessException from addPrisoners()
     * @throws InstantiationException from addPrisoners()
     * @throws InvocationTargetException from addPrisoners()
     */
    public Evolution(HashMap<String, Integer> prisonerInitialization, int maxRounds, int minRounds, Integer randSeed,
                     Double weight, double exploit_comply, double comply_exploit, double comply_comply, double exploit_exploit)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {

        if (randSeed != null)
            this.r = new Random(randSeed);
        else
            this.r = new Random();

        if (weight != null) {
            this.weight = weight;
            this.randWeight = false;
        } else {
            this.randWeight = true;
        }

        this.prisonerPopulations = new HashMap<>();
        this.MAX_ROUNDS = maxRounds;
        this.MIN_ROUNDS = minRounds;
        this.EXPLOIT_COMPLY = exploit_comply;
        this.COMPLY_EXPLOIT = comply_exploit;
        this.COMPLY_COMPLY = comply_comply;
        this.EXPLOIT_EXPLOIT = exploit_exploit;

        int cumulativePrisoners=0;
        // create the prisoner objects based on the initialization dictionary values
        for (HashMap.Entry<String, Integer> entry : prisonerInitialization.entrySet()) {
            String className = entry.getKey();
            int numToAdd = entry.getValue();
            cumulativePrisoners += numToAdd;
            addPrisoners(className, numToAdd);
        }
        this.numPrisoners = cumulativePrisoners;
    }

    /**
     * Adds a single type (strategy) of prisoners to the list for the generation.
     * @param className name of the prisoner class
     * @param numToAdd number of these prisoners to add
     * @throws ClassNotFoundException could not find the strategy class based on the string given
     * @throws NoSuchMethodException could not acquire the constructor (no default constructor given)
     */
    void addPrisoners(String className, int numToAdd) throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {

        // cannot add prisoners in the middle of the simulation
        if (generation == 0 && numToAdd > 0) {
            if (prisonerPopulations.containsKey(className))
                prisonerPopulations.replace(className, prisonerPopulations.get(className) + numToAdd);
            else
                prisonerPopulations.put(className, numToAdd);

            Class strategy = Class.forName("Strategies." + className.trim());
            Constructor constructor = strategy.getConstructor();

            for (int i = 0; i < numToAdd; i++) {
                prisoners.add((Prisoner) constructor.newInstance());
            }
        }
    }


    /**
     * Runs simulation of a single generation.
     * @throws InstantiationException thrown when a failure occurs in the Prisoner.evolve() method
     * @throws IllegalAccessException thrown when a failure occurs in the Prisoner.evolve() method
     */
    public void doGeneration() throws InstantiationException, IllegalAccessException {
        // matches each prisoner against every other prisoner
        for (int i = 0; i < prisoners.size(); i++) {

            Prisoner prisoner1 = prisoners.get(i);

            // count starts one after the first loop so that two players only play one another once
            for (int z = i + 1; z < prisoners.size(); z++) {

                Prisoner prisoner2 = prisoners.get(z);

                // if weight should be randomized, generate a value such that 0<weight<=1
                if (randWeight)
                    weight = Double.MIN_VALUE + r.nextDouble();

                // starts a game between the two selected prisoners
                InterrogationRoom game = new InterrogationRoom(r.nextInt(MAX_ROUNDS - MIN_ROUNDS) + MIN_ROUNDS,
                        prisoner1, prisoner2, weight, this);
                game.simulateGame();
            }
        }
        // sort the list of prisoners by score in descending order
        prisoners.sort((p1, p2) -> Double.compare(p2.getCumulativeScore(), p1.getCumulativeScore()));

        // kill the bottom half of the population
        for (int i=numPrisoners/2; i<numPrisoners; i++) {
            prisoners.remove(numPrisoners/2);
        }
        // reproduce and evolve the remaining prisoners
        for (int i=0; i<numPrisoners/2; i++) {
            prisoners.add(prisoners.get(i).evolve());
        }

        // reset the population data
        for (String className : prisonerPopulations.keySet())
            prisonerPopulations.replace(className, 0);

        // notify prisoners that the generation has ended
        // also update the populations map with the current population
        for (Prisoner prisoner : prisoners) {
            prisoner.notifyGenerationOver();
            prisoner.resetCumulativeScore();
            prisonerPopulations.replace(prisoner.getClass().getSimpleName(),
                                prisonerPopulations.get(prisoner.getClass().getSimpleName()) + 1);
        }

        generation++;
    }

    /**
     * Command line interface for the simulator.
     * @throws InstantiationException thrown when a failure occurs in the Prisoner.evolve() method
     * @throws IllegalAccessException thrown when a failure occurs in the Prisoner.evolve() method
     */
    public void beginEvolution() throws InstantiationException, IllegalAccessException {

        // variable initialization
        Scanner s = new Scanner(System.in);
        String choice = "";

        System.out.println("Running evolutionary simulation with a weight of " + (randWeight? "random":weight) + " and " + numPrisoners + " prisoners.");
        System.out.println("Games will have between " + MIN_ROUNDS + " and " + MAX_ROUNDS + " rounds.");

        System.out.println("The starting population is: " + prisonerPopulations);

        do {

            doGeneration();

            System.out.println("\nAt the conclusion of generation " + generation + ", the population is: " + prisonerPopulations);

            if (!choice.equalsIgnoreCase("F")) {
                System.out.print("N for next or Q for quit, F to run until stopped: ");
                choice = s.nextLine();
            }

        } while(choice.equalsIgnoreCase("N") || choice.equalsIgnoreCase("F"));

    }

    /**
     * Getter for prisoner population sizes.
     */
    public HashMap getPopulation() {
        return prisonerPopulations;
    }

    public ArrayList getPrisoners() {
        return prisoners;
    }

}
