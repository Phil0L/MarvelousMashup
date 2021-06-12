package logic.gameObjects;

import communication.messages.IDs;
import communication.messages.enums.EntityID;
import communication.messages.events.character.ExchangeInfinityStoneEvent;
import communication.messages.events.character.MeleeAttackEvent;
import communication.messages.events.character.MoveEvent;
import communication.messages.events.character.RangedAttackEvent;
import communication.messages.events.entity.*;
import communication.messages.objects.InGameCharacter;
import logic.model.Model;
import parameter.ConfigHero;

import java.util.*;

/**
 * Class that represents a hero in the game.
 * @author Luka Stoehr
 */
public class Hero extends  Placeable implements Attackable{
    /**
     * 1 or 2, depending on which player this Hero belongs to.
     * This is sometimes useful even if it stores the same information as Placeable.entityID
     */
    public int PID;

    /**
     * Current number of health points
     */
    int healthPoints;
    /**
     * Current number of movement points
     */
    int movementPoints;
    /**
     * Current number of action points
     */
    int actionPoints;

    /**
     * Maximum number of health points.
     */
    public final int maxHealthPoints;
    /**
     * Maximum number of movement points.
     */
    public final int maxMovementPoints;
    /**
     * Maximum number of action points.
     */
    public final int maxActionPoints;

    /**
     * This heroes name
     */
    public String name;
    /**
     * Inventory, which contains all the InfinityStones this hero owns.
     */
    public LinkedList<InfinityStone> inventory;

    /**
     * Amount of damage that is done in a near attack/melee attack
     */
    public int nearAttackDamage;
    /**
     * Damage that is done in a far attack/ranged attack
     */
    public int farAttackDamage;
    /**
     * Reach of a far attack/ranged attack
     */
    public int farAttackDistance;
    /**
     * True if this hero has been atomized by Thanos.
     */
    public boolean atomized;

    /**
     * Constructor for the Hero class. It fills in all the necessary attributes
     * for when a new Hero is created at the beginning of a game.
     * @author Luka Stoehr
     * @param PID Player ID of this Hero
     * @param ID ID of this player
     * @param maxHP Maximum number of HP as specified in character configuration
     * @param maxMP Maximum number of MP as specified in character configuration
     * @param maxAP Maximum number of AP as specified in character configuration
     * @param name Name of this Hero
     * @param nearAttackDamage NearAttackDamage as specified in character configuration
     * @param farAttackDamage FarAttackDamage as specified in character configuration
     * @param farAttackDistance FarAttackDistance as specified in character configuration
     * @param model Model for this match.
     */
    public Hero(int PID, int ID, int maxHP, int maxMP, int maxAP, String name,
                int nearAttackDamage, int farAttackDamage, int farAttackDistance, Model model){
        super((PID == 1 ? EntityID.P1 : EntityID.P2), ID);
        this.PID = PID;

        this.maxHealthPoints = maxHP;
        this.healthPoints = maxHP;
        this.maxMovementPoints = maxMP;
        this.movementPoints = maxMP;
        this.maxActionPoints = maxAP;
        this.actionPoints = maxAP;

        this.name = name;
        this.inventory = new LinkedList<>();

        this.nearAttackDamage = nearAttackDamage;
        this.farAttackDamage = farAttackDamage;
        this.farAttackDistance = farAttackDistance;
        this.atomized = false;

        this.model = model;
    }

    /**
     * Getter for HP
     * @author Luka Stoehr
     * @return Current HP
     */
    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * Getter for MP
     * @author Luka Stoehr
     * @return Current MP
     */
    public int getMovementPoints() {
        return movementPoints;
    }

    /**
     * Getter for AP
     * @author Luka Stoehr
     * @return Current AP
     */
    public int getActionPoints() {
        return actionPoints;
    }

