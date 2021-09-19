using System;
using System.Collections.Generic;
using System.Linq;
using Newtonsoft.Json;
using UnityEngine;

public class GameController : MonoBehaviour
{
    private static GameController _instance;
    private Deserializer _deserializer;
    private Action _loginCallback;
    private List<string> _receivedMessages;

    private bool _isAwake;

    private void Start()
    {
        _instance = this;
        _deserializer = new Deserializer();
        _receivedMessages = new List<string>();
        if (Server.IsAttached())
        {
            Server.Connection.OnMessage(message => MainThread.Execute(() => OnMessage(message)));
            _loginCallback?.Invoke();
            _isAwake = true;
        }
        else Debug.LogWarning("There is no server attached! Login phase has probably not finished correctly.");

        Game.State().Subscribe(OnUserInteraction);
    }

    private void OnMessage(string message)
    {
        if (_receivedMessages.Contains(message)) Debug.LogWarning("This message has already been processed!");
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

    public void OnUserInteraction(UserRequest ur)
    {
        Debug.Log($"Client issued {ur.GetType().Name}");
        if (!Server.IsAttached()) return;
        WebSocketClient wsc = Server.Connection;
        switch (ur)
        {
            case CharacterMoveRequest cmr:
                List<MoveRequest> moves = new List<MoveRequest>();
                for (int i = 0; i < cmr.path.Count - 1; i++)
                {
                    int fromIndex = i;
                    int toIndex = i + 1;
                    moves.Add(new MoveRequest(cmr.character.GetID(), 
                        cmr.path[fromIndex].ToArray(), cmr.path[toIndex].ToArray()));
                }
                wsc.Send(moves.ToArray().ToStructure(MessageType.REQUESTS));
                break;
            case LongRangeAttackRequest lrar:
                wsc.Send(new RangedAttackRequest(lrar.character.GetID(), lrar.attacked.GetIDCasted(),
                        lrar.characterPosition.ToArray(), lrar.attackPosition.ToArray(),
                        lrar.character.rangeCombatDamage)
                    .ToStructure(MessageType.REQUESTS));
                break;
            case CloseRangeAttackRequest crar:
                wsc.Send(new MeleeAttackRequest(crar.character.GetID(), crar.attacked.GetIDCasted(),
                        crar.characterPosition.ToArray(), crar.attackPosition.ToArray(),
                        crar.character.rangeCombatDamage)
                    .ToStructure(MessageType.REQUESTS));
                break;
            case EndTurnRequest _:
                wsc.Send(new EndRoundRequest().ToStructure(MessageType.REQUESTS));
                break;
            case StonePassRequest spr:
                wsc.Send(new ExchangeInfinityStoneRequest(spr.character.GetID(), spr.receivingCharacter.GetID(),
                        spr.fromPosition.ToArray(), spr.position.ToArray(), spr.infinityStone.GetID())
                    .ToStructure(MessageType.REQUESTS));
                break;
            case SpaceStoneRequest ssr:
                wsc.Send(new UseInfinityStoneRequest(ssr.character.GetID(),
                    Game.State().FindHeroPosition(ssr.character.characterID).ToArray(),
                    ssr.teleportPosition.ToArray(), ssr.infinityStone.GetID()).ToStructure(MessageType.REQUESTS));
                break;
            case PowerStoneRequest psr:
                wsc.Send(new UseInfinityStoneRequest(psr.character.GetID(),
                    Game.State().FindHeroPosition(psr.character.characterID).ToArray(), psr.attackPosition.ToArray(),
                    psr.infinityStone.GetID()).ToStructure(MessageType.REQUESTS));
                break;
            case MindStoneRequest msr:
                wsc.Send(new UseInfinityStoneRequest(msr.character.GetID(),
                    Game.State().FindHeroPosition(msr.character.characterID).ToArray(), msr.attackPosition.ToArray(),
                    msr.infinityStone.GetID()).ToStructure(MessageType.REQUESTS));
                break;
            case TimeStoneRequest tsr:
                wsc.Send(new UseInfinityStoneRequest(tsr.character.GetID(),
                    Game.State().FindHeroPosition(tsr.character.characterID).ToArray(),
                    Game.State().FindHeroPosition(tsr.character.characterID).ToArray(),
                    tsr.infinityStone.GetID()).ToStructure(MessageType.REQUESTS));
                break;
            case SoulStoneRequest sosr:
                wsc.Send(new UseInfinityStoneRequest(sosr.character.GetID(),
                    Game.State().FindHeroPosition(sosr.character.characterID).ToArray(),
                    Game.State().FindHeroPosition(sosr.revived.characterID).ToArray(),
                    sosr.infinityStone.GetID()).ToStructure(MessageType.REQUESTS));
                break;
            case RealityStoneRequest rsr:
                wsc.Send(new UseInfinityStoneRequest(rsr.character.GetID(),
                    Game.State().FindHeroPosition(rsr.character.characterID).ToArray(), rsr.stonePosition.ToArray(),
                    rsr.infinityStone.GetID()).ToStructure(MessageType.REQUESTS));
                break;
        }
    }

    public static void OnMessageAgain(string message)
    {
        if (_instance == null) return;
        _instance.OnMessage(message);
    }

    public void OnActive(Action callback)
    {
        if (_isAwake) callback();
        else _loginCallback = callback;
    }
}

public static class Structure
{
    public static MessageStructure ToStructure(this Message message, MessageType messageType)
    {
        return new MessageStructure(messageType, new[] {message}, null, null);
    }
    
    public static MessageStructure ToStructure(this Message[] message, MessageType messageType)
    {
        return new MessageStructure(messageType, message, null, null);
    }
}