using UnityEngine;
using UnityEngine.UI;
using Random = System.Random;

public class NameInput : MonoBehaviour
{
    public InputField nameInput;
    private static int _playerID;
    public string defaultName;
    private Random _random;

    private void Awake()
    {
        if (_random == null) _random = new Random();
    }

    private void Start()
    {
        _playerID = _random.Next(9999);
        defaultName = "Player #" + _playerID;
        if (nameInput.placeholder is Text placeholder) placeholder.text = defaultName;
    }

    public string GetPlayerName()
    {
        if (nameInput.text.Equals("")) return defaultName;
        return nameInput.text;
    }

    public int GetPlayerID()
    {
        return _playerID;
    }
}
