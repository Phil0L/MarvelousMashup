package logic.gameObjects;

import logic.model.TestUsingModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Space stone.
 * @author Luka Stoehr
 */
public class SpaceStoneTest extends TestUsingModel {

    /**
     * Tests the infinteraction() method of the SpaceStone class.
     * @author Luka Stoehr
     */
    @Test
    public void infinteractionTest(){
        createFreshModel();

        Hero hero = player1Team[2];
        hero.place(new Position(2,2));
        SpaceStone stone = new SpaceStone(1, model);
        hero.inventory.add(stone);
        hero.actionPoints = 100;

        assertTrue(hero.infinteraction(stone, new Position(7,8)));
        assertFalse(hero.infinteraction(stone, new Position(5,6))); // Stone in cooldown
        model.round += 2;
        assertTrue(hero.infinteraction(stone, new Position(5, 6)));
        model.round += 2;

        Rock rock = new Rock(model);
        rock.place(new Position(0,0));
        assertFalse(hero.infinteraction(stone, new Position(0,0)));    // field not free
        assertTrue(hero.infinteraction(stone, new Position(1,0)));
    }
}
