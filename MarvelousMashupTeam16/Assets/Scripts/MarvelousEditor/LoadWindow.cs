using System;
using System.IO;
using UnityEditor;
using UnityEngine;
using SFB;

namespace MarvelousEditor
{
    public class LoadWindow
    {
        public LoadWindow(string title, string ending, Action<string, string> onSuccess)
        {
            string path;
#if UNITY_EDITOR
            path = EditorUtility.OpenFilePanel(title, "", ending);
#else
            path = StandaloneFileBrowser.OpenFilePanel(title, "", ending, false)[0];
#endif
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
}