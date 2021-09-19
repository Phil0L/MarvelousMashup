using System;
using System.Collections.Generic;

public class Server
{
    private static List<Action> connected = new List<Action>();
    private static List<Action> started = new List<Action>();
    
    public static WebSocketClient Connection;

    public static bool IsAttached() => Connection != null;

    public static void OnConnected(Action callback) => connected.Add(callback);
    public static void OnGameStarted(Action callback) => started.Add(callback);
    
    
    public class ServerCaller
    {
        public static void Connected() => connected.ForEach(action => action());
        public static void GameStarted() => started.ForEach(action => action());
    }
}

