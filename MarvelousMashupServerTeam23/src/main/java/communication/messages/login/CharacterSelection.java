package communication.messages.login;


import communication.messages.BasicMessage;
import communication.messages.enums.MessageType;

/**
 * CharacterSelection is used by the Client to respond to the servers GameAssignment message. The job of this message-class is to inform the server
 * which characters the player has chosen.
 *
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 */
public class CharacterSelection extends BasicMessage {

    /**
     * true if the player has chosen the character, false if not. The size of this array is 12.
     */
    public Boolean[] characters;

    /**
     *
     * the constructor of the CharacterSelection-Class. It creates a CharacterSelection-MessageObject with the given parameters
     *
     * @author Matthias Ruf
     * @author Sarah Engele
     *
     * @param optionals optional field
     * @param characters true if the player has chosen the character, false if not. The size of this array is 12.
     *
     */
    public CharacterSelection(String optionals,Boolean[] characters){
        super( MessageType.CHARACTER_SELECTION,optionals);
        this.characters = characters;
    }

}
