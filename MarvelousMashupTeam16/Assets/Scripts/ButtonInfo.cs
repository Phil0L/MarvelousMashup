using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ButtonInfo : MonoBehaviour
{
    
    public string title;
    public string text;
    public Sprite icon;
    public bool usable = true;
    public float spacing;

    public Text textUI;
    public Image imageUI;
    public Text titleUI;
    public RectTransform textHolder;
    public RectTransform iconHolder;

    private void Update()
    {
        if (textUI)
        {
            textUI.text = text;
        }

        if (imageUI)
        {
            imageUI.sprite = icon;
        }

        if (titleUI)
        {
            titleUI.text = title;
            if (!usable)
                titleUI.color = new Color(titleUI.color.r, titleUI.color.g, titleUI.color.b, 0.5f);
            else
                titleUI.color = new Color(titleUI.color.r, titleUI.color.g, titleUI.color.b, 1f);
        }
        
        float left = textHolder.rect.xMin - spacing;
        iconHolder.localPosition = new Vector3(left, 0, 0);
    }

    private void OnDrawGizmos()
    {
        if (textUI)
        {
            textUI.text = text;
        }

        if (imageUI)
        {
            imageUI.sprite = icon;
        }

        if (titleUI)
        {
            titleUI.text = title;
            if (!usable)
                titleUI.color = new Color(titleUI.color.r, titleUI.color.g, titleUI.color.b, 0.5f);
            else
                titleUI.color = new Color(titleUI.color.r, titleUI.color.g, titleUI.color.b, 1f);
        }
        
        float left = textHolder.rect.xMin - spacing;
        iconHolder.localPosition = new Vector3(left, 0, 0);
        
    }

    
}
