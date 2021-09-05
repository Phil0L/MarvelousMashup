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
	    Debug.Log(message);
    }
}