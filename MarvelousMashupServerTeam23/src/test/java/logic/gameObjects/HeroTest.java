package logic.gameObjects;

import logic.model.TestUsingModel;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for the Hero class.
 * @author Luka Stoehr
 */
public class HeroTest extends TestUsingModel {
    /**
     * Tests the infinteraction method of the Hero class, using the SpaceStone as an example.
     * The purpose of this method is not to test the if the infinteraction of the SpaceStone works, but
     * whether the infinteraction() method in the Hero class allows infinteractions under the right
     * circumstances.
     * @author Luka Stoehr
     */
    @Test
    public void infinteractionTest(){
        createFreshModel();

        Hero hero = player1Team[1];
        hero.place(new Position(3,3));
        SpaceStone stone = new SpaceStone(2, model);
        hero.inventory.add(stone);

        hero.movementPoints = 0;
        model.round = 3;
        assertTrue(hero.infinteraction(stone, new Position(9,8)));
        model.round += 3;
        Position newPos = hero.getPosition();
        assertEquals(9, newPos.getX());
        assertEquals(8, newPos.getY());

        hero.actionPoints = 0;
        assertFalse(hero.infinteraction(stone, new Position(1,2)));  // no APs
        hero.actionPoints = 2;
        assertTrue(hero.infinteraction(stone, new Position(1,2)));
        model.round += 3;
        hero.healthPoints = 0;
        assertFalse(hero.infinteraction(stone, new Position(5,5))); // no HPs
        hero.healthPoints = 1;
        assertTrue(hero.infinteraction(stone, new Position(5,5)));
        model.round += 3;
        newPos = hero.getPosition();
        assertEquals(5, newPos.getX());
        assertEquals(5, newPos.getY());

        hero.inventory.remove(stone);
        assertFalse(hero.infinteraction(stone, new Position(1,2))); // no InfinityStone
    }

    /**
     * Tests the giveInfinityStone() method in the Hero class.
     * @author Luka Stoehr
     */
    @Test
    public void giveInfinityStoneTest(){
        createFreshModel();

        SpaceStone stone = new SpaceStone(4, model);
        Hero h1 = player1Team[0];
        Hero h2 = player1Team[5];

        h1.place(new Position(5,6));
        h2.place(new Position(6,6));
        h2.inventory.add(stone);

        assertTrue(h2.giveInfinityStone(h1, stone.ID));
        assertTrue(h1.inventory.contains(stone));
        assertFalse(h2.inventory.contains(stone));

        assertFalse(h2.giveInfinityStone(h2, stone.ID));    //h2 doesn't have the stone anymore
        assertTrue(h1.move(new Position(4, 6)));
        assertFalse(h1.giveInfinityStone(h2, stone.ID)); //h1 and h2 are not neighbours anymore
    }

    /**
     * This method tests the nearAttack() method
     * @author Luka Stoehr
     */
    @Test
    public void nearAttackTest(){
        createFreshModel();

        Hero attacker = model.playerOne.playerTeam[3];
        Hero target = model.playerTwo.playerTeam[5];

        attacker.actionPoints = 1;
        attacker.nearAttackDamage = 5;
        target.healthPoints = 10;

        attacker.place(new Position(7,8));
        target.place(new Position(5,6));
        assertFalse(attacker.nearAttack(new Position(5,6))); //Not a neighbour
        assertFalse(attacker.nearAttack(new Position(6,7))); //empty field
        assertTrue(target.move(new Position(6,7)));
        assertTrue(attacker.nearAttack(new Position(6,7)));

        assertEquals(5, target.healthPoints);
        assertEquals(5, model.causedDamagePlayerOne);
        assertEquals(0, attacker.actionPoints);

        assertFalse(attacker.nearAttack(new Position(6,7))); //No action points left
    }

    /**
     * This method tests the farAttack() method of the hero class.
     * @author Luka Stoehr
     */
    @Test
    public void farAttackTest(){
        createFreshModel();

        Hero attacker = model.playerTwo.playerTeam[4];
        Hero target = model.playerOne.playerTeam[3];

        attacker.place(new Position(9,9));
        target.place(new Position(8,9));
        attacker.actionPoints = 1;
        attacker.farAttackDamage = 5;
        attacker.farAttackDistance = 3;
        target.healthPoints = 5;

        assertFalse(attacker.farAttack(new Position(8,9)));  // Target is neighbour
        assertFalse(attacker.farAttack(new Position(9,9))); // Target is attacker itself

        assertTrue(target.move(new Position(7,9)));
        assertTrue(target.move(new Position(6,9)));

        assertTrue(attacker.farAttack(new Position(6,9)));
        assertEquals(0, target.healthPoints);
        assertEquals(5, model.causedDamagePlayerTwo);
        assertEquals(1, model.knockedOpHeroPlayerTwo);
        assertEquals(0, attacker.actionPoints);

        attacker.actionPoints = 1;

        assertFalse(target.move(new Position(5,9))); //No HP left
        target.healthPoints = 20;
        assertTrue(target.move(new Position(5,9)));
        assertFalse(attacker.farAttack(new Position(5,9)));  // Target out of range
        assertTrue(target.move(new Position(6,9)));
        assertTrue(attacker.farAttack(new Position(6,9)));
    }

    /**
     * Tests the hasCode() method of a hero.
     * @author Luka Stoehr
     */
    @Test
    public void hashCodeTest(){
        Hero hero = new Hero(1,4,3,3 ,3 , "Georg", 3, 3, 3, model);
        assertEquals(Objects.hash(1, 4, "Georg"), hero.hashCode());
    }

