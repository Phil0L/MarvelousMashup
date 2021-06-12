package logic.gameObjects;

import communication.messages.enums.EntityID;
import communication.messages.events.entity.DestroyedEntityEvent;
import logic.model.Model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * Class that represents the NPC StanLee. This is necessary, because the GamestateEvent from the
 * network standard document contains the field turnOrder which also contains NPCs. An object of
 * this class will be placed in the turnOrder list in the round in which StanLee appears. This class
 * also implements the logic for a turn of StanLee.
 * @author Luka Stoehr
 */
public class StanLee extends Placeable{

        /**
         * Constructor for StanLee. Uses EntityID.NPC and ID=1 as specified in NetworkStandard.
         *
         * @author Luka Stoehr
         */
        public StanLee(Model model) {
            super(EntityID.NPC, 1); // As specified in network standard
            this.model = model;
        }

        /**
         * StanLee cannot move! See class description. However this method must be implemented, because
         * it is declared abstract in class Placeable.
         *
         * @param newPosition where the object is moved to, must be a neighbouring field
         * @return false
         * @author Luka Stoehr
         */
        @Override
        public boolean move(Position newPosition) {
            return false;
        }

        /**
         * This method handles StanLees turn. It only works, if StanLee is in turnOrder and turnCount points to StanLee
         * (this means it has to bee StanLee's turn in this round for this method to work). It also has
         * to be the 7th round (model.round==6, because it starts with 0), because StanLee only appears in round 7.
         * If all conditions are correct, StanLee spawns on a randomly chosen field next to a knocked out
         * character. If there is no knocked out character, StanLee appears next to the one Character with the
         * least amount of HealthPoints. Once StanLee has spawned, he heals all Characters/Heroes within his
         * line of sight and then disappears again.
         * @author Luka Stoehr
         */
        public void stanLeeTurn(){
            if(model.controller.turnOrder.get(model.controller.turnCount) instanceof StanLee && model.round == 6) {
                StanLee stanLee = (StanLee) model.controller.turnOrder.get(model.controller.turnCount);

                LinkedList<Hero> knockedOutCharacter = new LinkedList<>();
                Hero heroWithMinHP = null;
                int minHPofAliveHero = Integer.MAX_VALUE;
                for(Hero heroTeamOne: model.playerOne.playerTeam){
                    if(heroTeamOne.atomized) continue;  // Atomized heroes are not taken into account
                    if(heroTeamOne.getHealthPoints() == 0){
                        knockedOutCharacter.add(heroTeamOne);
                    }else if(heroTeamOne.getHealthPoints() < minHPofAliveHero || minHPofAliveHero == 0){
                        // Find the hero that is alive, but has the minimum amounts of HPs
                        // If minHPofAliveHero==0 no hero has been selected yet (because a hero can't be alive with 0 HP),
                        // so choose the hero in this case as well
                        minHPofAliveHero = heroTeamOne.getHealthPoints();
                        heroWithMinHP = heroTeamOne;
                    }
                }
                for(Hero heroTeamTwo: model.playerTwo.playerTeam){
                    if(heroTeamTwo.atomized) continue; // Atomized heroes are not taken into account
                    if(heroTeamTwo.getHealthPoints() == 0 && !heroTeamTwo.atomized){
                        knockedOutCharacter.add(heroTeamTwo);
                    }else if(heroTeamTwo.getHealthPoints() < minHPofAliveHero || minHPofAliveHero == 0){
                        // Find the hero that is alive, but has the minimum amounts of HPs
                        // If minHPofAliveHero==0 no hero has been selected yet (because a hero can't be alive with 0 HP),
                        // so choose the hero in this case as well
                        minHPofAliveHero = heroTeamTwo.getHealthPoints();
                        heroWithMinHP = heroTeamTwo;
                    }
                }
                Hero chosenHero;
                if(knockedOutCharacter.size() > 0){
                    // There are knocked out characters on the field, choose a random one
                    Collections.shuffle(knockedOutCharacter);
                    chosenHero = knockedOutCharacter.get(0);
                }else{
                    // If no knocked out character exists, choose the one with the minimum amount of HPs
                    chosenHero = heroWithMinHP;
                }

                // Find a free field around the chosen hero to spawn on (randomly)
                Random r = new Random();
                Position currentPos = chosenHero.getPosition();
                Position spawnPos;
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
                    spawnPos = freePosList.get(chosenCandidate);
                    break;  //Placed the stone
                }

                //Spawn Stan Lee
                stanLee.place(spawnPos);

                // Heal characters within line of sight
                for(Hero heroTeamOne: model.playerOne.playerTeam){
                    if((!heroTeamOne.atomized) && model.checkLineOfSight(heroTeamOne.getPosition(), stanLee.getPosition())){
                        heroTeamOne.heal();
                    }
                }
                for(Hero heroTeamTwo: model.playerTwo.playerTeam){
                    if((!heroTeamTwo.atomized) && model.checkLineOfSight(heroTeamTwo.getPosition(), stanLee.getPosition())){
                        heroTeamTwo.heal();
                    }
                }

                // Destroy Stan Lee
                model.controller.eventList.add(
                        new DestroyedEntityEvent(stanLee.getPosAsArray(), stanLee.getIDs())
                );
                model.field[stanLee.getPosition().getX()][stanLee.getPosition().getY()] = null;
            }
        }
}
