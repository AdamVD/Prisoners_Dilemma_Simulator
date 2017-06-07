package Strategies;

/**
 * Created to do testing with multiple constructors.
 * THIS IS A CORRECT PRISONER IMPLEMENTATION THAT WILL FUNCTION PROPERLY.
 * @author AdamVD  avdonle@gmail.com
 */
public class MultipleConstructor extends Prisoner {

    private boolean choice;

    /**
     * Proper way to call a parametrized constructor from the required default constructor.
     */
    public MultipleConstructor() {
        // send values to the other constructor (may be randomized).
        this(true);
    }

    public MultipleConstructor(boolean choice) {
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

    /**
     * Creates an offspring prisoner with new characteristics.
     * You may choose to implement evolve as so, calling the parametrized constructor, or you may let the simulator
     * call the default constructor by not overriding this method.
     */
    @Override
    public Prisoner evolve() {
        return new MultipleConstructor(false);
    }
}
