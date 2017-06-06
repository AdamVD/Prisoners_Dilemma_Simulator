package model;

import Strategies.AlwaysComply;
import Strategies.AlwaysExploit;
import Strategies.Prisoner;
import junit.framework.TestCase;
import org.junit.Before;

import java.util.HashMap;

import static org.junit.Assert.*;

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

    public void testAddPrisonerByClassNames() throws Exception {
        assertEquals("Population initially empty", evo.getPopulation(), new HashMap());

        evo.addPrisoners("AlwaysComply", 5);
        evo.addPrisoners("AlwaysComply", 10);
        evo.addPrisoners("AlwaysExploit", 15);
        assertEquals("Population hash size", 2, evo.getPopulation().size());
        assertEquals("AlwaysComply size", 15, evo.getPopulation().get("AlwaysComply"));
        assertEquals("AlwaysExploit size", 15, evo.getPopulation().get("AlwaysExploit"));
        assertEquals("Prisoner list size", 30, evo.getPrisoners().size());
    }
}
