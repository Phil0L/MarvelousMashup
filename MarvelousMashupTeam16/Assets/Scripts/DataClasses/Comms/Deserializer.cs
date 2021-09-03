using Newtonsoft.Json;
using UnityEngine;

/*
 * This class desirializes the messages for general gameplay coming from the network stack. 
 */

public class Deserializer
{
    public void PreExtractor(Message message) {
	    if (message is EntityEvent ev)
		{
			ev.Execute();
		}
		Debug.Log(message);
    }
}