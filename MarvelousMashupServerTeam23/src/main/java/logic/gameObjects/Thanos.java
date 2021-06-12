package logic.gameObjects;

import communication.messages.enums.EntityID;
import communication.messages.events.character.MoveEvent;
import communication.messages.events.entity.ConsumedMPEvent;
import communication.messages.events.entity.DestroyedEntityEvent;
import logic.model.Model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements all logic that is necessary for the NPC Thanos.
 * @author Luka Stoehr
 */
public class Thanos extends Placeable implements Attackable{
    /**
     * Current amount of movement points Thanos has left.
     */
    public int movementPoints;
    /**
     * The maximum amount of movement points, to which the movementPoints were reset in this round.
     * This number is incremented in each round, as Thanos gets more and more movement points.
     */
    public int currentMaxMovementPoints;
    /**
     * Inventory containing all the InfinityStones Thanos currently owns.
     */
    public LinkedList<InfinityStone> inventory;

    /**
     * Constructor for Thanos class.
     * @author Luka Stoehr
     * @param model Model for this match.
     */
    public Thanos(Model model){
        super(EntityID.NPC, 2); // Thanos has a fixed ID
        this.model = model;
        for(Hero hero: model.playerOne.playerTeam){
            if(hero.getHealthPoints() > 0 && hero.getMovementPoints() > this.movementPoints) this.movementPoints = hero.movementPoints;
        }
        for(Hero hero: model.playerTwo.playerTeam){
            if(hero.getHealthPoints() > 0 && hero.getMovementPoints() > this.movementPoints) this.movementPoints = hero.movementPoints;
        }
        //Save value of maximum movement points. This will increase in each round
        this.currentMaxMovementPoints = this.movementPoints;
        this.inventory = new LinkedList<>();
    }

    /**
     * Thanos is immune to all kinds of attacks.
     * @author Luka Stoehr
     * @param damage How much damage the attack theoretically causes
     * @return 0
     */
    @Override
    public int attacked(int damage) {
        return 0;
    }

    /**
     * Reduces the MovementPoints of thanos and creates an ConsumedMPEvent.
     * @param amount Amount by which the MPs are decreased
     * @author Luka Stoehr
     * @return True if successful
     */
    public boolean reduceMP(int amount){
        if(this.movementPoints >= amount) {
            this.movementPoints -= amount;
            this.model.controller.eventList.add(
                    new ConsumedMPEvent(this.getIDs(), this.getPosAsArray(), amount)
            );
            return true;
        }else{
            return false;
        }
    }

