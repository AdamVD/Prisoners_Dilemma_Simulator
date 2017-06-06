package Strategies;

import java.text.DecimalFormat;

/**
 * Class representing the structure required for a prisoner.
 * Created by avdon on 5/12/2017.
 */
public abstract class Prisoner {

    final boolean EXPLOIT = true;
    final boolean COMPLY = false;
    private double score=0;
    private double cumulativeScore=0;

    /**
     * Gets the next decision for this prisoner.
     * @return true if the prisoner exploits, false if complies
     */
    public abstract boolean choose();

    /**
     * Updates this prisoner's cumulative score.
     * @param result points earned during the last round of the game
     */
    public final void updateScore(double result) {
        score += result;
        cumulativeScore +=result;
    }

    /**
     * Getter for prisoner score.
     * @return the score for this prisoner
     */
    public final double getScore() {
        return score;
    }

    /**
     * Getter for cumulative score.
     * @return the cumulative score of this prisoner throughout all games
     */
    public final double getCumulativeScore() {
        return cumulativeScore;
    }

    /**
     * Notifies this prisoner of the other prisoner's choice in the round.
     * @param choice choice of the opposing player
     */
    public void notifyOpponentChoice(boolean choice) {}

    /**
     * Called when the game is over so the prisoner may reset relevant information.
     */
    public void notifyGameOver() {}

    /**
     * Called when a game ends to reset the game score counter
     */
    public final void resetGameScore() {
        score = 0;
    }

    /**
     * Called when the current generation has ended.
     */
    public void notifyGenerationOver() {}

    /**
     * Called when a generation ends to reset the cumulative score.
     */
    public final void resetCumulativeScore() {
        cumulativeScore = 0;
    }

    /**
     * Called at the start of any game. Allows a prisoner strategy to keep a memory of who they played against.
     * @param prisonerObjectHash unique integer hash code value of the prisoner to be used as identification
     */
    public void notifyOtherPrisoner(int prisonerObjectHash) {
    }

    /**
     * Duplicate prisoner and evolve with new traits if applicable.
     * @return new prisoner object
     */
    public Prisoner evolve() throws IllegalAccessException, InstantiationException {
        return this.getClass().newInstance();
    }

    @Override
    public final String toString() {
        DecimalFormat round = new DecimalFormat("#,###.##");
        return this.getClass().getSimpleName() + " with a score of " + round.format(cumulativeScore);
    }

}