    /**
     * Reduces the Action Points of this hero and creates an ConsumedAPEvent.
     * @param amount Amount by which the APs are decreased
     * @author Luka Stoehr
     * @return True if successful
     */
    public boolean reduceAP(int amount){
        if(this.actionPoints >= amount){
            this.model.controller.eventList.add(
                    new ConsumedAPEvent(this.getIDs(), this.getPosAsArray(), amount)
            );
            this.actionPoints -= amount;
            return true;
        }else{
            return false;
        }
    }

    /**
     * Reduces the Health Points of this hero and creates an TakenDamageEvent.
     * @param amount Amount by which the HPs are decreased
     * @author Luka Stoehr
     * @return True if successful
     */
    public boolean reduceHP(int amount){
        if(this.healthPoints >= amount){
            this.healthPoints -= amount;
            this.model.controller.eventList.add(
                    new TakenDamageEvent(this.getIDs(), this.getPosAsArray(), amount)
            );
            return true;
        }else{
            return false;
        }
    }

    /**
     * Reduces the MovementPoints of this hero and creates an ConsumedMPEvent.
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
     * Restores the full HealthPoints of this character and creates an HealedEvent in the EventList
     * @author Luka Stoehr
     */
    public void heal(){
        int amount = this.maxHealthPoints - this.healthPoints;
        this.healthPoints = this.maxHealthPoints;
        model.controller.eventList.add(
                new HealedEvent(this.getIDs(), this.getPosAsArray(), amount)
        );
    }

    /**
     * Restores the action points of this character to its maximum value.
     * @author Luka Stoehr
     */
    public void restoreAP(){
        this.actionPoints = this.maxActionPoints;
    }

    /**
     * Restores the movement points of this character to its maximum value.
     * @author Luka Stoehr
     */
    public void restoreMP(){
        this.movementPoints = this.maxMovementPoints;
    }

    /**
     * Calls the infinteraction method of the InfinityStone stone, if the used stone is in the inventory of this hero.
     * @author Luka Stoehr
     * @param stone Used InfinityStone
     * @param position Some Infinteraction need a (target) Position, depending on the used InfinityStone.
     * @return Whether the infinteraction was successful.
     */
    public boolean infinteraction(InfinityStone stone, Position position){
        if(this.inventory.contains(stone) && this.actionPoints > 0 && this.healthPoints > 0){
            boolean success = stone.infinteraction(this, position);
            if(success && !(stone instanceof TimeStone)){
                this.reduceAP(1);
            }
            return success;
        }else{
            return false;
        }
    }

    /**
     * Gives a certain InfinityStone to another hero, if the stone currently is in the inventory of this hero.
     * @author Luka Stoehr
     * @param hero Hero the stone is given to.
     * @param stoneID StoneID of the stone that is transferred.
     * @return True if transfer was successful.
     */
    public boolean giveInfinityStone(Hero hero, int stoneID){
        InfinityStone stone = null;
        for(InfinityStone stoneInInventory: this.inventory){
            if(stoneInInventory.ID == stoneID) stone = stoneInInventory;
        }
        if(stone == null) return false; //You do not own the stone with this ID
        if(this.actionPoints > 0 && this.isNeighbour(hero.getPosition()) && this.inventory.remove(stone)){
            this.reduceAP(1);
            hero.inventory.add(stone);
            this.model.checkMaxNrInftyStones();
            this.model.controller.eventList.add(
              new ExchangeInfinityStoneEvent(this.getIDs(), hero.getIDs(), this.getPosAsArray(), hero.getPosAsArray(), stone.getIDs())
            );
            return true;
        }else{
            return false;
        }
    }

