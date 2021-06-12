package logic.model;

import communication.Profile;
import logic.Controller.Controller;
import logic.gameObjects.*;
import parameter.ConfigHero;
import parameter.MatchConfig;

import java.util.Random;

import static java.lang.Math.*;

/**
 * Model class for a match, which represents the current state of the game.
 * @author Luka Stoehr
 * @author Adrian Groeber
 */
public class Model {
    /**
     * GameID for this match.
     */
    public String gameID;
    /**
     * Player object for player one.
     */
    public Player playerOne;
    /**
     * Player object for player two.
     */
    public Player playerTwo;
    /**
     * Current round, starting with 0.
     */
    public int round;
    /**
     * Number of rounds after which Thanos appears
     */
    public final int overlength;

    /**
     * Maximum number of InfinityStones player one has owned during the match. Relevant to choose the winner if Thanos has atomized everybody.
     */
    public int maxNrInftyStonesPlayerOne;
    /**
     * Maximum number of InfinityStones player two has owned during the match. Relevant to choose the winner if Thanos has atomized everybody.
     */
    public int maxNrInftyStonesPlayerTwo;
    /**
     * Number of heroes from the other team player one knocked out. Relevant to choose the winner if Thanos has atomized everybody.
     */
    public int knockedOpHeroPlayerOne;
    /**
     * Number of heroes from the other team player two knocked out. Relevant to choose the winner if Thanos has atomized everybody.
     */
    public int knockedOpHeroPlayerTwo;
    /**
     * Amount of damage that was caused by player one. Relevant to choose the winner if Thanos has atomized everybody.
     */
    public int causedDamagePlayerOne;
    /**
     * Amount of damage that was caused by player two. Relevant to choose the winner if Thanos has atomized everybody.
     */
    public int causedDamagePlayerTwo;

    /**
     * 2D-array that represents the game field, containing all Rocks, Heroes...
     */
    public Placeable[][] field;

    /**
     * Reference to spaceStone
     */
    public SpaceStone spaceStone;
    /**
     * Reference to mindStone
     */
    public MindStone mindStone;
    /**
     * Reference to realityStone
     */
    public RealityStone realityStone;
    /**
     * Reference to powerStone
     */
    public PowerStone powerStone;
    /**
     * Reference to timeStone
     */
    public TimeStone timeStone;
    /**
     * Reference to soulsStone
     */
    public SoulStone soulStone;

    /**
     * Reference to NPC Goose. Null, if Thanos has not shown up yet.
     */
    public Goose goose;
    /**
     * Reference to StanLee. Null, if Thanos has not shown up yet.
     */
    public StanLee stanLee;
    /**
     * Reference to Thanos. Null, if Thanos has not shown up yet.
     */
    public Thanos thanos;

    /**
     * Refernce to the Controller which controls this match and therefore this model.
     */
    public Controller controller;

    /**
     * Constructor for the Model class
     * @author Luka Stoehr
     * @author Adrian Groeber
     * @param fieldSizeX Length of field on x axis.
     * @param fieldSizeY Length of field on y axis.
     * @param overlength Number of round in which Thanos appears.
     * @param playerOne Player object for player 1
     * @param playerTwo Player object for player 2
     * @param controller The controller that runs this match
     */
    public Model(int fieldSizeX, int fieldSizeY, int overlength, Player playerOne, Player playerTwo, Controller controller){
        this.field = new Placeable[fieldSizeX][fieldSizeY];
        this.overlength = overlength;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        //Create a gameID with a random part
        gameID = "ThisReallyPutsASmileOnMyFace" + Math.random();
        System.out.println(gameID);
        this.controller = controller;

        // Create the InfinityStones with values from configuration file
        MatchConfig mc = controller.configuration.matchConfig;
        this.spaceStone = new SpaceStone(mc.spaceStoneCD, this);
        this.mindStone = new MindStone(mc.mindStoneCD, mc.mindStoneDMG, this);
        this.realityStone = new RealityStone(mc.realityStoneCD, this);
        this.powerStone = new PowerStone(mc.powerStoneCD, this);
        this.timeStone = new TimeStone(mc.timeStoneCD, this);
        this.soulStone = new SoulStone(mc.soulStoneCD, this);

    }

