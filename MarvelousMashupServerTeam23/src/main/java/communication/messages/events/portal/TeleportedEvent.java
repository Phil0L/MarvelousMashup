package communication.messages.events.portal;

import communication.messages.IDs;
import communication.messages.Message;
import communication.messages.enums.EventType;
import communication.messages.objects.Entities;

public class TeleportedEvent extends Message {
    /**
     * The Entity that is teleported.
     */
    public IDs teleportedEntity;
    /**
     * Origin field of the teleportation, which is the field of the originPortal.
     */
    public int[] originField;
    /**
     * Target field of the teleportation, which is a neighbouring field of the targetPortal.
     */
    public int[] targetField;
    /**
     * The portal the hero stepped into.
     */
    public IDs originPortal;
    /**
     * The target portal that was chosen randomly from all portals except the originPortal.
     */
    public IDs targetPortal;


    /**
     * Constructor for a TeleportedEvent
     * @param teleportedEntity IDs object of the teleported entity
     * @param originField Origin field of teleportation, which is field of originPortal as int[]
     * @param targetField Target field of teleportation, which is a neighbouring field of targetPortal as int[]
     * @param originPortal IDs object of origin portal
     * @param targetPortal IDs object of target portal
     * @author Luka Stoehr
     */
    public TeleportedEvent(IDs teleportedEntity, int[] originField, int[] targetField,
                           IDs originPortal, IDs targetPortal) {
        super(EventType.TeleportedEvent);
        this.teleportedEntity = teleportedEntity;
        this.originField = originField;
        this.targetField = targetField;
        this.originPortal = originPortal;
        this.targetPortal = targetPortal;
    }
}
