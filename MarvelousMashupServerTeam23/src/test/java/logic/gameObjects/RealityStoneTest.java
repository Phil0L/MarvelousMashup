package logic.gameObjects;

import logic.model.TestUsingModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests for the RealityStone class.
 * @author Luka Stoehr
 */
public class RealityStoneTest extends TestUsingModel {

    /**
     * Tests the infinteraction() method of the RealityStone class.
     * @author Luka Stoehr
     */
    @Test
    public void infinteractionTest(){
        createFreshModel();

        Hero hero = player2Team[5];
        hero.actionPoints = 100;
        hero.place(new Position(7,8));

        RealityStone stone = new RealityStone(1, model);
        hero.inventory.add(stone);
        assertFalse(hero.infinteraction(stone, new Position(9,9)));  // not a neighbouring field
        assertTrue(hero.infinteraction(stone, new Position(8,8)));
        assertTrue(model.field[8][8] instanceof Rock);
        assertFalse(hero.infinteraction(stone, new Position(8,8)));  // Stone still in cooldown
        model.round += 2;
        assertTrue(hero.infinteraction(stone, new Position(8,8)));
        model.round += 2;
        assertNull(model.field[8][8]);

        Hero hero2 = player2Team[3];
        hero2.place(new Position(8,8));
        assertFalse(hero.infinteraction(stone, new Position(8,8))); // Field is occupied by another hero
    }
}
