package parameter;

import logic.gameObjects.Hero;
import logic.model.Model;

/**
 * This class is used to parse the JSON character config which contains an array
 * of these characters.
 * @author Luka Stoehr
 */
public class ConfigHero {

    public int characterID;
    public String name;
    public int HP;
    public int MP;
    public int AP;
    public int meleeDamage;
    public int rangeCombatDamage;
    public int rangeCombatReach;

    /**
     *
     * the constructor of the CharacterLogin-Class. Creates a CharacterLogin
     * -MessageObject.
     *
     * @author Sarah Engele
     * @author Matthias Ruf
     *
     * @param characterID the unique ID of a Character (e.g. Hulks ID is 3)
     * @param name the name of the Character (e.g. Hulk)
     * @param HP the amount of Health Points (HP) the Character has at the beginning
     * @param MP the amount of Movement Points (MP) the Character has at the beginning
     * @param AP the amount of Action Points (AP) the Character has at the beginning
     * @param meleeDamage the amount of meleeDamage the Character
     * @param rangeCombatDamage the amount of rangeCombatDamage the Character
     * @param rangeCombatReach the amount of rangeCombatReach the Character
     */
    public ConfigHero(int characterID, String name, int HP, int MP, int AP, int meleeDamage, int rangeCombatDamage, int rangeCombatReach) {
        this.characterID = characterID;
        this.name = name;
        this.HP = HP;
        this.MP = MP;
        this.AP = AP;
        this.meleeDamage = meleeDamage;
        this.rangeCombatDamage = rangeCombatDamage;
        this.rangeCombatReach = rangeCombatReach;
    }

    /**
     *
     * transforms a ConfigHero object (used in combination with network or configuration stuff) to a Hero object (used
     * server internal to compute game changes)
     *
     * @author Matthias Ruf
     *
     * @param PID Personal ID,identified the Player which the Heros is connected to
     * @param ID ID of the Hero Object (used in Ingame-messages)
     * @param model The Model which is related to the Hero object
     * @return a Hero object created by the attributes of the ConfigHero object and the additional information given by
     * the parameters
     */
    public Hero toHero(int PID, int ID, Model model){
        return new Hero(PID,ID,this.HP,this.MP,this.AP,this.name,this.meleeDamage,this.rangeCombatDamage,this.rangeCombatReach, model);
    }

    /**
     * Indicates whether some other object is "equal to" this one. An Hero is equal to an other Hero if the following
     *  attributs are equal
     *  * HP
     *  * AP
     *  * MP
     *  * name
     *  * meleeDamage
     *  * rangeCombatDamage
     *  * rangeCombatReach
     *
     * @author Matthias Ruf
     *
     * @param o the  object to be compared with a for equality
     * @return whether the object is equal to the calling ConfigHero or not.
     *
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigHero that = (ConfigHero) o;
        return HP == that.HP && MP == that.MP && AP == that.AP && meleeDamage == that.meleeDamage &&
                rangeCombatDamage == that.rangeCombatDamage && rangeCombatReach == that.rangeCombatReach &&
                name.equals(that.name);
    }


}