    /**
     * Attacks an Attackable on a neighbouring field
     * @author Luka Stoehr
     * @param position Target position.
     * @return Whether the attack was successful.
     */
    public boolean nearAttack(Position position){
        if(this.isNeighbour(position) &&
                model.field[position.getX()][position.getY()] instanceof Attackable &&
                this.actionPoints > 0 &&
                this.getHealthPoints() > 0){
            Attackable target = (Attackable) model.field[position.getX()][position.getY()];
            int doneDamage = target.attacked(this.nearAttackDamage);
            if(doneDamage > 0){
                this.reduceAP(1);
                this.model.controller.eventList.add(
                    new MeleeAttackEvent(this.getIDs(), ((Placeable)target).getIDs(),
                            this.getPosAsArray(), ((Placeable)target).getPosAsArray(),
                            doneDamage)
                );
                checkKnockedOpponentHeroAndDamage(target, doneDamage);
                return true;
            }
        }
        return false;
    }

    /**
     * Method for a farAttack. You can attack a Hero that is within line of sight, but
     * not on a neighbouring field.
     * @author Luka Stoehr
     * @param position Target Position
     * @return Whether the attack was successful
     */
    public boolean farAttack(Position position){
        if(this.actionPoints <= 0 || this.isNeighbour(position) ||
            !model.checkLineOfSight(position, this.getPosition()) ||
            this.getPosition().equals(position) ||
            this.getHealthPoints() <= 0){
            return false;
        }
        Position ownPosition = this.getPosition();
        double deltaX = ownPosition.getX() - position.getX();
        double deltaY = ownPosition.getY() - position.getY();
        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        Placeable target = model.field[position.getX()][position.getY()];
        if(target instanceof Attackable && distance <= this.farAttackDistance){
            int doneDamage = ((Attackable) target).attacked(this.farAttackDamage);
            this.reduceAP(1);
            this.model.controller.eventList.add(
                    new RangedAttackEvent(this.getIDs(), target.getIDs(),
                            this.getPosAsArray(), target.getPosAsArray(),
                            doneDamage)
            );
            checkKnockedOpponentHeroAndDamage((Attackable) target, doneDamage);
            return true;
        }
        return false;
    }

    /**
     * Creates a unique hash Code
     * @author Luka Stoehr
     * @return Hash code for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(PID, ID, name);
    }

    /**
     * Called if this Hero is attacked. Reduces the HealthPoints of this Hero.
     * @author Luka Stoehr
     * @param damage How much damage the attack theoretically causes.
     * @return The damage that was actually done.
     */
    @Override
    public int attacked(int damage) {
        if(this.healthPoints > damage){
            this.reduceHP(damage);
            return damage;
        }else{
            //Character is knocked out
            int doneDamage = this.healthPoints;
            this.reduceHP(doneDamage);
            //Distribute InfinityStones if there are any in the inventory
            if(this.inventory.size() > 0){
                Random r = new Random();
                for(InfinityStone stone: this.inventory){
                    Position currentPos = this.getPosition();
                    while(true){
                        //Check which neighbouring fields are free
                        LinkedList<Position> freePosList = new LinkedList<>();
                        Position neighbour1 = new Position(currentPos.getX(), currentPos.getY() + 1);
                        if(model.isFree(neighbour1)) freePosList.add(neighbour1);
                        Position neighbour2 = new Position(currentPos.getX() + 1, currentPos.getY() + 1);
                        if(model.isFree(neighbour2)) freePosList.add(neighbour2);
                        Position neighbour3 = new Position(currentPos.getX() + 1, currentPos.getY());
                        if(model.isFree(neighbour3)) freePosList.add(neighbour3);
                        Position neighbour4 = new Position(currentPos.getX() + 1, currentPos.getY() - 1);
                        if(model.isFree(neighbour4)) freePosList.add(neighbour4);
                        Position neighbour5 = new Position(currentPos.getX(), currentPos.getY() - 1);
                        if(model.isFree(neighbour5)) freePosList.add(neighbour5);
                        Position neighbour6 = new Position(currentPos.getX() - 1, currentPos.getY() - 1);
                        if(model.isFree(neighbour6)) freePosList.add(neighbour6);
                        Position neighbour7 = new Position(currentPos.getX() - 1, currentPos.getY());
                        if(model.isFree(neighbour7)) freePosList.add(neighbour7);
                        Position neighbour8 = new Position(currentPos.getX() - 1, currentPos.getY() + 1);
                        if(model.isFree(neighbour8)) freePosList.add(neighbour8);

                        //All neighbouring fields are occupied
                        if(freePosList.size() == 0){
                            //Check which of the neighbouring fields are valid and choose on as new currentPosition
                            LinkedList<Position> onFieldList = new LinkedList<>();
                            if(model.onField(neighbour1)) onFieldList.add(neighbour1);
                            if(model.onField(neighbour2)) onFieldList.add(neighbour2);
                            if(model.onField(neighbour3)) onFieldList.add(neighbour3);
                            if(model.onField(neighbour4)) onFieldList.add(neighbour4);
                            if(model.onField(neighbour5)) onFieldList.add(neighbour5);
                            if(model.onField(neighbour6)) onFieldList.add(neighbour6);
                            if(model.onField(neighbour7)) onFieldList.add(neighbour7);
                            if(model.onField(neighbour8)) onFieldList.add(neighbour8);
                            int chosenCandidate = r.nextInt(onFieldList.size());
                            currentPos = onFieldList.get(chosenCandidate);
                            continue;  //Try again starting from new currentPos
                        }

                        //Choose on of the free neighbouring fields randomly
                        int chosenCandidate = r.nextInt(freePosList.size());
                        stone.place(freePosList.remove(chosenCandidate));
                        break;  //Placed the stone
                    }
                }
                this.inventory.clear();     // All stones were placed on the field
            }
            return doneDamage;
        }
    }

