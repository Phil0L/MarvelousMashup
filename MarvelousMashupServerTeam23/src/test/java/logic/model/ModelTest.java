package logic.model;

import communication.Profile;
import communication.messages.enums.Role;
import logic.Controller.Controller;
import logic.gameObjects.*;
import org.junit.jupiter.api.Test;
import parameter.ConfigHero;
import parameter.Configuration;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Model class.
 * @author Luka Stoehr
 */
public class ModelTest extends TestUsingModel {

    /**
     * Tests the setPlayerTeam() method, by adding arrays as teams with the same character several times.
     * This method assumes, that the files "asgard.json", "marvelhero.json" and "matchconfig_1.json"
     * are located in the root directory of this project. These are example config files.
     * @author Luka Stoehr
     */
    @Test
    public void setPlayerTeamTest(){
        String p1ID = "id/1";
        String p2ID = "id/2";
        Player p1 = new Player(new Profile(null, p1ID, "1"), Role.PLAYER);
        Player p2 = new Player(new Profile(null, p2ID, "2"), Role.PLAYER);
        Controller controller;
        Model localModel;
        try {
            controller = new Controller(new Configuration("asgard.json", "marvelheros.json", "matchconfig_1.json"));
            //Use localModel for this test instead of the class variable model
            localModel = new Model(5,5,5, p1, p2, controller);

            ConfigHero configHeroA = new ConfigHero(1,"a", 7,8,9,10,11,12);

            ConfigHero[] team1 = new ConfigHero[5];
            ConfigHero[] team2 = new ConfigHero[6];
            for(int i = 0; i<6; i++){
                team2[i] = configHeroA;
            }

            assertFalse(localModel.setPlayerTeam(new Profile(null, p1ID, "1"), team1));    // Length of team1 is not 6
            assertTrue(localModel.setPlayerTeam(new Profile(null, p2ID, "2"), team2));

            team1 = new ConfigHero[6];

            ConfigHero configHeroB = new ConfigHero(2,"b", 1,2,3,4,5,6);

            for(int i = 0; i<6; i++){
                team1[i] = configHeroB;
            }

            assertTrue(localModel.setPlayerTeam(new Profile(null, p1ID, "1"), team1));
            assertTrue(localModel.setPlayerTeam(new Profile(null, p2ID, "2"), team2));

            assertEquals(1 , localModel.playerOne.playerTeam[1].ID);
            assertEquals( 1, localModel.playerOne.playerTeam[1].PID);
            assertEquals("b", localModel.playerOne.playerTeam[1].name);
            assertEquals(1, localModel.playerOne.playerTeam[1].getHealthPoints());
            assertEquals(2, localModel.playerOne.playerTeam[1].getMovementPoints());
            assertEquals(3, localModel.playerOne.playerTeam[1].getActionPoints());
            assertEquals(1, localModel.playerOne.playerTeam[1].maxHealthPoints);
            assertEquals(2, localModel.playerOne.playerTeam[1].maxMovementPoints);
            assertEquals(3, localModel.playerOne.playerTeam[1].maxActionPoints);
            assertEquals(4, localModel.playerOne.playerTeam[1].nearAttackDamage);
            assertEquals(5, localModel.playerOne.playerTeam[1].farAttackDamage);
            assertEquals(6, localModel.playerOne.playerTeam[1].farAttackDistance);

            assertEquals(3 , localModel.playerTwo.playerTeam[3].ID);
            assertEquals( 2, localModel.playerTwo.playerTeam[3].PID);
            assertEquals("a", localModel.playerTwo.playerTeam[3].name);
            assertEquals(7, localModel.playerTwo.playerTeam[3].getHealthPoints());
            assertEquals(8, localModel.playerTwo.playerTeam[3].getMovementPoints());
            assertEquals(9, localModel.playerTwo.playerTeam[3].getActionPoints());
            assertEquals(7, localModel.playerTwo.playerTeam[3].maxHealthPoints);
            assertEquals(8, localModel.playerTwo.playerTeam[3].maxMovementPoints);
            assertEquals(9, localModel.playerTwo.playerTeam[3].maxActionPoints);
            assertEquals(10, localModel.playerTwo.playerTeam[3].nearAttackDamage);
            assertEquals(11, localModel.playerTwo.playerTeam[3].farAttackDamage);
            assertEquals(12, localModel.playerTwo.playerTeam[3].farAttackDistance);
        } catch (IOException e) {
            e.printStackTrace();
            //Test failed because of exception
            fail();
        }
    }

    /**
     * Tests the isFree() method in the Model class.
     * @author Luka Stoehr
     */
    @Test
    public void isFreeTest(){
        createFreshModel();

        assertTrue(model.isFree(new Position(0,0)));
        Rock rock = new Rock(model);
        Hero hero = new Hero(1,1,1,1,1,"a", 1,1,1,model);
        Thanos thanos = new Thanos(model);
        SoulStone stone = new SoulStone(1,model);

        rock.place(new Position(0,1));
        hero.place(new Position(4,3));
        thanos.place(new Position(2,4));
        stone.place(new Position(4,4));

        assertFalse(model.isFree(new Position(0,1)));
        assertFalse(model.isFree(new Position(4,3)));
        assertFalse(model.isFree(new Position(2,4)));
        assertFalse(model.isFree(new Position(4,4)));
    }

