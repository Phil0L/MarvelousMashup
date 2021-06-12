package logic.Controller;

import communication.messages.Message;

import java.util.ArrayList;

/**
 * The handleRequest() methods in the Controller return an object of this type.
 * It contains information on whether a Request was successful and a list of all
 * the events that occurred because of this Request.
 * @author Luka Stoehr
 */
public class HandleReturn {
    /**
     * Indicates whether the Request that triggered this response was successful or not.
     */
    public boolean requestSuccessful;
    /**
     * List of all Events that have occurred due/since the request and have to be
     * sent to the Clients.
     */
    public ArrayList<Message> eventList;

    /**
     * Constructor for HandleReturn class
     * @author Luka Stoehr
     * @param requestSuccessful Whether the request was successful
     * @param eventList List of all events that occurred in the meantime
     */
    public HandleReturn(boolean requestSuccessful, ArrayList<Message> eventList){
        this.requestSuccessful = requestSuccessful;
        this.eventList = eventList;
    }
}