    /**
     * This method moves a hero on the field. If the target field is occupied by another
     * hero or thanos, places are swapped. If there is an InfinityStone, it is collected.
     * @author Luka Stoehr
     * @param newPosition where the object is moved to, must be a neighbouring field
     * @return Whether the hero was moved.
     */
    @Override
    public boolean move(Position newPosition) {
        Position oldPos = this.getPosition();
        if(oldPos == null || !this.isNeighbour(newPosition) || this.movementPoints <= 0 || this.getHealthPoints() <= 0){
            return false;       // Placeable was not found, target is not a neighbour or no MPs left
        }

        Placeable target = model.field[newPosition.getX()][newPosition.getY()];
        if(target == null) {
            // target field is free -> Placeable can be moved
            model.field[oldPos.getX()][oldPos.getY()] = null;
            model.field[newPosition.getX()][newPosition.getY()] = this;
            this.reduceMP(1);
            this.model.controller.eventList.add(
              new MoveEvent(this.getIDs(), oldPos.toArray(), newPosition.toArray())
            );
            return true;
        }else if (target instanceof Hero) {
                model.field[oldPos.getX()][oldPos.getY()] = target;
                model.field[newPosition.getX()][newPosition.getY()] = this;
                this.reduceMP(1);
                this.model.controller.eventList.add(
                        // MoveEvent for this Hero
                        new MoveEvent(this.getIDs(), oldPos.toArray(), newPosition.toArray())
                );
                this.model.controller.eventList.add(
                        // MoveEvent for target
                        new MoveEvent(target.getIDs(), newPosition.toArray(), oldPos.toArray())
                );
                return true;
        } else if (target instanceof InfinityStone) {
                this.inventory.add((InfinityStone) target);
                model.field[oldPos.getX()][oldPos.getY()] = null;
                model.field[newPosition.getX()][newPosition.getY()] = this;
                this.reduceMP(1);
                this.model.controller.eventList.add(
                        // DestroyedEntityEvent because stone is taken from field
                        new DestroyedEntityEvent(newPosition.toArray(), target.getIDs())
                );
                this.model.controller.eventList.add(
                        // MoveEvent for this Hero
                        new MoveEvent(this.getIDs(), oldPos.toArray(), newPosition.toArray())
                );
                this.model.checkMaxNrInftyStones();
                return true;
        } else {
                return false;   // Can't walk into Thanos
        }
    }