    /**
     * Checks if the field at a certain Position is free. If position is not on the field, this method returns
     * false.
     * @author Luka Stoehr
     * @param position Position to check.
     * @return True if field is free.
     */
    public boolean isFree(Position position){
        if(!onField(position)) return false;
        return this.field[position.getX()][position.getY()] == null;
    }

    /**
     * Checks if a Position is inside of the range of the field array.
     * @author Luka Stoehr
     * @param position Position to check
     * @return Whether position is on the field
     */
    public boolean onField(Position position){
        // Check if Position is outside the field
        return position.getX() >= 0 && position.getY() >= 0 && position.getX() < field.length && position.getY() < field[0].length;
    }

    /**
     * Checks if a certain field allows a line of sight or blocks it.
     * @author Luka Stoehr
     * @param position The position of the field to check.
     * @return Whether the field is "see-through"
     */
    public boolean seeThroughField(Position position){
        Placeable objectOnField = this.field[position.getX()][position.getY()];
        if(objectOnField == null){
            return true;
        }else if(objectOnField instanceof InfinityStone){
            return true;
        }else{
            return false;
        }
    }

    /**
     * This method checks if a line of sight exists between two fields. For that it checks
     * every field that is in the line of sight and returns false if a Placeable on one
     * of the fields blocks the line of sight.
     * The algorithm is inspired by: https://www.redblobgames.com/grids/line-drawing.html
     * @author Luka Stoehr
     * @param a Start Position
     * @param b End Position
     * @return Whether the line of sight is unobstructed between field a and b.
     */
    public boolean checkLineOfSight(Position a, Position b){
        int ax = a.getX();
        int ay = a.getY();
        int bx = b.getX();
        int by = b.getY();

        int deltaX = bx - ax;
        int deltaY = by - ay;

        if(deltaX == 0 && deltaY == 0) return true;

        int length = max(abs(deltaX), abs(deltaY));
        int numberOfChecks = 10*length;

        for(int i = 0; i <=numberOfChecks; i++){
            double t = ((double) i)/numberOfChecks;    // t goes from 0 to 1
            //Linearly interpolate the coordinates, t=0 is start field, t=1 is end field
            double currentX = ax + t*(bx-ax);
            double currentY = ay + t*(by-ay);
            if(currentY == 0.5 || currentX == 0.5) continue;    // This coordinate is on the grid/between two fields
            if((int)Math.round(currentX) == ax && (int)Math.round(currentY) == ay) continue; //This is the start field
            if((int)Math.round(currentX) == bx && (int)Math.round(currentY) == by) continue; //This is the end field

            if(!seeThroughField(new Position((int)Math.round(currentX), (int)Math.round(currentY)))) return false;
        }
        return true;
    }