    /**
     * Test for the attacked() method without InfinityStones in the inventory of a
     * character that is knocked out.
     * @author Luka Stoehr
     */
    @Test
    public void attackedTest(){
        createFreshModel();
        Hero hero = new Hero(1,2,3,3 ,3 , "Georg", 3, 3, 3, model);

        assertEquals(2,hero.attacked(2));
        assertEquals(1, hero.healthPoints);
        assertEquals(1, hero.attacked(10));
        assertEquals(0, hero.healthPoints);
        assertEquals(0, hero.attacked(42));
        assertEquals(0, hero.healthPoints);
    }

    /**
     * Checks if the InfinityStones of a character are distributed on the field if
     * the character is knocked out. In this test, the neighbouring fields of the Hero
     * are all free.
     * @author Luka Stoehr
     */
    @Test
    public void attackedTest2_KnockOutCharWithStones(){
        createFreshModel();
        SoulStone ss = new SoulStone(3, this.model);
        MindStone ms = new MindStone(4, model.controller.configuration.matchConfig.mindStoneDMG, this.model);
        PowerStone ps = new PowerStone(1, this.model);

        Hero hero = this.player1Team[2];
        hero.place(new Position(5,5));
        hero.inventory.add(ss);
        hero.inventory.add(ms);
        hero.inventory.add(ps);

        assertNull(ss.getPosition());
        assertNull(ms.getPosition());
        assertNull(ps.getPosition());

        assertEquals(hero.healthPoints, hero.attacked(hero.healthPoints));

        assertNotNull(ss.getPosition());
        assertNotNull(ms.getPosition());
        assertNotNull(ps.getPosition());
        assertTrue(ss.isNeighbour(hero.getPosition()));
        assertTrue(ms.isNeighbour(hero.getPosition()));
        assertTrue(ps.isNeighbour(hero.getPosition()));
    }

    /**
     * Checks if the InfinityStones of a character are distributed on the field if
     * the character is knocked out. In this test all the neighbouring fields of the
     * Hero are occupied by Rocks
     * @author Luka Stoehr
     */
    @Test
    public void attackedTest3_KnockOutCharWithStones(){
        createFreshModel();
        SoulStone ss = new SoulStone(3, this.model);
        MindStone ms = new MindStone(4, model.controller.configuration.matchConfig.mindStoneDMG, this.model);
        PowerStone ps = new PowerStone(1, this.model);

        Hero hero = this.player1Team[2];
        hero.place(new Position(5,5));
        hero.inventory.add(ss);
        hero.inventory.add(ms);
        hero.inventory.add(ps);

        assertTrue((new Rock(this.model)).place(new Position(5,6)));
        assertTrue((new Rock(this.model)).place(new Position(4,6)));
        assertTrue((new Rock(this.model)).place(new Position(4,5)));
        assertTrue((new Rock(this.model)).place(new Position(4,4)));
        assertTrue((new Rock(this.model)).place(new Position(5,4)));
        assertTrue((new Rock(this.model)).place(new Position(6,4)));
        assertTrue((new Rock(this.model)).place(new Position(6,5)));
        assertTrue((new Rock(this.model)).place(new Position(6,6)));


        assertNull(ss.getPosition());
        assertNull(ms.getPosition());
        assertNull(ps.getPosition());

        assertEquals(hero.healthPoints, hero.attacked(hero.healthPoints));

        assertNotNull(ss.getPosition());
        assertNotNull(ms.getPosition());
        assertNotNull(ps.getPosition());
        assertFalse(ss.isNeighbour(hero.getPosition()));
        assertFalse(ms.isNeighbour(hero.getPosition()));
        assertFalse(ps.isNeighbour(hero.getPosition()));
    }

    /**
     * Tests the move() method of the hero class. I like to move it move it!
     * @author Luka Stoehr
     */
    @Test
    public void moveTest(){
        createFreshModel();
        Hero moveMe = player1Team[5];
        moveMe.place(new Position(5,5));
        assertEquals(new Position(5,5), moveMe.getPosition());
        moveMe.movementPoints = 0;
        assertFalse(moveMe.move(new Position(4,5))); // No MPs
        assertEquals(new Position(5,5), moveMe.getPosition());
        moveMe.movementPoints = 100;
        assertFalse(moveMe.move(new Position(3,5))); // Can only do one step
        assertTrue(moveMe.move(new Position(4,5)));
        assertEquals(new Position(4,5), moveMe.getPosition());

        Rock rock = new Rock(model);
        rock.place(new Position(3,5));
        assertFalse(moveMe.move(new Position(3,5)));     // Can't walk into rock

        SpaceStone stone = new SpaceStone(4, model);
        stone.place(new Position(4,6));
        assertTrue(moveMe.move(new Position(4,6)));
        assertEquals(new Position(4,6), moveMe.getPosition());

        assertNull(stone.getPosition());       // Stone is not on the field anymore
        assertTrue(moveMe.inventory.contains(stone));

        Hero obstacleHero = player1Team[2];
        assertTrue(obstacleHero.place(new Position(4,7)));
        assertTrue(moveMe.move(new Position(4,7)));
        assertEquals(new Position(4,7), moveMe.getPosition());
        assertEquals(new Position(4,6), obstacleHero.getPosition());

        Thanos thanos = new Thanos(model);
        assertTrue(thanos.place(new Position(5,7)));
        assertFalse(moveMe.move(new Position(5,7)));     // Can't walk into Thanos
        assertEquals(new Position(4,7), moveMe.getPosition());
    }
}
