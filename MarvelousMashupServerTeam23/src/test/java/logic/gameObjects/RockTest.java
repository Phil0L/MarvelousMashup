package logic.gameObjects;

import logic.model.TestUsingModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class has test methods for the Rock class.
 * @author Luka Stoehr
 */
public class RockTest extends TestUsingModel {
    /**
     * Tests the move() method, which should always return false as
     * rocks cannot move.
     * @author Luka Stoehr
     */
    @Test
    public void moveTest(){
        createFreshModel();

        Rock rock = new Rock(model);
        rock.place(new Position(3,4));
        assertEquals(new Position(3,4), rock.getPosition());
        assertFalse(rock.move(new Position(2,4)));    //Rocks can't be moved
    }

    /**
     * Tests the attacked() method of a rock.
     * @author Luka Stoehr
     */
    @Test
    public void attacked(){
        createFreshModel();

        Rock rock = new Rock(model);
        rock.place(new Position(8,7));
        assertEquals(new Position(8,7), rock.getPosition());
        assertEquals(100, rock.healthPoints);

        assertEquals(30,rock.attacked(30));
        assertEquals(70, rock.healthPoints);
        assertEquals(70, rock.attacked(110));
        assertEquals(0, rock.healthPoints);
        assertNull(rock.getPosition());
    }

    /**
     * Tests if the Rock.rockCount is incremented after each creation of a Rock
     * and whether the Rock ID is set correctly.
     * @author Luka Stoehr
     */
    @Test
    public void rockCounterTest(){
        createFreshModel();
        int rockCount0 = Rock.rockCount;
        Rock rock1 = new Rock(model);
        assertEquals(rockCount0+1, Rock.rockCount);
        assertEquals(rockCount0, rock1.ID);
        Rock rock2 = new Rock(model);
        assertEquals(rockCount0+2, Rock.rockCount);
        assertEquals(rockCount0+1, rock2.ID);
        Rock rock3 = new Rock(model);
        assertEquals(rockCount0+3, Rock.rockCount);
        assertEquals(rockCount0+2, rock3.ID);
    }
}
