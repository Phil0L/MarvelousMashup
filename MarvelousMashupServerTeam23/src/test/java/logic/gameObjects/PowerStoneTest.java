package logic.gameObjects;

import logic.model.TestUsingModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the PowerStone class.
 * @author Luka Stoehr
 */
public class PowerStoneTest extends TestUsingModel {

    /**
     * Tests the infinteraction method of the PowerStone class.
     * @author Luka Stoehr
     */
    @Test
    public void infinteraction(){
        createFreshModel();

        Hero hero = player1Team[4];
        hero.actionPoints = 100;
        Hero victim = player2Team[0];

        PowerStone stone = new PowerStone(1, model);
        hero.inventory.add(stone);

        hero.place(new Position(2,3));
        victim.place(new Position(4,5));
        assertFalse(hero.infinteraction(stone , victim.getPosition())); // not  a neighbour
        assertFalse(hero.infinteraction(stone, new Position(3,4)));  // field is empty

        victim.move(new Position(3,4));
        int heroHPBefore = hero.healthPoints;
        int victimHPBefore = victim.healthPoints;
        int heroAPBefore = hero.actionPoints;
        assertTrue(hero.infinteraction(stone, new Position(3,4)));
        assertEquals(heroHPBefore - 0.1*hero.maxHealthPoints, hero.healthPoints);
        assertEquals(heroAPBefore - 1, hero.actionPoints);
        assertEquals(victimHPBefore - 2*hero.nearAttackDamage, victim.healthPoints);

        assertFalse(hero.infinteraction(stone, new Position(3,4)));  //stone in cooldown
        model.round += 2;
        hero.healthPoints = 1;
        assertTrue(hero.infinteraction(stone, new Position(3,4)));
        assertEquals(1, hero.healthPoints); // HP can't fall under 1 because of this infinteraction
    }
}
