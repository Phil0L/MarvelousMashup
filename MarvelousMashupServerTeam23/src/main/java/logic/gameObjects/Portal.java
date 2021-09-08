package logic.gameObjects;

import communication.messages.enums.EntityID;
import communication.messages.events.portal.TeleportedEvent;
import communication.messages.objects.Entities;
import logic.model.Model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * Class that represents a Portal on the field.
 * @author Luka Stoehr
 */
public class Portal extends Placeable{
    /**
     * Counts how many portals exist in total. This is used to set the IDs of the Portals uniquely when they are created.
     */
    public static int portalCount;

    public Portal(Model model){
        super(EntityID.Portals, portalCount);
        portalCount++;
        this.model = model;
    }

    /**
     * Teleports a Hero using this Portal. For that another portal is chosen as target randomly,
     * and the hero is teleported to a random free target field around the target portal.
     * @param hero Hero that will be teleported
     * @return True, if successful
     * @author Luka Stoehr
     */
    public boolean teleport(Hero hero){
        LinkedList<Portal> portalList = new LinkedList<>();
        for(int x = 0; x < model.field.length; x++){
            for(int y = 0; y < model.field[0].length; y++){
                if(model.field[x][y] instanceof Portal && model.field[x][y]!=this){
                    portalList.add((Portal) model.field[x][y]);
                }
            }
        }
        if(portalList.size() == 0) return false;        //There is not another portal
        Collections.shuffle(portalList);

        Random r = new Random();
        //Choose portal to teleport to
        Portal targetPortal = portalList.get(0);
        Position currentPos = targetPortal.getPosition();
        //Find a free field near the target Portal
        while(true) {
            //Check which neighbouring fields are free
            LinkedList<Position> freePosList = new LinkedList<>();
            Position neighbour1 = new Position(currentPos.getX(), currentPos.getY() + 1);
            if (model.isFree(neighbour1)) freePosList.add(neighbour1);
            Position neighbour2 = new Position(currentPos.getX() + 1, currentPos.getY() + 1);
            if (model.isFree(neighbour2)) freePosList.add(neighbour2);
            Position neighbour3 = new Position(currentPos.getX() + 1, currentPos.getY());
            if (model.isFree(neighbour3)) freePosList.add(neighbour3);
            Position neighbour4 = new Position(currentPos.getX() + 1, currentPos.getY() - 1);
            if (model.isFree(neighbour4)) freePosList.add(neighbour4);
            Position neighbour5 = new Position(currentPos.getX(), currentPos.getY() - 1);
            if (model.isFree(neighbour5)) freePosList.add(neighbour5);
            Position neighbour6 = new Position(currentPos.getX() - 1, currentPos.getY() - 1);
            if (model.isFree(neighbour6)) freePosList.add(neighbour6);
            Position neighbour7 = new Position(currentPos.getX() - 1, currentPos.getY());
            if (model.isFree(neighbour7)) freePosList.add(neighbour7);
            Position neighbour8 = new Position(currentPos.getX() - 1, currentPos.getY() + 1);
            if (model.isFree(neighbour8)) freePosList.add(neighbour8);

            //All neighbouring fields are occupied
            if (freePosList.size() == 0) {
                //Check which of the neighbouring fields are valid and choose on as new currentPosition
                LinkedList<Position> onFieldList = new LinkedList<>();
                if (model.onField(neighbour1)) onFieldList.add(neighbour1);
                if (model.onField(neighbour2)) onFieldList.add(neighbour2);
                if (model.onField(neighbour3)) onFieldList.add(neighbour3);
                if (model.onField(neighbour4)) onFieldList.add(neighbour4);
                if (model.onField(neighbour5)) onFieldList.add(neighbour5);
                if (model.onField(neighbour6)) onFieldList.add(neighbour6);
                if (model.onField(neighbour7)) onFieldList.add(neighbour7);
                if (model.onField(neighbour8)) onFieldList.add(neighbour8);
                int chosenCandidate = r.nextInt(onFieldList.size());
                currentPos = onFieldList.get(chosenCandidate);
                continue;  //Try again starting from new currentPos
            }
            //Choose on of the free neighbouring fields randomly
            int chosenCandidate = r.nextInt(freePosList.size());
            // Remove hero from old field
            Position oldPos = hero.getPosition();
            model.field[oldPos.getX()][oldPos.getY()] = null;
            Position newPos = freePosList.remove(chosenCandidate);
            model.field[newPos.getX()][newPos.getY()] = hero;
            model.controller.eventList.add(
                    new TeleportedEvent(hero.getIDs(), this.getPosAsArray(), newPos.toArray(), this.getIDs(), targetPortal.getIDs())
            );
            break;  //Placed the hero
        }
        return true;
    }


    /**
     * A Portal cannot be moved. This is only implemented, because it is declared abstract
     * in Placeable.
     * @param newPosition where the object is moved to, must be a neighbouring field
     * @return False
     * @author Luka Stoehr
     */
    @Override
    public boolean move(Position newPosition) {
        return false;
    }

    /**
     * Translates this Placeable object to an Entities object that is needed for some
     * network messages.
     *
     * @return Entity
     * @author Luka Stoehr
     */
    @Override
    public Entities toEntity() {
        return new communication.messages.objects.Portal(this.ID, this.getPosAsArray());
    }
}
