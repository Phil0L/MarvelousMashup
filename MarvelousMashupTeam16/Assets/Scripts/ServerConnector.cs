using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ServerConnector : MonoBehaviour
{
    public IPInput ipInput;
    public NameInput nameInput;
    public int maxConnectTime;
    public bool connected;

    private static ServerConnector instance;

    private void Awake()
    {
        instance = this;
    }

    public void Connect()
    {
        Disconnect(); // Making sure it is disconnected before connecting
        GetComponent<Button>().interactable = false;
        
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
        
        Server.Connection.Connect(hostname, port, () => MainThread.Execute(() => 
        {
            Info.Set()
                .Text("Waiting for answer...")
                .NewRandomSprite()
                .Cooldown(long.MaxValue)
                .Show();
            // MainThread.ExecuteDelayed(() =>
            // {
            //     Server.Connection.Send(new HelloServer(playerName, playerID.ToString()));
            //     bool hasReceivedAnswer = false;
            //     Server.Connection.OnMessage(message => MainThread.ExecuteDelayed(() =>
            //     {
            //         if (hasReceivedAnswer) return;
            Info.Set()
                        .Text("Successfully Connected!")
                        .NewRandomSprite()
                        .DefaultCooldown()
                        .Show();
                    if (Server.Connection == null) return;
                    connected = true;
                    Debug.Log($"Connected successfully to {hostname}:{port}");
                    Connected();
            //      hasReceivedAnswer = true;
            //     }, 3)); // delay until the hello client gets processed in ticks
            // }, 10); // delay between connecting and sending hello server in ticks
        }));
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

    public void Disconnect()
    {
        if (Server.IsAttached())
        {
            Server.Connection.Disconnect();
            Server.Connection.Destroy();
        }
        Server.Connection = null;
    }


    public static NameInput GetNameInput() => instance.nameInput;
}