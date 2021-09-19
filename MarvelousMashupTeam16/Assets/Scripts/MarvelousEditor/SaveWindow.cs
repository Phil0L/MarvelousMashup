using System;
using System.IO;
using UnityEditor;
using UnityEngine;
using SFB;

namespace MarvelousEditor
{
    public class SaveWindow
    {
        public SaveWindow(string title, string filename, string ending, string data, Action<string> onSuccess)
        {
            string path;
#if UNITY_EDITOR
            path = EditorUtility.SaveFilePanel(title, "", filename, ending);
#else
            path = StandaloneFileBrowser.SaveFilePanel(title, "", filename, ending);
#endif
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
}