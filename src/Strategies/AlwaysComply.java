package Strategies;

/**
 * Strategies.Prisoner that always complies.
 */
public class AlwaysComply extends Prisoner {

    /**
     * Gets the next decision for this prisoner.
     *
     * @return true if the prisoner exploits, false if complies
     */
    @Override
    public boolean choose() {
        return COMPLY;
    }

    /**
     * Duplicate prisoner and evolve with new traits if applicable.
     *
     * @return new prisoner object
     */
    @Override
    public Prisoner evolve() {
        return new AlwaysComply();
    }

    @Override
    public String toString() {
        return "Always Comply with cumulative score: " + cumulativeScore;
    }
}
