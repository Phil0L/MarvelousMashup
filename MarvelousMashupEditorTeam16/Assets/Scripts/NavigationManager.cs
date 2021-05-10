using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NavigationManager : MonoBehaviour
{
    public enum Pages
    {
        CONFIG = 1, 
        MAP = 2, 
        CHARACTER = 3
    }
    
    private static readonly int CONFIG = 1;
    private static readonly int MAP = 2;
    private static readonly int CHARACTER = 3;
    
    [Header("Navigation Items:")]
    public RectTransform navigationConfig;
    public RectTransform navigationMap;
    public RectTransform navigationCharacter;

    [Header("Pages:")] 
    public RectTransform config;
    public RectTransform map;
    public RectTransform character;

    [Header("Defaults:")] 
    public Pages defaultPage;

    public int selectedTabHeight = 90;
    public int unselectedTabHeight = 60;

    void Start()
    {
        RectTransform rt = GetComponent<RectTransform>();
        rt.sizeDelta = new Vector2(rt.rect.width, selectedTabHeight);
        NavigationItemClicked((int) defaultPage);
    }
    
    public void NavigationItemClicked(int which)
    {
        navigationConfig.GetComponent<NavigationItemController>().AnimateHeight(NavigationItemController.Heights.UNSELECTED);
        navigationMap.GetComponent<NavigationItemController>().AnimateHeight(NavigationItemController.Heights.UNSELECTED);
        navigationCharacter.GetComponent<NavigationItemController>().AnimateHeight(NavigationItemController.Heights.UNSELECTED);
        
        config.gameObject.SetActive(false);
        map.gameObject.SetActive(false);
        character.gameObject.SetActive(false);
        
        switch (which)
        {
            case (int) Pages.CONFIG:
                navigationConfig.GetComponent<NavigationItemController>().AnimateHeight(NavigationItemController.Heights.SELECTED);
                config.gameObject.SetActive(true);
                break;
            case (int) Pages.MAP:
                navigationMap.GetComponent<NavigationItemController>().AnimateHeight(NavigationItemController.Heights.SELECTED);
                map.gameObject.SetActive(true);
                break;
            case (int) Pages.CHARACTER:
                navigationCharacter.GetComponent<NavigationItemController>().AnimateHeight(NavigationItemController.Heights.SELECTED);
                character.gameObject.SetActive(true);
                break;
        }
    }
}
