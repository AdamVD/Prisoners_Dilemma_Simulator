import Strategies.*;

import java.util.*;

/**
 * Class to simulate evolution of strategies within the prisoner's dilemma game.
 */
public class Evolution {

    private static ArrayList<Prisoner> prisoners = new ArrayList<>();
    private static int numPrisoners;
    private static Random r = new Random();
    private static final int MAX_ROUNDS = 9;

    private static void beginEvolution(double weight) throws InstantiationException, IllegalAccessException {

        // variable initialization
        int generation = 1;
        Scanner s = new Scanner(System.in);
        String choice = "";

        System.out.println("Running evolutionary simulation with a weight of " + weight + " and " + numPrisoners + " prisoners.");
        System.out.println("Games will have between 1 and " + (MAX_ROUNDS + 1) + " rounds.");

        do {

            // matches each prisoner against every other prisoner
            for (int i = 0; i < prisoners.size(); i++) {

                Prisoner prisoner1 = prisoners.get(i);

                // count starts one after the first loop so that two players only play one another once
                for (int z = i + 1; z < prisoners.size(); z++) {

                    Prisoner prisoner2 = prisoners.get(z);

                    // starts a game between the two selected prisoners
                    InterrogationRoom game = new InterrogationRoom(r.nextInt(MAX_ROUNDS) + 1, prisoner1, prisoner2, weight);
                    game.simulateGame();

                }

            }
            // sort the list of prisoners by score in descending order
            prisoners.sort((p1, p2) -> Double.compare(p2.getCumulativeScore(), p1.getCumulativeScore()));

            int alwaysComply = 0;
            int titForTat = 0;
            int permanentRetaliation = 0;
            int alwaysExploit = 0;

            for (Prisoner prisoner : prisoners) {
                if (prisoner instanceof AlwaysComply)
                    alwaysComply++;
                else if (prisoner instanceof TitForTat)
                    titForTat++;
                else if (prisoner instanceof PermanentRetaliation)
                    permanentRetaliation++;
                else if (prisoner instanceof AlwaysExploit)
                    alwaysExploit++;
            }

            System.out.println("At the end of generation " + generation + " there are:\n" +
                    "\t" + permanentRetaliation + " Permanent Retaliation\n" +
                    "\t" + alwaysExploit + " Always Exploit");

            // kill the bottom half of the population
            for (int i=numPrisoners/2; i<numPrisoners; i++) {
                prisoners.remove(numPrisoners/2);
            }
            // reproduce and evolve the remaining prisoners
            for (int i=0; i<numPrisoners/2; i++) {
                prisoners.add(prisoners.get(i).evolve());
            }

            for (Prisoner prisoner : prisoners) {
                prisoner.notifyGenerationOver();
                prisoner.resetCumulativeScore();
            }

            generation++;

            if (!choice.equalsIgnoreCase("F")) {
                System.out.print("N for next or Q for quit, F to run until stopped: ");
                choice = s.nextLine();
            }

        } while(choice.equalsIgnoreCase("N") || choice.equalsIgnoreCase("F"));

    }

    /**
     * Creates new prisoners to begin the evolutionary simulation.
     * @param numberPrisoners number of prisoners to be in simulation
     */
    private static void createPrisoners(int numberPrisoners) {
        numPrisoners = numberPrisoners;

        for (int i=1; i<=numPrisoners; i+=1) {
            prisoners.add(new AlwaysComply());
        }

    }

    public static void main(String[] args) {
        createPrisoners(Integer.parseInt(args[0]));
        try {
            beginEvolution(Double.parseDouble(args[1]));
        } catch (InstantiationException e) {
            System.out.println("Cannot evolve prisoner classes with a custom constructor!");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
