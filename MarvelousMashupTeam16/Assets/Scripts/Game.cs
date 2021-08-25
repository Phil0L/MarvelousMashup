using UnityEngine;

public class Game : MonoBehaviour
{
    private static Game INSTANCE;
    private GameState state;

    public GroundLoader GroundLoader;
    public CharacterLoader CharacterLoader;
    public InfinityStoneLoader InfinityStoneLoader;
    public CameraController CameraController;
    public Pathfinding Pathfinding;
    public PathDisplayer PathDisplayer;
    public AttackDisplayer AttackDisplayer;
    public InfinityStoneActionDisplayer InfinityStoneActionDisplayer;
    public InfinityStonePassDisplayer InfinityStonePassDisplayer;
    public ButtonInfoManager ButtonInfoManager;
    public ArrowDispenser ArrowDispenser;
    public CurrentTurnInfo CurrentTurnInfo;

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

    private void Awake()
    {
        INSTANCE = this;
        state = new GameState();
        GroundLoader.LoadMap();
    }

}