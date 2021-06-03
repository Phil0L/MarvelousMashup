using System;
using System.IO;
using UnityEditor;
using UnityEngine;

public class SaveWindow
{
    public SaveWindow(string title, string filename, string ending, string data, Action<string> onSuccess)
    {
        var path = EditorUtility.SaveFilePanel(title, "", filename, ending);
        if (path.Length != 0)
        {
            Debug.Log("Selected file path:" + path);
            File.WriteAllText(path, data);
            onSuccess(path);
        }
        else
            Debug.Log("Save has been canceled");
    }
}