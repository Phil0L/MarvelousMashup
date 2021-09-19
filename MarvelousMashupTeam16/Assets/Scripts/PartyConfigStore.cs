using System;
using UnityEngine;
using Newtonsoft.Json;

public class PartyConfigStore : MonoBehaviour, IConfigStore
{
    private static Party _party = new Party();
    private static string yourName;
    private static string opponentName;
    private static int playerNumber;
    
    public  Party GetParty() => _party;
    public static Party Party() => _party;
    public static string You() => yourName;
    public static string Opponent() => opponentName;

    public static string Winner(int id)
    {
        if (id == playerNumber) return "You";
        if (playerNumber == 0 && id == 1) return You();
        return Opponent();

    }

    public static int CooldownOf(int stone)
    {
        if (stone == InfinityStone.RED) return _party.realityStoneCD;
        if (stone == InfinityStone.BLUE) return _party.spaceStoneCD;
        if (stone == InfinityStone.GREEN) return _party.timeStoneCD;
        if (stone == InfinityStone.YELLOW) return _party.mindStoneCD;
        if (stone == InfinityStone.ORANGE) return _party.soulStoneCD;
        if (stone == InfinityStone.PURPLE) return _party.powerStoneCD;
        return 0;
    }

    public static void SetParty(Party party) => _party = party;

    public static void SetNames(string you, string other, int id)
    {
        yourName = you;
        opponentName = other;
        playerNumber = id;
    }

    public void LoadJson(string json)
    {
        try
        {
            _party = JsonConvert.DeserializeObject<Party>(json);
        }
        catch (Exception)
        {
            Debug.Log("Load canceled due to errors");
        }
    }
    
    private void Awake()
    {
        if (GetComponent<FileLoader>())
        {
            LoadJson(GetComponent<FileLoader>().GetContent());
        }
    }
}