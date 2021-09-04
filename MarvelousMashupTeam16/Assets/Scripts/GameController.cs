using System;
using System.Collections.Generic;
using System.Linq;
using Newtonsoft.Json;
using UnityEngine;

public class GameController : MonoBehaviour
{
    private Deserializer _deserializer;
    private Action _loginCallback;
    private List<string> _receivedMessages;

    private bool _isAwake;

    private void Awake()
    {
        _deserializer = new Deserializer();
        _receivedMessages = new List<string>();
        if (Server.IsAttached())
        {
            Server.Connection.OnMessage(message => MainThread.Execute(() => OnMessage(message)));
            _loginCallback?.Invoke();
            _isAwake = true;
        }
        else Debug.LogWarning("There is no server attached! Login phase has probably not finished correctly.");
    }

    private void OnMessage(string message)
    {
        // check if event
        _receivedMessages.Add(message);

        ExtractorMessageType obj = JsonConvert.DeserializeObject<ExtractorMessageType>(message);
        if (obj.messageType == MessageType.EVENTS)
        {
            // split in separate messages
            ExtractorMessageStructure exMessageStruct =
                JsonConvert.DeserializeObject<ExtractorMessageStructure>(message);
            MessageStructure messageStruct = exMessageStruct.toMessageStructure();


            foreach (Message msg in messageStruct.messages.ToArray())
            {
                //processes the messages in the messageStructure
                _deserializer.PreExtractor(msg);
            }
        }
    }

    public void OnMessageAgain(string message)
    {
        if (!_receivedMessages.Contains(message))
            OnMessage(message);
    }

    public void OnActive(Action callback)
    {
        if (_isAwake) callback();
        else _loginCallback = callback;
    }
}