using System;
using System.Collections;
using System.Net.WebSockets;
using System.Text;
using System.Threading;
using UnityEditor;
using UnityEngine;
using UnityEngine.PlayerLoop;

public class TestConnection : MonoBehaviour
{
    public string url;

    public ClientWebSocket cws;

    private string message = "";
    private Coroutine receiveRoutine;

    public void Connect()
    {
        StartCoroutine(OpenConnection());
    }

    public void Disconnect()
    {
        StartCoroutine(CloseConnection());
    }

    public void Send(string data)
    {
        StartCoroutine(SendData(data));
    }

    public string Receive()
    {
        StartCoroutine(ReceiveData());
        return message;
    }

    private IEnumerator OpenConnection()
    {
        cws = new ClientWebSocket();
        yield return cws.ConnectAsync(new UriBuilder("ws", url.Split(':')[0], int.Parse(url.Split(':')[1])).Uri,
            CancellationToken.None);
    }

    private IEnumerator CloseConnection()
    {
        if (cws != null && (cws.State == WebSocketState.Open || cws.State == WebSocketState.Connecting))
        {
            yield return cws.CloseAsync(WebSocketCloseStatus.NormalClosure, "Connection manually closed",
                CancellationToken.None);
        }

        cws = null;
    }

    private IEnumerator SendData(string data)
    {
        if (cws != null && cws.State == WebSocketState.Open)
        {
            byte[] sendBytes = Encoding.UTF8.GetBytes(data);
            var sendBuffer = new ArraySegment<byte>(sendBytes);
            yield return cws.SendAsync(sendBuffer, WebSocketMessageType.Text, true, CancellationToken.None);
        }
    }

    private IEnumerator ReceiveData()
    {
        Read();
        yield return null;
    }

    private async void Read()
    {
        while (cws != null && cws.State == WebSocketState.Open)
        {
            Debug.Log("Reading...");
            var sendBuffer = new ArraySegment<byte>(new byte[10000]);
            await cws.ReceiveAsync(sendBuffer, CancellationToken.None);
            string received = Encoding.UTF8.GetString(sendBuffer.Array);
            Debug.Log(received);
        }
    }
}

#if UNITY_EDITOR
[CustomEditor(typeof(TestConnection))]
public class GridDisplayerEditor : Editor
{
    private string enteredText = "";
    private string state;
    private string lastMaessageReveived = "No message received yet";

    public override void OnInspectorGUI()
    {
        DrawDefaultInspector();

        GUILayout.Space(20);

        TestConnection myScript = (TestConnection) target;

        GUILayout.BeginHorizontal();
        if (GUILayout.Button("Connect"))
        {
            myScript.Connect();
            myScript.Receive();
        }

        if (GUILayout.Button("Disconnect"))
        {
            myScript.Disconnect();
        }

        if (GUILayout.Button("Refresh State"))
        {
            state = myScript.cws == null ? "Not connected" : myScript.cws.State.ToString();
        }

        GUILayout.EndHorizontal();

        state = myScript.cws == null ? "Not connected" : myScript.cws.State.ToString();
        GUILayout.Label(state);

        GUILayout.Space(20);

        GUILayout.Label("Send:");
        enteredText = GUILayout.TextArea(enteredText, GUILayout.Height(50));
        if (GUILayout.Button("Send"))
        {
            myScript.Send(enteredText);
            enteredText = "";
        }

        GUILayout.Space(20);

        GUILayout.BeginHorizontal();
        if (GUILayout.Button("Hello Server"))
        {
            myScript.Send("{\"messageType\":\"HELLO_SERVER\",\"name\":\"UnityClient\",\"deviceID\":\"123\"}");
        }

        if (GUILayout.Button("Empty Button"))
        {
        }

        GUILayout.EndHorizontal();
        GUILayout.BeginHorizontal();
        if (GUILayout.Button("Empty Button"))
        {
        }

        if (GUILayout.Button("Empty Button"))
        {
        }

        GUILayout.EndHorizontal();

        GUILayout.Space(20);

        GUILayout.Label("Received:");
        string message = myScript.Receive();
        if (!string.IsNullOrEmpty(message))
            lastMaessageReveived = message;
        GUILayout.TextArea(lastMaessageReveived, GUILayout.Height(50));
    }
}
#endif