package logic.gameObjects;

/**
 * Interface for all things that can be attacked
 * @author Luka Stoehr
 */
public interface Attackable {

    /**
     * Method is called when an attackable object is attacked.
     * @author Luka Stoehr
     * @param damage How much damage the attack theoretically causes
     * @return How much damage the attack actually caused (can be less than the damage of an attack, for example if a character has less HP than the attack has damage)
     */
    public int attacked(int damage);
}
