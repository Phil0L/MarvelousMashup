package logic.gameObjects;

import logic.model.TestUsingModel;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.Port;

import static org.junit.jupiter.api.Assertions.*;

public class PortalTest extends TestUsingModel {

    @Test
    public void teleportTest(){
        createFreshModel();
        Hero hero = model.playerOne.playerTeam[2];
        hero.place(new Position(4,4));
        Portal p1 = new Portal(model);
        Portal p2 = new Portal(model);
        model.field[3][4] = p1;
        model.field[0][0] = p2;

        assertFalse(p2.isNeighbour(hero.getPosition()));
        assertTrue(hero.move(new Position(3,4)));
        assertTrue(p2.isNeighbour(hero.getPosition()));
    }
}
