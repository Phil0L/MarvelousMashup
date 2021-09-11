using Newtonsoft.Json;
using UnityEngine;

/*
 * This class deserializes the messages for general gameplay coming from the network stack. 
 */
public class Deserializer
{
    public void PreExtractor(Message message) {
	    if (message is EntityEvent ev) ev.Execute();
	    if (message is GameEvent gv) gv.Execute();
	    if (message is CharacterEvent cv) cv.Execute();
	    if (message is GamestateEvent gs) gs.Execute();
	    // Debug.Log(message);
    }
}