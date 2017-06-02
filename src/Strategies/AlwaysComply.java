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
}
