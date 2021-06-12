package logic.Controller;

import communication.messages.events.character.ExchangeInfinityStoneEvent;
import communication.messages.requests.DisconnectRequest;
import communication.messages.requests.ExchangeInfinityStoneRequest;
import logic.TestUsingController;
import logic.gameObjects.Hero;
import logic.gameObjects.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for some of the controller methods. Some methods of the Controller
 * are also called by the tests in class communication.InGameTest
 * @auhtor Luka Stoehr
 */
public class ControllerTest extends TestUsingController {

    @Test
    public void handleRequest2_ExchangeInfinityStoneRequest_Test(){
        createController();
        controller.startGame();

        Hero hero1 = model.playerOne.playerTeam[1];
        Hero hero2 = model.playerOne.playerTeam[2];

        Position h1_oldPos = hero1.getPosition();
        Position h2_oldPos = hero2.getPosition();
        model.field[h1_oldPos.getX()][h1_oldPos.getY()] = null;
        model.field[h2_oldPos.getX()][h2_oldPos.getY()] = null;

        model.field[3][4] = hero1;
        model.field[3][5] = hero2;

        hero1.inventory.add(model.mindStone);
        assertTrue(hero1.inventory.contains(model.mindStone));
        assertFalse(hero2.inventory.contains(model.mindStone));

        controller.turnOrder.add(controller.turnCount, hero1);
        HandleReturn hr = controller.handleRequest(model.playerOne,
                new ExchangeInfinityStoneRequest(hero1.getIDs(), hero2.getIDs(),
                        hero1.getPosAsArray(), hero2.getPosAsArray(),
                        model.mindStone.getIDs())
        );

        assertFalse(hero1.inventory.contains(model.mindStone));
        assertTrue(hero2.inventory.contains(model.mindStone));
        assertTrue(hr.eventList.get(hr.eventList.size()-2) instanceof ExchangeInfinityStoneEvent);
    }
}
