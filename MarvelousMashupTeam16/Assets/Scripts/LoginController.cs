using System.Collections.Generic;
using System.Linq;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.SceneManagement;

public class LoginController : MonoBehaviour
{
    [HideInInspector] public Character[] heroSet;
    [HideInInspector] public string playerTwoName;
    [HideInInspector] public string playerOneName;
    [HideInInspector] public Character[] playerOneHeroes;
    [HideInInspector] public Character[] playerTwoHeroes;
    [HideInInspector] public Party matchConfig;
    [HideInInspector] public Map scenarioConfig;
    [HideInInspector] public int playerType; // 0 = spectator, 1 = player1, 2 = player2

    private List<string> _events;

    public bool active;

    private void Start()
    {
        active = true;
        _events = new List<string>();
        Server.OnConnected(() => MainThread.Execute(OnConnected));
        DontDestroyOnLoad(this);
    }

    private void OnConnected()
    {
        Server.Connection.OnMessage(message =>
        {
            if (active)
                MainThread.Execute(() => OnMessage(message));
        });
        Server.Connection.Send(new HelloServer(
            ServerConnector.GetNameInput().GetPlayerName(),
            ServerConnector.GetNameInput().GetPlayerID().ToString()));
    }

    private void OnMessage(string message)
    {
        ExtractorMessageType obj = JsonConvert.DeserializeObject<ExtractorMessageType>(message);
        if (obj.messageType != MessageType.NULL)
        {
            // Message belongs to the LoginMessages

            switch (obj.messageType)
            {
                case MessageType.HELLO_CLIENT:
                    // unzip incoming message
                    HelloClient helloClient = JsonConvert.DeserializeObject<HelloClient>(message);

                    if (!helloClient.runningGame)
                    {
                        // There is no running game (by the server) with the username and deviceID of the client
                        PlayerReady objPlayerReady = new PlayerReady(null, true, Role.PLAYER);
                        string msgPlayerReady = JsonConvert.SerializeObject(objPlayerReady);
                        Server.Connection.Send(msgPlayerReady);
                        Info.Set()
                            .Text("Waiting for second player to connect...")
                            .NewRandomSprite()
                            .Cooldown(long.MaxValue)
                            .Show();
                    }
                    //TODO: client wants to spectate

                    // else if (role.Equals(Role.SPECTATOR))
                    // {
                    //     PlayerReady obj_playerReady = new PlayerReady(null, true, role);
                    //     string msg_playerReady = JsonConvert.SerializeObject(obj_playerReady);
                    //     Server.Connection.Send(msg_playerReady);
                    // }

                    // TODO: client has a reconnect
                    else
                    {
                        // There is a running game (boolean runningGame was true) and the client wants to reconnect
                        Reconnect objReconnect = new Reconnect(null, true);
                        string msgReconnect = JsonConvert.SerializeObject(objReconnect);
                        Server.Connection.Send(msgReconnect);
                        Info.Set()
                            .Text("Reconnecting...")
                            .NewRandomSprite()
                            .Cooldown(3000)
                            .Show();
                    }

                    break;

                case MessageType.GAME_ASSIGNMENT:
                    GameAssignment gameAssignment = JsonConvert.DeserializeObject<GameAssignment>(message);
                    heroSet = gameAssignment.characterSelection;
                    SceneManager.LoadScene("CharacterChooser");
                    MainThread.ExecuteDelayed(
                        () => { CharacterChooser.Instance.characters = heroSet.ToList(); }, 2);
                    break;

                case MessageType.GENERAL_ASSIGNMENT:
                    GeneralAssignment generalAssignment = JsonConvert.DeserializeObject<GeneralAssignment>(message);
                    Debug.Log($"Game ID: {generalAssignment.gameID}");
                    break;

                case MessageType.CONFIRM_SELECTION:
                    ConfirmSelection confirmSelection = JsonConvert.DeserializeObject<ConfirmSelection>(message);
                    if (!confirmSelection.selectionComplete)
                    {
                        Debug.LogWarning("Selection not confirmed");
                        Info.Set()
                            .Text("Selection has not been Confirmed")
                            .NewRandomSprite()
                            .Cooldown(3000)
                            .Show();
                        CharacterChooser.Instance.ReActivateButton();
                    }
                    else
                    {
                        PopUp.Create()
                            .Title("Waiting...")
                            .Text("Your opponent hasn't chosen his Heroes yet.\nYou have to wait until he has decided.")
                            .Show();
                    }

                    break;

                case MessageType.GAME_STRUCTURE:
                    Info.Clear();
                    PopUp.Clear();

                    // store useful information
                    GameStructure gameStructure = JsonConvert.DeserializeObject<GameStructure>(message);

                    switch (gameStructure.assignment)
                    {
                        case "Spectator":
                            playerType = 0;
                            break;
                        case "PlayerOne":
                            playerType = 1;
                            break;
                        case "PlayerTwo":
                            playerType = 2;
                            break;
                    }

                    playerOneName = gameStructure.playerOneName;
                    playerTwoName = gameStructure.playerTwoName;
                    PartyConfigStore.SetNames(
                        playerType == 1 ? playerOneName : playerTwoName,
                        playerType != 1 ? playerTwoName : playerOneName);

                    playerOneHeroes = gameStructure.playerOneCharacters;
                    playerTwoHeroes = gameStructure.playerTwoCharacters;
                    List<Character> allCharacters = new List<Character>();
                    foreach (var p1H in playerOneHeroes)
                    {
                        p1H.maxHP = p1H.HP;
                        p1H.maxMP = p1H.MP;
                        p1H.maxAP = p1H.AP;
                        p1H.enemy = playerType != 1;
                        allCharacters.Add(p1H);
                    }

                    foreach (var p2H in playerTwoHeroes)
                    {
                        p2H.maxHP = p2H.HP;
                        p2H.maxMP = p2H.MP;
                        p2H.maxAP = p2H.AP;
                        p2H.enemy = playerType != 2;
                        allCharacters.Add(p2H);
                    }

                    CharacterConfigStore.SetCharacters(allCharacters);

                    matchConfig = gameStructure.matchconfig;
                    PartyConfigStore.SetParty(matchConfig);

                    scenarioConfig = gameStructure.scenarioconfig;
                    MapConfigStore.SetMap(scenarioConfig);
                    Debug.Log($"Setting the map to {scenarioConfig}");

                    Server.ServerCaller.GameStarted();
                    GameController controller = gameObject.AddComponent<GameController>();
                    controller.OnActive(() =>
                    {
                        Debug.Log("Login Completed!");
                        active = false;
                        foreach (var events in _events)
                        {
                            controller.OnMessageAgain(events);
                        }
                    });

                    SceneManager.LoadScene("Game");
                    break;

                case MessageType.ERROR:
                    //  not supported
                    Close(-1);
                    break;

                case MessageType.GOODBYE_CLIENT:
                    Info.Set()
                        .Text("Server rejected connection!")
                        .NewRandomSprite()
                        .Cooldown(3000)
                        .Show();
                    //close the connection
                    Close(1);
                    break;

                case MessageType.EVENTS:
                    // not supported
                    if (active)
                    {
                        Debug.LogWarning("Received Event Message while in Login");
                        _events.Add(message);
                    }
                        
                    break;

                case MessageType.REQUESTS:
                    // not supported
                    Debug.LogWarning("Received Request Message while in Login");
                    break;
                default:
                    // not supported
                    Close(-3);
                    break;
            }
        }
    }

    public void Close(int code = 0)
    {
        if (code == 0)
        {
            Debug.LogWarning("Connection closed normally.");
        }
        else
        {
            Debug.LogError("Connection closed with code: " + code + ".");
        }

        active = false;
    }
}