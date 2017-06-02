package Strategies;

/**
 * Strategies.Prisoner that plays whatever the opponent played in the previous round.
 */
public class TitForTat extends Prisoner {

    private boolean prevOppMove = COMPLY;

    /**
     * Gets the next decision for this prisoner.
     *
     * @return true if the prisoner exploits, false if complies
     */
    @Override
    public boolean choose() {
        return prevOppMove;
    }

    /**
     * Notifies this prisoner of the other prisoner's choice in the round.
     *
     * @param choice choice of the opposing player
     */
    @Override
    public void notifyOpponentChoice(boolean choice) {
        prevOppMove = choice;
    }

    /**
     * Called when the game is over so the prisoner may reset relevant information.
     */
    @Override
    public void notifyGameOver() {
        prevOppMove = COMPLY;
    }
}
