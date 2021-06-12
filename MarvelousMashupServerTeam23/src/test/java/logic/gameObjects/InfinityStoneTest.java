package logic.gameObjects;

import logic.model.TestUsingModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class InfinityStone. InfinityStone is an abstract
 * class, but has a non-abstract move() method which is tested here
 * using one of the Stones which extend InfinityStone for this purpose.
 * @author Luka Stoehr
 */
public class InfinityStoneTest extends TestUsingModel {

    /**
     * Tests the non-abstract move() method in the InfinityStone class.
     * @author Luka Stoehr
     */
    @Test
    public void moveTest(){
        createFreshModel();

        SoulStone stone = new SoulStone(10, this.model);
        assertFalse(stone.move(new Position(2,3))); //Stone was not placed yet
        stone.place(new Position(9,5));
        assertEquals(new Position(9,5), stone.getPosition());

        assertTrue(stone.move(new Position(9,4)));
        assertFalse(stone.move(new Position(0,0)));     //Not a neighbour
        assertTrue(stone.move(new Position(8,3)));
        assertEquals(new Position(8,3), stone.getPosition());

        Hero hero = this.player1Team[3];
        hero.place(new Position(7,3));
        assertFalse(stone.move(new Position(7,3)));    // Target field is occupied
        assertEquals(new Position(8,3), stone.getPosition());
    }


}
