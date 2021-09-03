using Newtonsoft.Json;
using UnityEngine;

public class GameController : MonoBehaviour
{
    private Deserializer _deserializer;

    private void Awake()
    {
        _deserializer = new Deserializer();
        Server.OnGameStarted(() => Server.Connection.OnMessage(OnMessage));
    }

    private void OnMessage(string message)
    {
        // check if event
        ExtractorMessageType obj = JsonConvert.DeserializeObject<ExtractorMessageType>(message);
        if (obj.messageType == MessageType.EVENTS)
        {
            // split in seperate messages
            ExtractorMessageStructure exMessageStruct = JsonConvert.DeserializeObject<ExtractorMessageStructure>(message);
            MessageStructure messageStruct = exMessageStruct.toMessageStructure();


            foreach (Message msg in messageStruct.messages)
            {
                //processes the messages in the messageStructure
                _deserializer.PreExtractor(msg);
            }
        }
        
    }
}