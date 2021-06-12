package logic.gameObjects;

import logic.TestUsingController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Thanos class. This class extends TestUsingController which
 * is different in the class ThanosTest (it extends TestUsingModel). Therefore
 * this class tests the thanosTurn() method which needs a valid controller object
 * to work.
 * @author Luka Stoehr
 */
public class ThanosTest2 extends TestUsingController {

    /**
     * Test the thanosTurn() method by placing a Hero as bait with an InfinityStone
     * next to Thanos and making sure Thanos collects the stone.
     * @author Luka Stoehr
     */
    @Test
    public void testThanosTurn1(){
        createController();
        controller.startGame();

        assertNull(model.thanos);   //Thanos has not been created yet
        for(int i = 0; i < 29; i++){
            controller.nextRound();
        }
        assertNull(model.thanos);   //Thanos has not been created yet
        controller.nextRound();
        assertNotNull(model.thanos);    //Thanos shows up in round 30 in the matchconfig used for this test.
        assertNotNull(model.thanos.getPosition());

        Position thanosPos = model.thanos.getPosition();
        int thanosX = thanosPos.getX();
        int thanosY = thanosPos.getY();

        //Place Thanos in the middle of the field
        model.field[thanosX][thanosY] = null;
        model.field[3][3] = model.thanos;

        thanosPos = model.thanos.getPosition();
        thanosX = thanosPos.getX();
        thanosY = thanosPos.getY();

        //Clear all fields around Thanos
        model.field[thanosX+1][thanosY+1] = null;
        model.field[thanosX][thanosY+1] = null;
        model.field[thanosX-1][thanosY+1] = null;
        model.field[thanosX-1][thanosY] = null;
        model.field[thanosX-1][thanosY-1] = null;
        model.field[thanosX][thanosY-1] = null;
        model.field[thanosX+1][thanosY-1] = null;
        model.field[thanosX+1][thanosY] = null;

        //Place a hero with InfinityStone as bait next to Thanos
        Hero bait = new Hero(1, 7, 2, 2, 2, "Olaf", 2, 2, 2, model);
        assertTrue(bait.place(new Position(thanosX+1, thanosY)));
        bait.inventory.add(model.spaceStone);

        controller.nextRound();     //Thanos should take stone from Hero bait now, in this round
        for(int i = 0; i < 14; i++) controller.nextTurn();  //Make sure Thanos can play his turn
        assertEquals(new Position(thanosX, thanosY), bait.getPosition());   //Hero switches places with Thanos
        assertEquals(0, bait.inventory.size());
        assertTrue(model.thanos.inventory.contains(model.spaceStone));
        assertTrue(bait.getHealthPoints() == 0);
        assertEquals(12, countNonAtomizedHeroes()); //No one should be atomized at this point
    }

    /**
     * Test the thanosTurn() method by placing an InfinityStone as bait
     * next to Thanos and making sure Thanos collects the stone.
     * @author Luka Stoehr
     */
    @Test
    public void testThanosTurn2(){
        createController();
        controller.startGame();

        assertNull(model.thanos);   //Thanos has not been created yet
        for(int i = 0; i < 29; i++){
            controller.nextRound();
        }
        assertNull(model.thanos);   //Thanos has not been created yet
        controller.nextRound();
        assertNotNull(model.thanos);    //Thanos shows up in round 30 in the matchconfig used for this test.
        assertNotNull(model.thanos.getPosition());

        Position thanosPos = model.thanos.getPosition();
        int thanosX = thanosPos.getX();
        int thanosY = thanosPos.getY();

        //Place Thanos in the middle of the field
        model.field[thanosX][thanosY] = null;
        model.field[3][3] = model.thanos;

        thanosPos = model.thanos.getPosition();
        thanosX = thanosPos.getX();
        thanosY = thanosPos.getY();

        //Clear all fields around Thanos
        model.field[thanosX+1][thanosY+1] = null;
        model.field[thanosX][thanosY+1] = null;
        model.field[thanosX-1][thanosY+1] = null;
        model.field[thanosX-1][thanosY] = null;
        model.field[thanosX-1][thanosY-1] = null;
        model.field[thanosX][thanosY-1] = null;
        model.field[thanosX+1][thanosY-1] = null;
        model.field[thanosX+1][thanosY] = null;

        //Place an InfinityStone as bait next to Thanos
        InfinityStone bait = model.mindStone;
        assertTrue(bait.place(new Position(thanosX+1, thanosY)));

        controller.nextRound();     //Thanos should take stone from Hero bait now, in this round
        for(int i = 0; i < 14; i++) controller.nextTurn();  //Make sure Thanos can play his turn

        assertTrue(model.thanos.inventory.contains(model.mindStone));
        assertEquals(12, countNonAtomizedHeroes()); //No one should be atomized at this point
    }

    /**
     * Tests if Thanos atomizes the right amount of heroes, if he has all 6 Stones.
     * @author Luka Stoehr
     */
    @Test
    public void testThanosAtomize(){
        //TODO This test is not always successful
        createController();
        controller.startGame();

        assertNull(model.thanos);   //Thanos has not been created yet
        for(int i = 0; i < 29; i++){
            controller.nextRound();
        }
        assertNull(model.thanos);   //Thanos has not been created yet
        controller.nextRound();
        assertNotNull(model.thanos);    //Thanos shows up in round 30 in the matchconfig used for this test.
        assertNotNull(model.thanos.getPosition());

        //Give Thanos all InfinityStones and check if he atomizes heroes
        model.thanos.inventory.add(model.timeStone);
        model.thanos.inventory.add(model.spaceStone);
        model.thanos.inventory.add(model.mindStone);
        model.thanos.inventory.add(model.soulStone);
        model.thanos.inventory.add(model.powerStone);
        model.thanos.inventory.add(model.realityStone);
        assertEquals(6, model.thanos.inventory.size());
        assertEquals(6, model.thanos.inventoryToArray().length);

        int nonAtomizedBefore = countNonAtomizedHeroes();
        //Make sure Thanos can play his turn
        controller.nextRound();
        if (countNonAtomizedHeroes() == nonAtomizedBefore){
            //Thanos has not had his turn in this round yet, therefore place him as next in turnOrder
            controller.turnOrder.remove(model.thanos);
            controller.turnOrder.add(controller.turnCount+1, model.thanos);
            controller.nextTurn();
        }
        int nonAtomizedAfter = countNonAtomizedHeroes();
        assertEquals(nonAtomizedBefore/2, nonAtomizedAfter);
    }


    /**
     * Helper method that counts how many heroes are not atomized at the moment.
     * @author Luka Stoehr
     * @return Number of non atomized heroes
     */
    private int countNonAtomizedHeroes(){
        int counter = 0;
        for(Hero hero: model.playerOne.playerTeam){
            if(!hero.atomized) counter++;
        }
        for(Hero hero: model.playerTwo.playerTeam){
            if(!hero.atomized) counter++;
        }
        return counter;
    }
}
