using System;
using UnityEngine;

public class Game : MonoBehaviour
{
    private static Game INSTANCE;
    private GameState state;

    public GroundLoader GroundLoader;
    public CharacterLoader CharacterLoader;
    public CameraController CameraController;
    public Pathfinding Pathfinding;

    public static GameState State()
    {
        return INSTANCE.state;
    }
    
    public static Game Controller()
    {
        return INSTANCE;
    }

    public static bool IsInstantiated()
    {
        return INSTANCE != null;
    }

    private void Start()
    {
        INSTANCE = this;
        state = new GameState();
        GroundLoader.LoadMap(state);
        Pathfinding.PathFind(new Vector2Int(1, 1), new Vector2Int(5,2));
    }
}