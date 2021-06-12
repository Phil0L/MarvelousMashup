package logic.gameObjects;

import logic.model.TestUsingModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for SoulStone.
 * @author Luka Stoehr
 */
public class SoulStoneTest extends TestUsingModel {

    @Test
    public void infinteractionTest(){
        createFreshModel();

        Hero hero1 = player1Team[4];
        Hero hero2 = player1Team[5];

        hero1.place(new Position(4,7));
        hero2.place(new Position(5,7));
        SoulStone stone = new SoulStone(3, model);
        hero1.inventory.add(stone);
        hero1.healthPoints = 10;
        hero1.actionPoints = 1;
        model.round = 1;

        hero2.healthPoints = 10;
        assertFalse(hero1.infinteraction(stone, new Position(3,7))); // Position is empty
        assertFalse(hero1.infinteraction(stone, hero2.getPosition())); // Target not knocked out
        hero2.attacked(10);
        assertTrue(hero1.infinteraction(stone, hero2.getPosition()));
        assertEquals(hero2.maxHealthPoints, hero2.healthPoints);
        assertEquals(0, hero1.actionPoints);

        model.round = 5;
        hero2.healthPoints = 0;
        assertFalse(hero1.infinteraction(stone, hero2.getPosition()));  // Hero1 does not have AP left

        hero1.actionPoints = 10;
        assertTrue(hero1.infinteraction(stone, hero2.getPosition()));
        hero2.healthPoints = 0;
        assertFalse(hero1.infinteraction(stone, hero2.getPosition()));  // Stone is still in cooldown

        model.round = 10;
        hero2.healthPoints = 10;
        hero2.movementPoints = 1;
        hero2.move(new Position(6,7));
        hero2.healthPoints = 0;
        assertFalse(hero1.infinteraction(stone, hero2.getPosition()));  // Target is not a neighbour
    }
}
