package logic.gameObjects;

import logic.Controller.HandleReturn;
import logic.TestUsingController;
import logic.model.TestUsingModel;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the class Goose.
 * @author Luka Stoehr
 */
public class GooseTest extends TestUsingController {

    /**
     * Tests the move method of Goose, which should always return false.
     * @author Luka Stoehr
     */
    @Test
    public void moveTest(){
        createController();

        model.goose = new Goose(model);
        assertTrue(model.goose.place(new Position(3,4)));
        //Goose cannot move
        assertFalse(model.goose.move(new Position(3,5)));
        assertFalse(model.goose.move(new Position(2,5)));
        assertFalse(model.goose.move(new Position(2,4)));
        assertFalse(model.goose.move(new Position(7,8)));
    }

    /**
     * Tests the gooseTurn() method by starting new rounds and checking if
     * goose has left a new InfinityStone on the field.
     * @auhtor Luka Stoehr
     */
    @Test
    public void gooseTurnTest(){
        createController();

        assertEquals(0, getPosWithInfinityStones().size());

        HandleReturn startReturn = controller.startGame();

        assertEquals(1, getPosWithInfinityStones().size());
        model.goose.gooseTurn();      // This should not work, because it is not Gooses turn
        assertEquals(1, getPosWithInfinityStones().size());
        controller.nextRound();
        assertEquals(2, getPosWithInfinityStones().size());
        controller.nextRound();
        assertEquals(3, getPosWithInfinityStones().size());
        controller.nextRound();
        assertEquals(4, getPosWithInfinityStones().size());
        controller.nextRound();
        assertEquals(5, getPosWithInfinityStones().size());
        controller.nextRound();
        assertEquals(6, getPosWithInfinityStones().size());
        controller.nextRound(); //Goose will not work again, because all Stones are on field now
        assertEquals(6, getPosWithInfinityStones().size());
    }

    /**
     * This is a helper method for the gooseTurnTest(). It finds all gras
     * fields with InfinityStones on the field and returns a LinkedList
     * containing all these positions
     * @author Luka Stoehr
     * @return LinkedList containing all Positions of gras fields with InfinityStones on the field
     */
    private LinkedList<Position> getPosWithInfinityStones(){
        LinkedList<Position> posWithInfinityStones = new LinkedList<>();
        for(int x = 0; x < model.field.length; x++){
            for(int y = 0; y < model.field[0].length; y++){
                if(model.field[x][y] instanceof InfinityStone){
                    posWithInfinityStones.add(new Position(x,y));
                }
            }
        }
        return posWithInfinityStones;
    }
}