    /**
     * This method removes all Heroes in selectedHeroes from the game, if Thanos has all InfinityStones
     * @author Luka Stoehr
     * @param selectedHeroes The heroes that will be removed
     * @return Whether removing the heroes was successful.
     */
    public boolean atomize(List<Hero> selectedHeroes) {
        if (this.inventory.size() == 6) {
            for(Hero hero: selectedHeroes){
                model.controller.eventList.add(
                        new DestroyedEntityEvent(hero.getPosAsArray(), hero.getIDs())
                );
                Position pos = hero.getPosition();
                model.field[pos.getX()][pos.getY()] = null;
                hero.healthPoints = 0;
                hero.atomized = true;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method moves thanos on the field. If the target field is occupied by another
     * her, places are swapped. If there is an InfinityStone, it is collected.
     * @author Luka Stoehr
     * @param newPosition where the object is moved to, must be a neighbouring field
     * @return Whether moving succeeded.
     */
    @Override
    public boolean move(Position newPosition) {
        Position oldPos = this.getPosition();
        if(oldPos == null || !this.isNeighbour(newPosition) || this.movementPoints <= 0){
            return false;       // Placeable was not found and can therefore not be moved, use place() instead
                                // Or the new field is not a neighbour. move() currently only supports on step
                                // Or MPs are zero
        }

        Placeable target = model.field[newPosition.getX()][newPosition.getY()];
        if(target == null) {
            // target field is free -> Placeable can be moved
            model.field[oldPos.getX()][oldPos.getY()] = null;
            model.field[newPosition.getX()][newPosition.getY()] = this;
            model.controller.eventList.add(
                    new MoveEvent(this.getIDs(), oldPos.toArray(), newPosition.toArray())
            );
            this.reduceMP(1);
            return true;
        }else if (target instanceof Hero) {
            model.field[oldPos.getX()][oldPos.getY()] = target;
            model.field[newPosition.getX()][newPosition.getY()] = this;

            for(InfinityStone stone: ((Hero) target).inventory){    //Thanos takes all InfinityStones
                this.inventory.add(stone);
                // ((Hero) target).inventory.remove(stone);
            }
            ((Hero) target).inventory.clear();
            ((Hero) target).attacked(Integer.MAX_VALUE);      //Thanos knocks out all players he encounters
            model.controller.eventList.add(
                    new MoveEvent(this.getIDs(), oldPos.toArray(), newPosition.toArray())
            );
            this.reduceMP(1);
            return true;
        } else if (target instanceof InfinityStone) {
            this.inventory.add((InfinityStone) target);
            model.field[oldPos.getX()][oldPos.getY()] = null;
            model.field[newPosition.getX()][newPosition.getY()] = this;
            this.model.controller.eventList.add(
                    // DestroyedEntityEvent because stone is taken from field
                    new DestroyedEntityEvent(newPosition.toArray(), target.getIDs())
            );
            model.controller.eventList.add(
                    new MoveEvent(this.getIDs(), oldPos.toArray(), newPosition.toArray())
            );
            this.reduceMP(1);
            return true;
        } else if(target instanceof Rock){
            ((Rock) target).attacked(Integer.MAX_VALUE);
            model.field[oldPos.getX()][oldPos.getY()] = null;
            model.field[newPosition.getX()][newPosition.getY()] = this;
            model.controller.eventList.add(
                    new MoveEvent(this.getIDs(), oldPos.toArray(), newPosition.toArray())
            );
            this.reduceMP(1);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Returns the inventory of this class as an int array containing the IDs of the Stones
     * that Thanos carries.
     * @author Luka Stoehr
     * @return int[] containing the IDs of the InfinityStones in the inventory
     */
    public int[] inventoryToArray(){
        int[] array = new int[this.inventory.size()];
        for(int i = 0; i < this.inventory.size(); i++){
            array[i] = this.inventory.get(i).ID;
        }
        return array;
    }

    /**
     * This method carries out one turn by Thanos as specified in the requirements document.
     * If he has all 6 InfinityStones, he atomizes heroes, otherwise he moves towards the
     * closest InfinityStone that is present on the board. In this case it does not matter
     * whether the InfinityStone is on a grass field or in the inventory of a hero.
     * @author Luka Stoehr
     */
    public void thanosTurn(){
        if(this.inventory.size() < 6) {
            //Find all fields with InfinityStones, either lying there or in a heroes inventory
            LinkedList<Position> fieldsWithStones = new LinkedList<>();
            for (int x = 0; x < model.field.length; x++) {
                for (int y = 0; y < model.field[0].length; y++) {
                    if (model.field[x][y] != null) {
                        Placeable onField = model.field[x][y];
                        if (onField instanceof InfinityStone) {
                            fieldsWithStones.add(new Position(x, y));
                        } else if (onField instanceof Hero && ((Hero) onField).inventory.size() > 0) {
                            fieldsWithStones.add(new Position(x, y));
                        }
                    }
                }
            }
            // Choose a field to move towards - take the nearest one with a stone
            Position myPos = this.getPosition();
            double myPosX = myPos.getX();
            double myPosY = myPos.getY();
            double minDistance = Float.MAX_VALUE;
            int minDistanceIndex = 0;

            for(int i = 0; i < fieldsWithStones.size(); i++){
                double targetX = fieldsWithStones.get(i).getX();
                double targetY = fieldsWithStones.get(i).getY();
                double checkDistance = Math.sqrt(Math.pow(Math.abs(myPosX-targetX), 2) + Math.pow(Math.abs(myPosY-targetY), 2));
                if(checkDistance < minDistance){
                    minDistance = checkDistance;
                    minDistanceIndex = i;
                }
            }

            //Move towards target until MP are used up
            int myX = myPos.getX();
            int myY = myPos.getY();
            int targetX = fieldsWithStones.get(minDistanceIndex).getX();
            int targetY = fieldsWithStones.get(minDistanceIndex).getY();

            for (int i = 0; i < this.currentMaxMovementPoints; i++){
                if(targetX > myX && targetY > myY){
                    this.move(new Position(myX + 1, myY + 1));
                }else if(targetX > myX && targetY < myY){
                    this.move(new Position(myX + 1, myY - 1));
                }else if(targetX < myX && targetY < myY){
                    this.move(new Position(myX - 1, myY - 1));
                }else if(targetX < myX && targetY > myY){
                    this.move(new Position(myX - 1, myY + 1));
                }else if(targetX == myX && targetY > myY){
                    this.move(new Position(myX, myY + 1));
                }else if(targetX == myX && targetY < myY){
                    this.move(new Position(myX, myY - 1));
                }else if(targetX > myX && targetY == myY){
                    this.move(new Position(myX + 1, myY));
                }else if(targetX < myX && targetY == myY){
                    this.move(new Position(myX - 1, myY));
                }
            }
        }else{
            // Choose heroes to atomize
            LinkedList<Hero> nonAtomizedHeroes = new LinkedList<>();
            for(Hero hero: model.playerOne.playerTeam){
                if(hero.atomized == false) nonAtomizedHeroes.add(hero);
            }
            for(Hero hero: model.playerTwo.playerTeam){
                if(hero.atomized == false) nonAtomizedHeroes.add(hero);
            }
            Collections.shuffle(nonAtomizedHeroes);
            //Take only half of the randomly ordered heroes
            int removeNumber = nonAtomizedHeroes.size()/2;
            for(int i = 0; i < removeNumber; i++){
                nonAtomizedHeroes.pop();
            }
            this.atomize(nonAtomizedHeroes);    //atomize
        }
    }
}
