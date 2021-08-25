using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ServerConnector : MonoBehaviour
{
    public IPInput ipInput;
    public NameInput nameInput;
    public int maxConnectTime;
    public bool connected;
    
    public void Connect()
    {
        string hostname = ipInput.GetHostName();
        int port = ipInput.GetPort();
        string playerName = nameInput.GetPlayerName();
        int playerID = nameInput.GetPlayerID();

        connected = false;
        Server.Connection = new WebSocketClient();
        StartCoroutine(ConnectionFailed());
        Info.Set()
            .Text("Connecting to the server...")
            .NewRandomSprite()
            .Cooldown(long.MaxValue)
            .Show();
        Server.Connection.Connect(hostname, port, () =>
        {
            Info.Set()
                .Text("Waiting for answer...")
                .NewRandomSprite()
                .Cooldown(long.MaxValue)
                .Show();
            Server.Connection.Send("{\"messageType\":\"HELLO_SERVER\",\"name\":\"" + playerName + "\",\"deviceID\":\"" +
                                   playerID + "\"}");
            Server.Connection.OnMessage(message =>
            {
                Info.Set()
                    .Text("Successfully Connected!")
                    .NewRandomSprite()
                    .DefaultCooldown()
                    .Show();
                if (Server.Connection == null) return;
                connected = true;
                Debug.Log($"Connected successfully to {hostname}:{port}");
                Connected();
                
            });
        });
    }

    private IEnumerator ConnectionFailed()
    {
        yield return new WaitForSeconds(maxConnectTime);
        if (connected) yield break;
        Server.Connection = null;

        Info.Clear();
        PopUp.Create()
            .Title("Failed")
            .Text("Connecting to the server has failed.\nUnable to reach the server.")
            .Bubble(PopUp.Bubbles.Regular)
            .AddAction(new PopUp.PopUpAction("Okay", popup => popup.Destroy()))
            .AddAction(new PopUp.PopUpAction("Retry", popup =>
            {
                popup.Destroy();
                Connect();
            }))
            .Show();
    }

    private void Connected()
    {
        Server.ServerCaller.Connected();
    }
}