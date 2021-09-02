using Newtonsoft.Json;

/*
 * This class desirializes the messages for general gameplay coming from the network stack. 
 */

public class Desirializer
{
    public Message PreExtractor(string message) {
		Message msg = JsonConvert.DeserializeObject<ExtractorMessage>(message).toMessage();
		if (msg is EntityEvent)
		{
			EntityEvent ev = (EntityEvent) msg;
			ev.Execute();
		}

		return msg;
    }
}