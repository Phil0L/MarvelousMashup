package communication.messages.enums;

/**
 * @author Matthias Ruf
 * @author Sarah Engele
 *
 * lists all MessageTypes, which are defined by the network standard document
 *
 */
public enum MessageType {
    HELLO_CLIENT,
    HELLO_SERVER,
    RECONNECT,
    PLAYER_READY,
    GAME_ASSIGNMENT,
    GENERAL_ASSIGNMENT,
    CHARACTER_SELECTION,
    CONFIRM_SELECTION,
    GAME_STRUCTURE,
    GOODBYE_CLIENT,
    ERROR,
    EVENTS,
    REQUESTS
}