    /**
     * Checks if an opponent was knocked out and increases a counter if that is the case.
     * It also adds the damage to "causedDamagePlayerOne" and "causedDamagePlayerTwo".
     * This is necessary to decide who wins in some cases.
     * @author Luka Stoehr
     * @param target Attacked Attackable object
     * @param damage done damage
     */
    void checkKnockedOpponentHeroAndDamage(Attackable target, int damage){
        if(target instanceof Hero) {
            //Check which team this Hero and the target is in
            boolean thisHeroPlayer1 = false;
            boolean targetPlayer1 = false;
            for (Hero hero : this.model.playerOne.playerTeam) {
                if (hero == this) thisHeroPlayer1 = true;
                if (hero == target) targetPlayer1 = true;
            }
            //If target is opponent and it was knocked out
            if (thisHeroPlayer1 != targetPlayer1) {
                if(thisHeroPlayer1){
                    if(((Hero) target).healthPoints == 0) this.model.knockedOpHeroPlayerOne++;
                    this.model.causedDamagePlayerOne += damage;
                }else{
                    if(((Hero) target).healthPoints == 0) this.model.knockedOpHeroPlayerTwo++;
                    this.model.causedDamagePlayerTwo += damage;
                }
            }
        }
    }


    /**
     * Converts this Hero to a Charackter, which is send over the network during the IngamePhase.
     * @author Luka Stoehr
     * @return Charackter
     */
    public InGameCharacter toIngameCharackter(){
        int[] stones = new int[this.inventory.size()];
        for(int i = 0; i < this.inventory.size(); i++){
            stones[i] = this.inventory.get(i).ID;
        }
        int[] position = {this.getPosition().getX(), this.getPosition().getY()};
        InGameCharacter inGameCharacter = new InGameCharacter(this.name, this.PID, this.ID, this.healthPoints, this.movementPoints,
                this.actionPoints, stones, position);
        return inGameCharacter;
    }

    /**
     * Method to create a ConfigHero object out of the Hero
     * @author Adrian Groeber
     * @return ConfigHero Object that was created out of a Hero
     */
    public ConfigHero toConfigHero(){
        return new ConfigHero(this.ID, this.name, this.maxHealthPoints, this.maxMovementPoints,
                this.maxActionPoints, this.nearAttackDamage, this.farAttackDamage, this.farAttackDistance);
    }

    /**
     * Returns the inventory of this Hero as an int array containing the IDs of the Stones
     * that this hero carries.
     * @author Luka Stoehr
     * @return int[] containing the IDs of the InfinityStones in the inventory
     */
    public int[] inventoryToArray() {
        int[] array = new int[this.inventory.size()];
        for (int i = 0; i < this.inventory.size(); i++) {
            array[i] = this.inventory.get(i).ID;
        }
        return array;
    }

    /**
     *  Indicates whether some other object is "equal to" this one. An Hero is equal to an other Hero if the following
     *  attributes are equal
     *  * PID
     *  * ID
     *  * healthPoints
     *  * movementPoints
     *  * actionPoints
     *  * maxHealthPoints
     *  * nearAttackDamage
     *  * farAttackDamage
     *  * farAttackDistance
     *  * atomized
     *  * name
     *  * inventory
     * @author Matthias Ruf
     *
     * @param o the  object to be compared with a for equality
     * @return whether the object is equal to the calling Hero or not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hero hero = (Hero) o;
        return PID == hero.PID && ID == hero.ID && healthPoints == hero.healthPoints &&
                movementPoints == hero.movementPoints && actionPoints == hero.actionPoints &&
                maxHealthPoints == hero.maxHealthPoints && maxMovementPoints == hero.maxMovementPoints &&
                maxActionPoints == hero.maxActionPoints && nearAttackDamage == hero.nearAttackDamage &&
                farAttackDamage == hero.farAttackDamage && farAttackDistance == hero.farAttackDistance &&
                atomized == hero.atomized && name.equals(hero.name) && Objects.equals(inventory, hero.inventory);
    }
}
