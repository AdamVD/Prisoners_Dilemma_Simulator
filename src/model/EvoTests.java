package model;

import Strategies.*;
import junit.framework.TestCase;
import org.junit.Before;

import java.util.HashMap;

import static org.junit.Assert.assertNotEquals;

/**
 * Class for testing of the simulation program.
 * Created by Adam on 5/12/2017.
 */
public class EvoTests extends TestCase {

    Evolution evo;
    Prisoner prisoner1;
    Prisoner prisoner2;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        evo = new Evolution(new HashMap<>(), 5, 4, 1, 1.,
                10, 0, 7, 3);
        prisoner1 = new AlwaysComply();
        prisoner2 = new AlwaysComply();
    }

    /**
     * Tests various prisoner's dilemma game scenarios for proper score results.
     */
    public void testInterrogationRoom() {
        InterrogationRoom room = new InterrogationRoom(4, prisoner1, prisoner2, 1., evo);

        room.simulateGame();
        assertEquals("Both comply, 4 rounds, weight 1", 28., prisoner1.getCumulativeScore());
        assertEquals("Both comply, 4 rounds, weight 1", 28., prisoner2.getCumulativeScore());

        prisoner1.resetCumulativeScore();
        prisoner2.resetCumulativeScore();
        room = new InterrogationRoom(4, prisoner1, prisoner2, .75, evo);

        room.simulateGame();
        assertEquals("Both comply, 4 rounds, weight .75", 19.140625, prisoner1.getCumulativeScore());
        assertEquals("Both comply, 4 rounds, weight .75", 19.140625, prisoner2.getCumulativeScore());

        prisoner1.resetCumulativeScore();
        prisoner2.resetCumulativeScore();
        room = new InterrogationRoom(1, prisoner1, prisoner2, .75, evo);

        room.simulateGame();
        assertEquals("Both comply, 1 round, weight .75", 7., prisoner1.getCumulativeScore());
        assertEquals("Both comply, 1 round, weight .75", 7., prisoner2.getCumulativeScore());

        prisoner1 = new AlwaysExploit();
        prisoner2 = new AlwaysComply();
        room = new InterrogationRoom(1, prisoner1, prisoner2, .75, evo);

        room.simulateGame();
        assertEquals("AlwaysExploit, 1 round, weight .75", 10., prisoner1.getCumulativeScore());
        assertEquals("AlwaysComply, 1 round, weight .75", 0., prisoner2.getCumulativeScore());
    }

    /**
     * General testing of all relevant prisoner methods.
     */
    public void testPrisonerMethods() throws Exception {
        assertFalse("AlwaysComply choose()", prisoner1.choose());

        prisoner1.updateScore(10);
        assertEquals("Updated score", 10., prisoner1.getScore());
        assertEquals("Updated cumulative score", 10., prisoner1.getCumulativeScore());

        prisoner1.resetGameScore();
        assertEquals("Reset game score", 0., prisoner1.getScore());
        prisoner1.resetCumulativeScore();
        assertEquals("Reset cumulative score", 0., prisoner1.getCumulativeScore());

        prisoner2 = prisoner1.evolve();
        assertNotEquals("Prisoner evolve", prisoner1, prisoner2);
        assertEquals("Prisoner evolve", prisoner1.getClass(), prisoner2.getClass());

        prisoner1.updateScore(110);
        assertEquals("Prisoner toString", "AlwaysComply with a score of 110", prisoner1.toString());
    }

    /**
     * Attempts prisoner creation directly through the Evolution.addPrisoners() method.
     */
    public void testAddPrisonerByClassNames() throws Exception {
        assertEquals("Population initially empty", evo.getPopulation(), new HashMap());

        evo.addPrisoners("AlwaysComply", 5);
        evo.addPrisoners("AlwaysComply", 10);  // this second entry adds on to the first
        evo.addPrisoners("AlwaysExploit", 15);
        assertEquals("Population hash map size", 2, evo.getPopulation().size());
        assertEquals("AlwaysComply size", 15, evo.getPopulation().get("AlwaysComply"));
        assertEquals("AlwaysExploit size", 15, evo.getPopulation().get("AlwaysExploit"));
        assertEquals("Prisoner list size", 30, evo.getPrisoners().size());
    }

    /**
     * Attempts prisoner creation through the hashMap parameter of the Evolution constructor.
     */
    public void testAddPrisonerByConstructorHash() throws Exception {
        HashMap<String, Integer> popInit = new HashMap<>();
        popInit.put("AlwaysComply", 5);
        popInit.put("AlwaysComply", 10);  // only the last entry is kept
        popInit.put("AlwaysExploit", 15);

        evo = new Evolution(popInit, 5, 4, 1, 1.,
                10, 0, 7, 3);

        assertEquals("Population hash map size", 2, evo.getPopulation().size());
        assertEquals("AlwaysComply size", 10, evo.getPopulation().get("AlwaysComply"));
        assertEquals("AlwaysExploit size", 15, evo.getPopulation().get("AlwaysExploit"));
        assertEquals("Prisoner list size", 25, evo.getPrisoners().size());
    }

    /**
     * Tests trying to addPrisoners() with a strategy name that does not exist.
     */
    public void testNonexistentPrisonerName() throws Exception {
        try {
            evo.addPrisoners("DoesNotExist", 45);
            assertTrue("Should throw ClassNotFound exception", false);
        } catch(ClassNotFoundException e) {
            assertTrue(true);
        }
    }

    /**
     * Tries to initialize an invalid prisoner with no default constructor from the addPrisoners() method.
     */
    public void testInitializePrisonerWithNoDefault() throws Exception {
        try {
            evo.addPrisoners("NoDefaultConstructor", 5);
            assertTrue("Should throw NoSuchMethod exception", false);
        } catch(NoSuchMethodException e) {
            assertTrue(true);
        }
    }

    /**
     * Initializes a valid prisoner with multiple constructors from the addPrisoners() method.
     * @throws Exception test has failed if exception thrown
     */
    public void testInitializePrisonerWithMultipleConstructor() throws Exception {
        evo.addPrisoners("MultipleConstructor", 5);
    }

    /**
     * Tries to evolve a prisoner that does not contain a default constructor or override evolve().
     */
    public void testEvolvePrisonerWithNoDefault() throws Exception {
        try {
            prisoner1 = new NoDefaultConstructor(true);
            prisoner1.evolve();
            assertTrue("Should throw InstantiationException", false);
        } catch (InstantiationException e) {
            assertTrue(true);
        }
    }

    /**
     * Calls a correctly implemented prisoner with multiple constructors (one default, one parametrized).
     * Upon evolution the new prisoner's choice should be false (initial prisoner was true).
     * @throws Exception test has failed if exception thrown
     */
    public void testEvolvePrisonerWithMultipleConstructor() throws Exception {
        prisoner1 = new MultipleConstructor();
        assertTrue(prisoner1.choose());

        prisoner2 = prisoner1.evolve();
        assertFalse(prisoner2.choose());
    }
}