    /**
     * This method is used to set the hero selection of a player. It converts the ConfigHero[] playerTeam
     * to a Hero[] and sets the PID and IDs of the Heroes accordingly.
     * @author Adrian Groeber
     * @author Luka Stoehr
     * @param profile the profile of the player for which the selection has been confirmed
     * @param playerTeam the Hero set of the player which has been accepted
     * @return returns true if the team was successfully set to one of the players
     */
    public boolean setPlayerTeam(Profile profile, ConfigHero[] playerTeam){
        if(profile.equals(playerOne.profile) && playerTeam.length == 6){
            Hero[] heroArray = new Hero[6];
            //Convert ConfigHero to Hero and set PID and ID of each hero
            for(int i = 0; i < 6; i++){
                ConfigHero ccf = playerTeam[i];
                heroArray[i] = new Hero(1, i, ccf.HP, ccf.MP, ccf.AP, ccf.name,
                        ccf.meleeDamage, ccf.rangeCombatDamage, ccf.rangeCombatReach, this);
            }
            playerOne.playerTeam = heroArray;
            playerOne.selectionConfirmed = true;
            return true;
        }
        else if(profile.equals(playerTwo.profile) && playerTeam.length == 6){
            Hero[] heroArray = new Hero[6];
            //Convert ConfigHero to Hero and set PID and ID of each hero
            for(int i = 0; i < 6; i++){
                ConfigHero ccf = playerTeam[i];
                heroArray[i] = new Hero(2, i, ccf.HP, ccf.MP, ccf.AP, ccf.name,
                        ccf.meleeDamage, ccf.rangeCombatDamage, ccf.rangeCombatReach, this);
            }
            playerTwo.playerTeam = heroArray;
            playerTwo.selectionConfirmed = true;
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Method that counts how many InfinityStones each player currently has.
     * The max number of InfinityStones must be checked regularly, as this is important
     * to decide who wins if Thanos destroys everybody.
     * @author Luka Stoehr
     */
    public void checkMaxNrInftyStones(){
        int counterPlayer1 = 0;
        for(Hero hero: this.playerOne.playerTeam){
            counterPlayer1 += hero.inventory.size();
        }
        int counterPlayer2 = 0;
        for(Hero hero: this.playerTwo.playerTeam){
            counterPlayer1 += hero.inventory.size();
        }

        if(counterPlayer1 > this.maxNrInftyStonesPlayerOne) this.maxNrInftyStonesPlayerOne = counterPlayer1;
        if(counterPlayer2 > this.maxNrInftyStonesPlayerTwo) this.maxNrInftyStonesPlayerTwo = counterPlayer2;
    }

    /**
     * This method converts a SingleConfigCharacter to a Hero object. This method is part of the Model class,
     * because each Hero object needs a model when it is created.
     * @param cc SingleConfigCharacter to convert
     * @param PID PlayerID (either 1 or 2) for this Hero
     * @param ID ID of this hero (from 0 to 5)
     * @return New Hero object
     * @author Luka Stoehr
     */
    public Hero singleConfigCharacterToHero(ConfigHero cc, int PID, int ID){
        return new Hero(PID, ID, cc.HP, cc.MP, cc.AP, cc.name, cc.meleeDamage, cc.rangeCombatDamage, cc.rangeCombatReach, this);
    }

    /**
     * Checks if the game has ended and if so, returns the winner.
     * @author Luka Stoehr
     * @return Winner of this match, or null if it is still running
     */
    public Player getWinner(){
        boolean notAtomizedHeroLeft = false;
        //Check if Player1 has won
        for(Hero hero: this.playerOne.playerTeam){
            if(!hero.atomized) notAtomizedHeroLeft = true;
            if(hero.inventory.size() == 6){
                return this.playerOne;
            }
        }
        //Check if Player2 has won
        for(Hero hero: this.playerTwo.playerTeam){
            if(!hero.atomized) notAtomizedHeroLeft = true;
            if(hero.inventory.size() == 6){
                return this.playerTwo;
            }
        }
        //Check if Thanos has atomized everybody
        if(!notAtomizedHeroLeft){
            if(maxNrInftyStonesPlayerOne > maxNrInftyStonesPlayerTwo){
                return playerOne;
            }else if(maxNrInftyStonesPlayerTwo > maxNrInftyStonesPlayerOne){
                return playerTwo;
            }else if(knockedOpHeroPlayerOne > knockedOpHeroPlayerTwo){
                return playerOne;
            }else if(knockedOpHeroPlayerTwo > knockedOpHeroPlayerOne){
                return playerTwo;
            }else if(causedDamagePlayerOne > causedDamagePlayerTwo){
                return playerOne;
            }else if(causedDamagePlayerTwo > causedDamagePlayerOne){
                return playerTwo;
            }else{
                // Decide randomly
                Random r = new Random();
                if(r.nextInt() % 2 == 0){
                    return playerOne;
                }else{
                    return playerTwo;
                }
            }
        }
        // The game is still on!
        return null;
    }
}
