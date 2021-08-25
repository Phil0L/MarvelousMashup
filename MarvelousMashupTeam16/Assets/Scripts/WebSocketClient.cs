using System;
using System.Collections.Generic;
using System.IO;
using System.Net.WebSockets;
using System.Text;
using System.Threading;
using Newtonsoft.Json;
using UnityEngine;
using Object = System.Object;

public class WebSocketClient
{
    public ClientWebSocket socket;
    private List<Action<string>> listener;

    public WebSocketClient()
    {
        listener = new List<Action<string>>();
    }
    
    public async void Connect(string host, int port)
    {
        socket = new ClientWebSocket();
        await socket.ConnectAsync(new UriBuilder("ws", host, port).Uri,
            CancellationToken.None);
    }

    public void Connect(string host, int port, Action callback)
    {
        var thread = new Thread(() =>
        {
            Connect(host, port);
            while (socket.State == WebSocketState.Connecting) {}
            if (socket.State == WebSocketState.Open)
            {
                callback();
                Read();
            }
        })
        {
            Name = "Client connect"
        };
        thread.Start();
    }
    
    public async void Disconnect()
    {
        if (socket != null && (socket.State == WebSocketState.Open || socket.State == WebSocketState.Connecting))
        {
            await socket.CloseAsync(WebSocketCloseStatus.NormalClosure, "Connection manually closed",
                CancellationToken.None);
        }
    }

    public void Disconnect(Action callback)
    {
        if (socket != null && (socket.State == WebSocketState.Open || socket.State == WebSocketState.Connecting))
        {
            var thread = new Thread(() =>
            {
                Disconnect();
                callback();
            })
            {
                Name = "Client disconnect"
            };
            thread.Start();
        }
    }

    public void Send(string data)
    {
        if (socket is {State: WebSocketState.Open})
        {
            byte[] sendBytes = Encoding.UTF8.GetBytes(data);
            var sendBuffer = new ArraySegment<byte>(sendBytes);
            socket.SendAsync(sendBuffer, WebSocketMessageType.Text, true, CancellationToken.None);
        }
    }

    public void Send(Object data)
    {
        Send(JsonConvert.SerializeObject(data));
    }
    
    private void Read()
    {
        var thread = new Thread(() =>
        {
            ReadInternal();
        })
        {
            Name = "Client message receiver"
        };
        thread.Start();
    }

    private async void ReadInternal()
    {
        while (socket is {State: WebSocketState.Open})
        {
            byte[] buffer = new byte[1024];
            WebSocketReceiveResult result = await socket.ReceiveAsync(new ArraySegment<byte>(buffer), CancellationToken.None);

            if (result.MessageType == WebSocketMessageType.Close)
            {
                Debug.LogWarning("Connection closed from server");
                return;
            }

            using (MemoryStream stream = new MemoryStream())
            {
                stream.Write(buffer,0, result.Count);
                while(!result.EndOfMessage)
                {
                    result = await socket.ReceiveAsync(new ArraySegment<byte>(buffer), CancellationToken.None);
                    stream.Write(buffer, 0, result.Count);
                }

                stream.Seek(0, SeekOrigin.Begin);
                using (StreamReader reader = new StreamReader(stream, Encoding.UTF8))
                {
                    string message = reader.ReadToEnd();
                    Debug.Log("Incoming message: '" + message + "'");
                    foreach (var lis in listener)
                    {
                        lis(message);
                    }
                }
            }
        }
    }

    public void OnMessage(Action<string> callback)
    {
        listener.Add(callback);
    }
    
}
