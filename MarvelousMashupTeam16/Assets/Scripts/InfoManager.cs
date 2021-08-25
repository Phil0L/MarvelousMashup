using UnityEngine;
using UnityEngine.UI;

public class InfoManager : MonoBehaviour
{
    public static InfoManager manager;
    
    public Info info;

    private void Awake()
    {
        manager = this;
    }
    
    public Info GetInfo()
    {
        return info;
    }
}
