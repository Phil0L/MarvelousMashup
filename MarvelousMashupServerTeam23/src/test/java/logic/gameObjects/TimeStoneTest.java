package logic.gameObjects;

import logic.model.TestUsingModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TimeStone.
 * @author Luka Stoehr
 */
public class TimeStoneTest extends TestUsingModel {

    /**
     * Test method for the infinteraction() method of the
     * TimeStone class.
     * @author Luka Stoehr
     */
    @Test
    public void infinteractionTest(){
        createFreshModel();

        Hero hero = player1Team[1];
        TimeStone stone = new TimeStone(1, model);
        hero.inventory.add(stone);

        hero.movementPoints--;
        hero.actionPoints--;
        assertTrue(hero.infinteraction(stone, null));
        assertEquals(hero.maxMovementPoints, hero.movementPoints);
        assertEquals(hero.maxActionPoints, hero.actionPoints);

        hero.movementPoints--;
        hero.actionPoints--;
        assertFalse(hero.infinteraction(stone, null));      // Stone in cooldown

        hero.movementPoints = 0;
        hero.actionPoints = 1;
        model.round += 2;
        assertTrue(hero.infinteraction(stone, null));

        model.round += 2;
        hero.actionPoints = 0;
        assertFalse(hero.infinteraction(stone, null));  // No APs left
    }
}
