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

    public void Start()
    {
        RectTransform rt = GetComponent<RectTransform>();
        rt.sizeDelta = new Vector2(rt.rect.width, selectedTabHeight);
        NavigationItemClicked((int) defaultPage);
    }
    
    public void NavigationItemClicked(int which)
    {
        navigationConfig.sizeDelta = new Vector2(navigationConfig.rect.width, unselectedTabHeight);
        navigationMap.sizeDelta = new Vector2(navigationMap.rect.width, unselectedTabHeight);
        navigationCharacter.sizeDelta = new Vector2(navigationCharacter.rect.width, unselectedTabHeight);
        
        config.gameObject.SetActive(false);
        map.gameObject.SetActive(false);
        character.gameObject.SetActive(false);
        
        switch (which)
        {
            case (int) Pages.CONFIG:
                navigationConfig.sizeDelta = new Vector2(navigationConfig.rect.width, selectedTabHeight);
                config.gameObject.SetActive(true);
                break;
            case (int) Pages.MAP:
                navigationMap.sizeDelta = new Vector2(navigationMap.rect.width, selectedTabHeight);
                map.gameObject.SetActive(true);
                break;
            case (int) Pages.CHARACTER:
                navigationCharacter.sizeDelta = new Vector2(navigationCharacter.rect.width, selectedTabHeight);
                character.gameObject.SetActive(true);
                break;
        }
    }
}
