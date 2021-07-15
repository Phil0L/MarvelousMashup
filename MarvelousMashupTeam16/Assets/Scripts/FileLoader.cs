using System;
using UnityEngine;

public class FileLoader : MonoBehaviour
{
    public TextAsset file;

    public string GetContent()
    {
        Debug.LogWarning("Usage of static File Loader to load a config file");
        return file.text;
    }
}