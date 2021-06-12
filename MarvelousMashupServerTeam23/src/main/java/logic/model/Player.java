package logic.model;


import communication.Profile;
import communication.messages.Message;
import communication.messages.enums.Role;
import logic.gameObjects.Hero;
import logic.timer.SimpleTimer;
import parameter.ConfigHero;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 *
 * Player-class. Describes one of the two players which plays a Marvelous MashUp
 * game.
 *
 * @author Adrian Groeber
 * @author Matthias Ruf
 */
public class Player {



    /**
     * The role of the player. It is either a PLAYER or a KI
     */
    public Role role;
    public Hero[] playerTeam;    // Getter and Setter method?
    /**
     * The Array of ConfigHeroes which is sent to the player in the GameAssignment message to choose his Heroes from.
     */
    public ConfigHero[] teamOptions;


    int strikes = 0;

    public ArrayList<Message> wrongMessage = new ArrayList<>();

    /**
     * The profile associated with the player object
     */
    public Profile profile;
    /**
     * The selectionConfirmed Boolean is true if the player has confirmed his Hero selection, else it is false
     */
    public boolean selectionConfirmed;

    /**
     * A timer to limit the time a player is able to reconnect after a connection loss
     */
    public SimpleTimer timeoutTimer;


    /**
     * The constructor of the Player class. it uses the profile of a connected client to create a new player object
     * before a new game model is created.
     * @author Adrian Groeber
     * @param profile the profile of the client who will play the game as a player.
     * @param role the role of the player (KI or PLAYER)
     */
    public Player(Profile profile, Role role) {
        this.profile = profile;
        this.role = role;
        selectionConfirmed = false;
    }

    /**
     * Checks if a certain Hero is part of the team of this Player object.
     * @param checkHero Hero to search for
     * @return True, if checkHero belongs to this player
     */
    public boolean oneOfMyHeroes(Hero checkHero){
        for(Hero hero: playerTeam){
            if(hero.equals(checkHero)){
                return true;
            }
        }
        return false;
    }

}