package logic;

import communication.Profile;
import communication.messages.enums.Role;
import logic.Controller.Controller;
import logic.model.Model;
import logic.model.Player;
import parameter.ConfigHero;
import parameter.Configuration;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Abstract class for all test classes that need a controller. This class creates one
 * for the tests.
 * @author Luka Stoehr
 */
public abstract class TestUsingController {
    public Model model;
    public Controller controller;

    /**
     * This method creates a fresh controller (and necessarily also a model) for
     * the tests that need it. To create a fresh state, this method can simply be called again.
     * This method assumes, that the files "asgard.json", "marvelhero.json" and "matchconfig_1.json"
     * are located in the root directory of this project.
     * @author Luka Stoehr
     */
    public void createController(){
        try {
            this.controller = new Controller(new Configuration("asgard.json", "marvelheros.json", "matchconfig_1.json"));
        } catch (IOException e) {
            fail("Accessing config files 'asgard.json', 'marvelhero.json','matchconfig_1.json' failed. These have to be present in the root directory of this project, for this method to work");
            e.printStackTrace();
        }
        Player p1 = new Player(new Profile(null, "id/1", "1"), Role.PLAYER);
        Player p2 = new Player(new Profile(null, "id/2", "2"), Role.PLAYER);
        controller.createModel(p1,p2);
        //For easier access
        this.model = controller.model;

        //Choose a team for each player
        ConfigHero[] team1 = new ConfigHero[6];
        ConfigHero[] team2 = new ConfigHero[6];
        for(int i = 0; i < 6; i++){
            team1[i] = controller.configuration.characterConfig.characters[i];
            team2[i] = controller.configuration.characterConfig.characters[i+6];
        }

        model.setPlayerTeam(new Profile(null, "id/1","1"), team1);
        model.setPlayerTeam(new Profile(null, "id/2","2"), team2);

    }
}
