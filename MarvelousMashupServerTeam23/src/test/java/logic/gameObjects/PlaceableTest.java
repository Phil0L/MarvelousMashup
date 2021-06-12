package logic.gameObjects;

import logic.model.TestUsingModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class with all the tests for the Placeable class.
 * @author Luka Stoehr
 */
public class PlaceableTest extends TestUsingModel {
    /**
     * Test the place() method by placing Placeables on free and occupied positions.
     * @author Luka Stoehr
     */
    @Test
    public void placeTest(){
        createFreshModel();

        assertNull(model.field[1][1]);
        assertTrue(player1Team[0].place(new Position(1, 1)));
        assertEquals(model.field[1][1], player1Team[0]);
        assertFalse(player2Team[2].place(new Position(1, 1)));
        assertTrue(player2Team[2].place(new Position(2, 1)));
    }

    /**
     * This method tests the getPosition() method, by first getting the position
     * of a character that was not placed yet and then checking again after placing it.
     * @author Luka Stoehr
     */
    @Test
    public void getPositionTest(){
        createFreshModel();

        assertNull(player1Team[0].getPosition());

        player1Team[0].place(new Position(5,4));
        Position pos = player1Team[0].getPosition();
        assertEquals(5,pos.getX());
        assertEquals(4, pos.getY());
    }

    /**
     * This method tests the isNeighbour() method.
     * @author Luka Stoehr
     */
    @Test
    public void isNeighbourTest(){
        createFreshModel();

        assertFalse(player1Team[0].isNeighbour(new Position(2, 2)));
        player1Team[0].place(new Position(3,3));

        assertTrue(player1Team[0].isNeighbour(new Position(3, 4)));
        assertTrue(player1Team[0].isNeighbour(new Position(4, 4)));
        assertTrue(player1Team[0].isNeighbour(new Position(4, 3)));
        assertTrue(player1Team[0].isNeighbour(new Position(4, 2)));
        assertTrue(player1Team[0].isNeighbour(new Position(3, 2)));
        assertTrue(player1Team[0].isNeighbour(new Position(2, 2)));
        assertTrue(player1Team[0].isNeighbour(new Position(2, 3)));
        assertTrue(player1Team[0].isNeighbour(new Position(2, 4)));

        player2Team[0].place(new Position(2, 4));
        assertTrue(player1Team[0].isNeighbour(new Position(2, 4)));

        assertFalse(player1Team[0].isNeighbour(new Position(6, 4)));
        assertFalse(player1Team[0].isNeighbour(new Position(0, 0)));
        assertFalse(player1Team[0].isNeighbour(new Position(2, 1)));

    }
}
