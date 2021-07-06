using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class PopUpManager : MonoBehaviour
{
    public static PopUpManager manager;
    
    public PopUp prefab;
    public Button actionPrefab;

    private void Awake()
    {
        manager = this;
    }
    
    public PopUp GetNewPopUp()
    {
        PopUp popUp = Instantiate(prefab, transform);
        popUp.name = "Empty Popup";
        popUp.gameObject.SetActive(false);
        return popUp;
    }
}
