package logic.gameObjects;

import logic.TestUsingController;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the StanLee class
 * @author Luka Stoehr
 */
public class StanLeeTest extends TestUsingController {

    /**
     * Tests the stanLeeTurn() mehtod by placing a knocked out character on the field and
     * checking if its HPs are refilled.
     * @author Luka Stoehr
     */
    @Test
    public void stanLeeTurnTest(){
        createController();
        controller.startGame();

        //Skip the rounds before StanLee shows up
        for(int i = 0; i < 5; i++){
            controller.nextRound();
        }

        //Clear field
        LinkedList<Position> freePosList = new LinkedList<>();
        for(int x = 0; x < model.field.length; x++){
            for(int y = 0; y < model.field[0].length; y++){
                model.field[x][y] = null;
            }
        }

        //Cut off a part of the field with rocks, if there is a hero, place them somewhere else
        model.field[0][2] = new Rock(model);
        model.field[1][2] = new Rock(model);
        model.field[2][2] = new Rock(model);
        model.field[3][2] = new Rock(model);
        model.field[3][1] = new Rock(model);
        model.field[3][0] = new Rock(model);

        //Place hero teams again
        for(int i = 0; i < 6; i++){
            Hero hero = model.playerOne.playerTeam[i];
            hero.place(new Position(i, 4));
            Hero hero2 = model.playerTwo.playerTeam[i];
            hero2.place(new Position(i,5));
        }

        //Place a knocked out hero in there
        Hero hero = model.playerOne.playerTeam[1];
        hero.healthPoints = 0;
        Position oldPos = hero.getPosition();
        model.field[oldPos.getX()][oldPos.getY()] = null;
        model.field[1][0] = hero;

        //The other heroes are out of sight, so HP of hero2 and hero3 should not change
        Hero hero2 = model.playerOne.playerTeam[5];
        hero2.healthPoints = 2;
        assertFalse(model.checkLineOfSight(hero2.getPosition(), hero.getPosition()));
        Hero hero3 = model.playerTwo.playerTeam[4];
        assertFalse(model.checkLineOfSight(hero3.getPosition(), hero.getPosition()));
        hero3.healthPoints = 3;

        assertEquals(0, hero.getHealthPoints());
        assertEquals(2, hero2.getHealthPoints());
        assertEquals(3, hero3.getHealthPoints());

        //StanLee should show up and set hero.healthPoints to max value
        controller.nextRound();

        assertEquals(hero.maxHealthPoints, hero.getHealthPoints());

        assertEquals(2, hero2.getHealthPoints());
        assertEquals(3, hero3.getHealthPoints());
    }

    /**
     * Tests the stanLeeTurn() method by placing a knocked out character on the field and
     * checking if its HPs are refilled. There is also another hero placed within sight of
     * the knocked out one, so its HPs should also be refilled.
     * @author Luka Stoehr
     */
    @Test
    public void stanLeeTurnTest2(){
        createController();
        controller.startGame();

        //Skip the rounds before StanLee shows up
        for(int i = 0; i < 5; i++){
            controller.nextRound();
        }

        //Clear field
        LinkedList<Position> freePosList = new LinkedList<>();
        for(int x = 0; x < model.field.length; x++){
            for(int y = 0; y < model.field[0].length; y++){
                model.field[x][y] = null;
            }
        }

        //Cut off a part of the field with rocks, if there is a hero, place them somewhere else
        model.field[0][2] = new Rock(model);
        model.field[1][2] = new Rock(model);
        model.field[2][2] = new Rock(model);
        model.field[3][2] = new Rock(model);
        model.field[3][1] = new Rock(model);
        model.field[3][0] = new Rock(model);

        //Place hero teams again
        for(int i = 0; i < 6; i++){
            Hero hero = model.playerOne.playerTeam[i];
            hero.place(new Position(i, 4));
            Hero hero2 = model.playerTwo.playerTeam[i];
            hero2.place(new Position(i,5));
        }

        //Place a knocked out hero in there
        Hero hero = model.playerOne.playerTeam[1];
        hero.healthPoints = 0;
        Position oldPos = hero.getPosition();
        model.field[oldPos.getX()][oldPos.getY()] = null;
        model.field[0][0] = hero;

        Hero hero2 = model.playerOne.playerTeam[5];
        hero2.healthPoints = 2;
        Position oldPos2 = hero2.getPosition();
        model.field[oldPos2.getX()][oldPos2.getY()] = null;
        model.field[2][1] = hero2;
        assertTrue(model.checkLineOfSight(hero2.getPosition(), hero.getPosition()));

        //The other heroes are out of sight, so HP of hero3 should not change
        Hero hero3 = model.playerTwo.playerTeam[4];
        assertFalse(model.checkLineOfSight(hero3.getPosition(), hero.getPosition()));
        hero3.healthPoints = 3;

        assertEquals(0, hero.getHealthPoints());
        assertEquals(2, hero2.getHealthPoints());
        assertEquals(3, hero3.getHealthPoints());

        //StanLee should show up and set hero.healthPoints to max value
        controller.nextRound();

        assertEquals(hero.maxHealthPoints, hero.getHealthPoints());
        assertEquals(hero2.maxHealthPoints, hero2.getHealthPoints());
        assertEquals(3, hero3.getHealthPoints());
    }

    /**
     * In this test the knocked out character is trapped by rocks, so StanLee should not
     * be able to revive him.
     * @author Luka Stoehr
     */
    @Test
    public void stanLeeTurnTest3(){
        createController();
        controller.startGame();

        //Skip the rounds before StanLee shows up
        for(int i = 0; i < 5; i++){
            controller.nextRound();
        }

        //Clear field
        LinkedList<Position> freePosList = new LinkedList<>();
        for(int x = 0; x < model.field.length; x++){
            for(int y = 0; y < model.field[0].length; y++){
                model.field[x][y] = null;
            }
        }

        //Cut off a part of the field with rocks, if there is a hero, place them somewhere else
        model.field[0][1] = new Rock(model);
        model.field[1][1] = new Rock(model);
        model.field[1][0] = new Rock(model);

        //Place hero teams again
        for(int i = 0; i < 6; i++){
            Hero hero = model.playerOne.playerTeam[i];
            hero.place(new Position(i, 4));
            Hero hero2 = model.playerTwo.playerTeam[i];
            hero2.place(new Position(i,5));
        }

        //Place a knocked out hero in there
        Hero hero = model.playerOne.playerTeam[1];
        hero.healthPoints = 0;
        Position oldPos = hero.getPosition();
        model.field[oldPos.getX()][oldPos.getY()] = null;
        model.field[0][0] = hero;

        assertEquals(0, hero.getHealthPoints());

        //StanLee should show up
        controller.nextRound();

        assertEquals(0, hero.getHealthPoints());
        assertTrue(model.round >= 6);

    }
}
