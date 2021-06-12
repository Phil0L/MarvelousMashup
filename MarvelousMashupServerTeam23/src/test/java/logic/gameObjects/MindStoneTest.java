package logic.gameObjects;

import logic.model.TestUsingModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MindStone.
 * @author Luka Stoehr
 */
public class MindStoneTest extends TestUsingModel {

    /**
     * Tests the infinteraction() method of the MindStone class.
     * @author Luka Stoehr
     */
    @Test
    public void infinteractionTest(){
        createFreshModel();
        Hero hero1 = player1Team[5];
        Hero hero2 = player2Team[1];
        MindStone stone = new MindStone(1, model.controller.configuration.matchConfig.mindStoneDMG, model);
        hero1.inventory.add(stone);
        hero1.actionPoints = 100;
        hero2.healthPoints = 100;
        hero1.place(new Position(3,4));
        hero2.place(new Position(3,5));
        assertFalse(hero1.infinteraction(stone, hero2.getPosition())); // target is neighbour
        assertTrue(hero2.move(new Position(3,6)));
        assertTrue(hero2.move(new Position(3,7)));
        assertTrue(hero2.move(new Position(3,8)));

        assertTrue(hero1.infinteraction(stone, hero2.getPosition()));
        assertFalse(hero1.infinteraction(stone, hero2.getPosition()));  // Cooldown
        model.round += 2;
        hero1.farAttackDistance = 2;
        assertFalse(hero1.farAttack(hero2.getPosition()));  // Should be out of range
        assertTrue(hero1.infinteraction(stone, hero2.getPosition()));
        model.round += 2;

        Rock rock = new Rock(model);
        rock.place(new Position(3,6));
        assertFalse(hero1.infinteraction(stone, hero2.getPosition()));  // Line of sight is blocked
        rock.attacked(100);
        assertTrue(hero1.infinteraction(stone, hero2.getPosition()));
        model.round += 2;

        rock.healthPoints = 100;
        rock.place(new Position(3,6));
        assertTrue(hero1.infinteraction(stone, rock.getPosition()));
        model.round += 2;
        SoulStone soul = new SoulStone(3, model);
        soul.place(new Position(8,4));
        assertFalse(hero1.infinteraction(stone, soul.getPosition())); // oulStone cannot be attacked
    }
}
