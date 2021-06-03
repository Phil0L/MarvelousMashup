using System;
using System.IO;
using UnityEditor;
using UnityEngine;

public class LoadWindow
{
    public LoadWindow(string title, string ending, Action<string, string> onSuccess)
    {
        var path = EditorUtility.OpenFilePanel(title, "", ending);
        if (path.Length != 0)
        {
            Debug.Log("Selected file path:" + path);
            var content = File.ReadAllText(path);
            onSuccess(path, content);
        }
        else
            Debug.Log("Load has been canceled");
    }
}