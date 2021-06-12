package logic.gameObjects;

import communication.messages.enums.EntityID;
import communication.messages.events.entity.DestroyedEntityEvent;
import logic.model.Model;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Class that represents the NPC Goose. This is necessary, because the GamestateEvent from the
 * network standard document contains the field turnOrder which also contains NPCs. Therefore an
 * object of this class is placed in the List turnOrder in the controller everytime Goose shows
 * up in a round. This class also implements the logic for when it is Gosses turn.
 * @author Luka Stoehr
 */
public class Goose extends Placeable{
    /**
     * Constructor for Goose. Uses EntityID.NPC and ID=0 as specified in NetworkStandard.
     *
     * @author Luka Stoehr
     */
    public Goose(Model model) {
        super(EntityID.NPC, 0); // As specified in network standard
        this.model = model;
    }

    /**
     * Goose cannot move! See class description. However this method must be implemented, because
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
     * This method handles all actions of Goose, if it is called when its Gooses turn. The method only
     * works correctly if Goose is in turnOrder and turnCount points to Goose and in the first 6 rounds.
     * In that case, Goose spawns on a random free field, disappears again and leaves one of the
     * InfinityStones on the field that has not been placed yet.
     * @author Luka Stoehr
     */
    public void gooseTurn(){
        if(model.controller.turnOrder.get(model.controller.turnCount) instanceof Goose &&
                model.round >= 0 && model.round <=5) {
            Goose goose = (Goose) model.controller.turnOrder.get(model.controller.turnCount);

            //Find all free fields
            LinkedList<Position> freeFields = new LinkedList<>();
            for (int x = 0; x < model.field.length; x++) {
                for (int y = 0; y < model.field[0].length; y++) {
                    if (model.isFree(new Position(x, y))) freeFields.add(new Position(x, y));
                }
            }
            //Shuffle
            Collections.shuffle(freeFields);
            Position goosePos = freeFields.get(0);
            goose.place(goosePos);

            //Find all InfinityStones that are not on the field yet
            LinkedList<InfinityStone> unplacedStones = new LinkedList<>();
            if(model.spaceStone.getPosition() == null) unplacedStones.add(model.spaceStone);
            if(model.mindStone.getPosition() == null) unplacedStones.add(model.mindStone);
            if(model.realityStone.getPosition() == null) unplacedStones.add(model.realityStone);
            if(model.powerStone.getPosition() == null) unplacedStones.add(model.powerStone);
            if(model.timeStone.getPosition() == null) unplacedStones.add(model.timeStone);
            if(model.soulStone.getPosition() == null) unplacedStones.add(model.soulStone);
            if(unplacedStones.size() == 0) return;  //All stones are already placed
            //Shuffle
            Collections.shuffle(unplacedStones);
            //Take Goose from field again
            model.controller.eventList.add(
                    new DestroyedEntityEvent(goose.getPosAsArray(), goose.getIDs())
            );
            model.field[goosePos.getX()][goosePos.getY()] = null;

            //Place randomly chosen stone
            unplacedStones.get(0).place(goosePos);
        }
    }
}
