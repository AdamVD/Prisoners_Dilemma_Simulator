package model;

import Strategies.Prisoner;
import model.Evolution;

/**
 * Class that simulates the iterative prisoner's dilemma.
 * Created by Adam on 5/12/2017.
 */
class InterrogationRoom {

    /** The model.Evolution class that is managing this interrogation room */
    Evolution evo;
    /** Weight or 'discount parameter' for successive turns */
    private final double weight;
    /** The first prisoner */
    private Prisoner prisoner1;
    /** The second prisoner */
    private Prisoner prisoner2;
    /** Number of turns in this game */
    private int numTurns;

    /** Create an interrogation room to play out a game between two prisoners */
    InterrogationRoom(int numTurns, Prisoner prisoner1, Prisoner prisoner2, double weight, Evolution evo) {
        this.numTurns = numTurns;
        this.prisoner1 = prisoner1;
        this.prisoner2 = prisoner2;
        this.weight = weight;
        this.evo = evo;
    }

    /**
     * Runs a game between the two prisoners.
     */
     void simulateGame() {

        // gives the prisoners a unique identifier for the opponent
        prisoner1.notifyOtherPrisoner(prisoner2.hashCode());
        prisoner2.notifyOtherPrisoner(prisoner1.hashCode());

        for (int currRound = 1; currRound <= numTurns; currRound++) {

            // calculates the weight to apply to the scores this round
            double discountParam = Math.pow(weight, currRound - 1);

            boolean prisoner1Choice = prisoner1.choose();
            boolean prisoner2Choice = prisoner2.choose();

            // both prisoners exploit
            if (prisoner1Choice && prisoner2Choice) {
                prisoner1.updateScore(evo.EXPLOIT_EXPLOIT * discountParam);
                prisoner2.updateScore(evo.EXPLOIT_EXPLOIT * discountParam);
            }
            // prisoner1 exploit, prisoner2 comply
            else if (prisoner1Choice && !prisoner2Choice) {
                prisoner1.updateScore(evo.EXPLOIT_COMPLY * discountParam);
                prisoner2.updateScore(evo.COMPLY_EXPLOIT * discountParam);
            }
            // prisoner1 comply, prisoner2 exploit
            else if (!prisoner1Choice && prisoner2Choice) {
                prisoner1.updateScore(evo.COMPLY_EXPLOIT * discountParam);
                prisoner2.updateScore(evo.EXPLOIT_COMPLY * discountParam);
            }
            // both comply
            else {
                prisoner1.updateScore(evo.COMPLY_COMPLY * discountParam);
                prisoner2.updateScore(evo.COMPLY_COMPLY * discountParam);
            }

            // notifies the prisoners of their opponent's choice
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
