import Strategies.Prisoner;

/**
 * Class that simulates the iterative prisoner's dilemma.
 * Created by Adam on 5/12/2017.
 */
public class InterrogationRoom {

    /** Payoff to a player that exploits while the other complies */
    final double EXPLOIT_COMPLY = 11;
    /** Payoff to a player that complies while the other exploits */
    final double COMPLY_EXPLOIT = 0;
    /** Payoff to players if they both comply */
    final double COMPLY_COMPLY = 7;
    /** Payoff to players if they both exploit */
    final double EXPLOIT_EXPLOIT = 3;

    /** Weight or 'discount parameter' for successive turns */
    private final double weight;
    /** The first prisoner */
    private Prisoner prisoner1;
    /** The second prisoner */
    private Prisoner prisoner2;
    /** Number of turns in this game */
    private int numTurns;

    /** Create an interrogation room to play out a game between two prisoners */
    public InterrogationRoom(int numTurns, Prisoner prisoner1, Prisoner prisoner2, double weight) {
        this.numTurns = numTurns;
        this.prisoner1 = prisoner1;
        this.prisoner2 = prisoner2;
        this.weight = weight;
    }

    /**
     * Runs a game between the two prisoners.
     */
    public void simulateGame() {

        for (int currRound = 1; currRound <= numTurns; currRound++) {

            double discountParam = Math.pow(weight, currRound - 1);
            boolean prisoner1Choice = prisoner1.choose();
            boolean prisoner2Choice = prisoner2.choose();

            // both prisoners exploit
            if (prisoner1Choice && prisoner2Choice) {
                prisoner1.updateScore(EXPLOIT_EXPLOIT * discountParam);
                prisoner2.updateScore(EXPLOIT_EXPLOIT * discountParam);
            }
            // prisoner1 exploit, prisoner2 comply
            else if (prisoner1Choice && !prisoner2Choice) {
                prisoner1.updateScore(EXPLOIT_COMPLY * discountParam);
                prisoner2.updateScore(COMPLY_EXPLOIT * discountParam);
            }
            // prisoner1 comply, prisoner2 exploit
            else if (!prisoner1Choice && prisoner2Choice) {
                prisoner1.updateScore(COMPLY_EXPLOIT * discountParam);
                prisoner2.updateScore(EXPLOIT_COMPLY * discountParam);
            }
            // both comply
            else {
                prisoner1.updateScore(COMPLY_COMPLY * discountParam);
                prisoner2.updateScore(COMPLY_COMPLY * discountParam);
            }

            prisoner1.notifyOpponentChoice(prisoner2Choice);
            prisoner2.notifyOpponentChoice(prisoner1Choice);

        }
        // notifies the prisoners that the game has ended
        prisoner1.notifyGameOver();
        prisoner1.resetGameScore();
        prisoner2.notifyGameOver();
        prisoner2.resetGameScore();
    }
}
