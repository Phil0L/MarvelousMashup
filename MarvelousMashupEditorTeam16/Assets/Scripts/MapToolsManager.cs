using UnityEngine;
using UnityEngine.UI;

public class MapToolsManager : MonoBehaviour
{
    
    public enum Tool
    {
        Switch,
        Stone,
        Grass,
        Portal
    }

    [Header("Tools:")]
    public Image toolSwitch;
    public Image toolGrass;
    public Image toolStone;
    public Image toolPortal;

    [Header("Colors:")]
    public Color unselectedColor;
    public Color selectedColor;

    [Header("Data:")]
    public Tool defaultTool;
    public Tool selectedTool;

    void Start()
    {
        switch (defaultTool)
        {
            case Tool.Switch:
                OnSwitchToolSelected();
                break;
            case Tool.Grass:
                OnGrassToolSelected();
                break;
            case Tool.Stone:
                OnStoneToolSelected();
                break;
            case Tool.Portal:
                OnPortalToolSelected();
                break;
        }
    }

    public void OnSwitchToolSelected()
    {
        toolSwitch.color = selectedColor;
        toolGrass.color = unselectedColor;
        toolStone.color = unselectedColor;
        toolPortal.color = unselectedColor;
        selectedTool = Tool.Switch;
        Debug.Log("Switch tool selected!");
    }
    
    public void OnGrassToolSelected()
    {
        toolSwitch.color = unselectedColor;
        toolGrass.color = selectedColor;
        toolStone.color = unselectedColor;
        toolPortal.color = unselectedColor;
        selectedTool = Tool.Grass;
        Debug.Log("Grass tool selected!");
    }
    
    public void OnStoneToolSelected()
    {
        toolSwitch.color = unselectedColor;
        toolGrass.color = unselectedColor;
        toolStone.color = selectedColor;
        toolPortal.color = unselectedColor;
        selectedTool = Tool.Stone;
        Debug.Log("Stone tool selected!");
    }
    
    public void OnPortalToolSelected()
    {
        toolSwitch.color = unselectedColor;
        toolGrass.color = unselectedColor;
        toolStone.color = unselectedColor;
        toolPortal.color = selectedColor;
        selectedTool = Tool.Portal;
        Debug.Log("Portal tool selected!");
    }
}
