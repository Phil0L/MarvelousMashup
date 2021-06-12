package logic.gameObjects;

import logic.model.TestUsingModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedList;


/**
 * Contains tests for the Thanos class.
 * @author Luka Stoehr
 */
public class ThanosTest extends TestUsingModel {

    /**
     * Tests the attacked() method, which should not do anything
     * as Thanos is immune to attacks.
     * @author Luka Stoehr
     */
    @Test
    public void attackedTest(){
        createFreshModel();
        Thanos thanos = new Thanos(model);
        assertEquals(0, thanos.attacked(10));
    }

    /**
     * Tests the atomize() method.
     * @author Luka Stoehr
     */
    @Test
    public void atomizeTest(){
        createFreshModel();

        int counter = 0;
        for(Hero h: player1Team){
            h.place(new Position(0,counter));
            counter++;
        }
        int counter2 = 0;
        for(Hero h: player2Team){
            h.place(new Position(1, counter2));
            counter2++;
        }

        Thanos thanos = new Thanos(model);
        LinkedList<Hero> list = new LinkedList<>();
        list.add(player1Team[2]);
        list.add(player1Team[4]);
        list.add(player2Team[5]);

        assertFalse(thanos.atomize(list));
        assertFalse(player1Team[2].atomized);
        assertFalse(player1Team[4].atomized);
        assertFalse(player2Team[5].atomized);

        MindStone ms = new MindStone(1, model.controller.configuration.matchConfig.mindStoneDMG, model);
        PowerStone ps = new PowerStone(1, model);
        RealityStone rs = new RealityStone(1, model);
        SoulStone sos = new SoulStone(1, model);
        SpaceStone sps = new SpaceStone(1, model);
        TimeStone ts = new TimeStone(1, model);
        thanos.inventory.add(ms);
        thanos.inventory.add(ps);
        thanos.inventory.add(rs);
        thanos.inventory.add(sos);
        thanos.inventory.add(sps);
        thanos.inventory.add(ts);

        assertTrue(thanos.atomize(list));
        assertTrue(player1Team[2].atomized);
        assertTrue(player1Team[4].atomized);
        assertTrue(player2Team[5].atomized);
        assertNull(player1Team[2].getPosition());
        assertNull(player1Team[4].getPosition());
        assertNull(player2Team[5].getPosition());
    }

    /**
     * Tests the move() method in the Thanos class.
     * @author Luka Stoehr
     */
    @Test
    public void moveTest(){
        createFreshModel();

        Thanos thanos = new Thanos(model);
        thanos.place(new Position(4,4));
        assertTrue(thanos.move(new Position(3,4)));
        assertFalse(thanos.move(new Position(1,1)));    // Not a neighbouring field

        SoulStone soulStone = new SoulStone(1, model);
        soulStone.place(new Position(3,5));
        assertFalse(thanos.inventory.contains(soulStone));
        assertTrue(thanos.move(new Position(3,5)));
        assertTrue(thanos.inventory.contains(soulStone));

        Rock rock = new Rock(model);
        assertTrue(rock.place(new Position(4,6)));
        assertTrue(thanos.move(new Position(4,6)));
        assertNull(rock.getPosition());

        Hero hero = player2Team[2];
        hero.place(new Position(5,6));
        MindStone mindStone = new MindStone(1, model.controller.configuration.matchConfig.mindStoneDMG, model);
        PowerStone powerStone = new PowerStone(1, model);
        hero.inventory.add(mindStone);
        hero.inventory.add(powerStone);

        assertTrue(thanos.move(new Position(5,6)));
        assertEquals(new Position(4,6), hero.getPosition());
        assertEquals(0, hero.healthPoints);
        assertTrue(thanos.inventory.contains(mindStone));
        assertTrue(thanos.inventory.contains(powerStone));

        assertFalse(thanos.move(new Position(5,6)));      // Already on this field

        assertTrue(thanos.move(new Position(6,6)));
        assertTrue(thanos.move(new Position(7,6)));
        assertTrue(thanos.move(new Position(8,6)));
        assertTrue(thanos.move(new Position(9,6)));
        assertTrue(thanos.move(new Position(9,5)));
        assertTrue(thanos.move(new Position(9,4)));
        assertTrue(thanos.move(new Position(9,3)));
        assertTrue(thanos.move(new Position(9,2)));
        assertTrue(thanos.move(new Position(9,1)));
        assertTrue(thanos.move(new Position(9,0)));
        assertTrue(thanos.move(new Position(8,0)));
        assertTrue(thanos.move(new Position(8,1)));
        assertTrue(thanos.move(new Position(8,2)));
        assertTrue(thanos.move(new Position(8,3)));
        assertTrue(thanos.move(new Position(8,4)));
        assertTrue(thanos.move(new Position(8,5)));

        assertFalse(thanos.move(new Position(8,6)));    // No MPs left
    }
}