    /**
     * Tests the seeThroughField() method.
     * @author Luka Stoehr
     */
    @Test
    public void seeThroughFieldTest(){
        createFreshModel();

        assertTrue(model.seeThroughField(new Position(3,2)));

        SoulStone stone = new SoulStone(1, model);
        stone.place(new Position(3,2));
        assertTrue(model.seeThroughField(stone.getPosition()));

        Hero hero = player2Team[2];
        hero.place(new Position(4,4));
        assertFalse(model.seeThroughField(hero.getPosition()));

        Rock rock = new Rock(model);
        rock.place(new Position(3,1));
        assertFalse(model.seeThroughField(rock.getPosition()));

        Thanos thanos = new Thanos(model);
        thanos.place(new Position(4,1));
        assertFalse(model.seeThroughField(thanos.getPosition()));
    }

    @Test
    public void checkLineOfSightTest(){
        createFreshModel();

        assertTrue(model.checkLineOfSight(new Position(0,0), new Position(9,9)));
        assertTrue(model.checkLineOfSight(new Position(0,0), new Position(0,9)));
        assertTrue(model.checkLineOfSight(new Position(0,0), new Position(9,0)));
        assertTrue(model.checkLineOfSight(new Position(9,5), new Position(0,0)));
        assertTrue(model.checkLineOfSight(new Position(1,1), new Position(2,1)));
        assertTrue(model.checkLineOfSight(new Position(1,1), new Position(1,2)));
        assertTrue(model.checkLineOfSight(new Position(0,0), new Position(9,4)));
        assertTrue(model.checkLineOfSight(new Position(6,7), new Position(2,1)));
        assertTrue(model.checkLineOfSight(new Position(9,9), new Position(0,0)));
        assertTrue(model.checkLineOfSight(new Position(4,3), new Position(4,3)));

        Rock rock = new Rock(model);
        rock.place(new Position(3,2));

        assertTrue(model.checkLineOfSight(new Position(2,1), new Position(4,1)));
        assertTrue(model.checkLineOfSight(new Position(2,0), new Position(2,5)));
        assertTrue(model.checkLineOfSight(new Position(2,0), new Position(5,3)));
        assertTrue(model.checkLineOfSight(new Position(3,3), new Position(2,3)));

        assertFalse(model.checkLineOfSight(new Position(3,0), new Position(3,9)));
        assertFalse(model.checkLineOfSight(new Position(3,9), new Position(3,0)));
        assertFalse(model.checkLineOfSight(new Position(7,2), new Position(2,2)));

        assertTrue(model.checkLineOfSight(new Position(7,2), new Position(3,2)));

        assertFalse(model.checkLineOfSight(new Position(5,0), new Position(2,3)));
        assertFalse(model.checkLineOfSight(new Position(1,0), new Position(6,5)));
        assertFalse(model.checkLineOfSight(new Position(1,0), new Position(8,5)));
        assertFalse(model.checkLineOfSight(new Position(9,0), new Position(2,3)));
        assertFalse(model.checkLineOfSight(new Position(2,3), new Position(9,0)));
        assertFalse(model.checkLineOfSight(new Position(9,0), new Position(1,2)));

        Rock rock2 = new Rock(model);
        rock2.place(new Position(3,5));
        assertFalse(model.checkLineOfSight(new Position(4,0), new Position(3,9)));
    }

    /**
     * This method tests the singleConfigCharacterToHero() method.
     * @author Luka Stoehr
     */
    @Test
    public void singleConfigCharacterToHeroTest(){
        ConfigHero scc = new ConfigHero(1,"Horst", 2, 7, 4, 10, 3 ,25);

        createFreshModel();

        Hero hero = model.singleConfigCharacterToHero(scc, 1, 4);
        assertEquals(scc.name, hero.name);
        assertEquals(scc.AP, hero.maxActionPoints);
        assertEquals(scc.HP, hero.maxHealthPoints);
        assertEquals(scc.MP, hero.maxMovementPoints);
        assertEquals(scc.meleeDamage, hero.nearAttackDamage);
        assertEquals(scc.rangeCombatReach, hero.farAttackDistance);
        assertEquals(scc.rangeCombatDamage, hero.farAttackDamage);
        assertEquals(model, hero.model);
        assertEquals(1, hero.PID);
        assertEquals(4, hero.ID);
    }

    /**
     * This method tests the onField() method.
     * @author Luka Stoehr
     */
    @Test
    public void onFieldTest(){
        createFreshModel();
        assertTrue(model.onField(new Position(0,0)));
        assertTrue(model.onField(new Position(0,1)));
        assertTrue(model.onField(new Position(5,8)));
        assertTrue(model.onField(new Position(0,9)));
        assertTrue(model.onField(new Position(9,0)));
        assertTrue(model.onField(new Position(9,9)));

        assertFalse(model.onField(new Position(-1,0)));
        assertFalse(model.onField(new Position(-1,-7)));
        assertFalse(model.onField(new Position(0,-1)));
        assertFalse(model.onField(new Position(5,10)));
        assertFalse(model.onField(new Position(-8,10)));
        assertFalse(model.onField(new Position(4,10)));
        assertFalse(model.onField(new Position(8,27)));
        assertFalse(model.onField(new Position(4,42)));
        assertFalse(model.onField(new Position(11,15)));
    }
}
