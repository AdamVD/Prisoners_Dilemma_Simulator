package Strategies;

/**
 * Strategies.Prisoner that complies until it is exploited, then the prisoner always exploits.
 */
public class PermanentRetaliation extends Prisoner {

    private boolean beenExploited = false;

    /**
     * Gets the next decision for this prisoner.
     *
     * @return true if the prisoner exploits, false if complies
     */
    @Override
    public boolean choose() {
        return beenExploited; // will comply until the opponent has exploited
    }

    /**
     * Notifies this prisoner of the other prisoner's choice in the round.
     *
     * @param choice choice of the opposing player
     */
    @Override
    public void notifyOpponentChoice(boolean choice) {
        beenExploited = choice;
    }

    /**
     * Called when the game is over so the prisoner may reset relevant information.
     */
    @Override
    public void notifyGameOver() {
        beenExploited = false;
    }

    @Override
    public String toString() {
        return "Permanent Retaliation with cumulative score: " + getCumulativeScore();
    }
}
