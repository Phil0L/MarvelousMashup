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
        if (!Server.IsAttached()) return;
        WebSocketClient wsc = Server.Connection;
        if (ur is CharacterMoveRequest cmr)
        {
            wsc.Send(new MoveRequest(cmr.character.GetID(), cmr.previousPosition.ToArray(), cmr.newPosition.ToArray()));
        }

        if (ur is LongRangeAttackRequest lrar)
        {
            wsc.Send(new RangedAttackRequest(lrar.character.GetID(), lrar.attacked.GetIDCasted(), 
                lrar.characterPosition.ToArray(), lrar.attackPosition.ToArray(), lrar.character.rangeCombatDamage));
        }

        if (ur is CloseRangeAttackRequest crar)
        {
            wsc.Send(new RangedAttackRequest(crar.character.GetID(), crar.attacked.GetIDCasted(),
                crar.characterPosition.ToArray(), crar.attackPosition.ToArray(), crar.character.rangeCombatDamage));
        }

        if (ur is EndTurnRequest etr)
        {
            wsc.Send(new EndRoundRequest());
        }

        if (ur is StonePassRequest spr)
        {
            wsc.Send(new ExchangeInfinityStoneRequest(spr.character.GetID(), spr.receivingCharacter.GetID(), 
                spr.fromPosition.ToArray(), spr.position.ToArray(), spr.infinityStone.GetID()));
        }

        if (ur is SpaceStoneRequest ssr)
        {
            wsc.Send(new UseInfinityStoneRequest(ssr.character.GetID(), 
                Game.State().FindHeroPosition(ssr.character.characterID).ToArray(), 
                ssr.teleportPosition.ToArray(), ssr.infinityStone.GetID()));
        }

        if (ur is PowerStoneRequest psr)
        {
            wsc.Send(new UseInfinityStoneRequest(psr.character.GetID(),
                Game.State().FindHeroPosition(psr.character.characterID).ToArray(), psr.attackPosition.ToArray(),
                psr.infinityStone.GetID()));
        }

        if (ur is MindStoneRequest msr)
        {
            wsc.Send(new UseInfinityStoneRequest(msr.character.GetID(),
                Game.State().FindHeroPosition(msr.character.characterID).ToArray(), msr.attackPosition.ToArray(),
                msr.infinityStone.GetID()));
        }

        if (ur is TimeStoneRequest tsr)
        {
            wsc.Send(new UseInfinityStoneRequest(tsr.character.GetID(),
                Game.State().FindHeroPosition(tsr.character.characterID).ToArray(),
                Game.State().FindHeroPosition(tsr.character.characterID).ToArray(),
                tsr.infinityStone.GetID()));
        }

        if (ur is SoulStoneRequest sosr)
        {
            wsc.Send(new UseInfinityStoneRequest(sosr.character.GetID(),
                Game.State().FindHeroPosition(sosr.character.characterID).ToArray(),
                Game.State().FindHeroPosition(sosr.revived.characterID).ToArray(),
                sosr.infinityStone.GetID()));
        }

        if (ur is RealityStoneRequest rsr)
        {
            wsc.Send(new UseInfinityStoneRequest(rsr.character.GetID(),
                Game.State().FindHeroPosition(rsr.character.characterID).ToArray(), rsr.stonePosition.ToArray(),
                rsr.infinityStone.GetID()));
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