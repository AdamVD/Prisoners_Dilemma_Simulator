package Strategies;

/**
 * Created to test a prisoner with no default constructor.
 * THIS IS AN INCORRECT PRISONER IMPLEMENTATION THAT WILL BREAK THE SIMULATOR.
 * @author AdamVD  avdonle@gmail.com
 */
public class NoDefaultConstructor extends Prisoner {

    private boolean choice;

    /**
     * This is the incorrect way of creating a prisoner. It must have a default constructor.
     */
    public NoDefaultConstructor(boolean choice) {
        this.choice = choice;
    }

    /**
     * Gets the next decision for this prisoner.
     *
     * @return true if the prisoner exploits, false if complies
     */
    @Override
    public boolean choose() {
        return choice;
    }
}
