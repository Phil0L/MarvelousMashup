package logic.model;

import communication.Profile;
import communication.messages.enums.Role;
import logic.Controller.Controller;
import logic.gameObjects.Hero;
import logic.model.Model;
import parameter.ConfigHero;
import parameter.Configuration;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Abstract class for all Test classes that need a Model to work.
 * @author Luka Stoehr
 */
public abstract class TestUsingModel {

    public Hero[] player1Team;
    public Hero[] player2Team;
    public Model model;
    public Controller controller;
    /**
     * This method fills the class variables player1Team, player2Team and model
     * with six characters for each team and an empty field. This model can then be used
     * for tests and can easily be reset by calling this method again.
     * This method assumes, that the files "asgard.json", "marvelhero.json" and "matchconfig_1.json"
     * are located in the root directory of this project.
     * @author Luka Stoehr
     */
    public void createFreshModel(){
        try {
            this.controller = new Controller(new Configuration("asgard.json", "marvelheros.json", "matchconfig_1.json"));
        } catch (IOException e) {
            //Test can't work without Controller
            fail();
            e.printStackTrace();
        }
        Player p1 = new Player(new Profile(null, "id/1", "1"), Role.PLAYER);
        Player p2 = new Player(new Profile(null, "id/2", "2"), Role.PLAYER);
        this.model = new Model(10,10,10,p1,p2, controller);

        this.player1Team = new Hero[6];
        this.player1Team[0] = new Hero(1,0, 20, 20, 20, "H0", 5, 5, 5, model);
        this.player1Team[1] = new Hero(1, 1, 20, 2, 3, "H1", 5, 5, 5, model);
        this.player1Team[2] = new Hero(1,2,20, 20, 20, "H2", 5, 5, 5, model);
        this.player1Team[3] = new Hero(1,3,20, 20, 20, "H3", 5, 5, 5, model);
        this.player1Team[4] = new Hero(1,4,20, 20, 20, "H4", 5, 5, 5, model);
        this.player1Team[5] = new Hero(1,5,20, 20, 20, "H5", 5, 5, 5, model);

        this.player2Team = new Hero[6];
        this.player2Team[0] = new Hero(2,0,20, 20, 20, "H6", 5, 5, 5, model);
        this.player2Team[1] = new Hero(2,1,20, 20, 20, "H7", 5, 5, 5, model);
        this.player2Team[2] = new Hero(2,2,20, 20, 20, "H8", 5, 5, 5, model);
        this.player2Team[3] = new Hero(2,3,20, 20, 20, "H9", 5, 5, 5, model);
        this.player2Team[4] = new Hero(2,4,20, 20, 20, "H10", 5, 5, 5, model);
        this.player2Team[5] = new Hero(2,5,20, 20, 20, "H11", 5, 5, 5, model);

        ConfigHero[] cfHArray1 = new ConfigHero[6];
        for(int i = 0; i < 6; i++){
            cfHArray1[i] = player1Team[i].toConfigHero();
        }
        ConfigHero[] cfHArray2 = new ConfigHero[6];
        for(int i = 0; i < 6; i++){
            cfHArray2[i] = player2Team[i].toConfigHero();
        }
        this.model.setPlayerTeam(new Profile(null, "id/1", "1"), cfHArray1);
        this.model.setPlayerTeam(new Profile(null, "id/2", "2"), cfHArray2);
    }
}
