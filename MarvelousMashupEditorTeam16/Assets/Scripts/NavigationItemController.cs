using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;

public class NavigationItemController : MonoBehaviour, IPointerEnterHandler, IPointerExitHandler
{
    
    public enum Heights
    {
        UNSELECTED = 60,
        HOVERED = 70,
        SELECTED = 90
    }

    private float height;
    private float animateGoal;
    private int state;

    public float movementSpeed;

    void Start()
    {
        RectTransform rt = GetComponent<RectTransform>();
        height = rt.rect.height;
    }

    void Update()
    {
        RectTransform rt = GetComponent<RectTransform>();
        rt.sizeDelta = Vector2.Lerp(rt.rect.size, new Vector2(rt.rect.width, animateGoal), Time.deltaTime * movementSpeed);
    }
    

    private void SetHeight(float height)
    {
        this.height = height;
        this.animateGoal = height;
        RectTransform rt = GetComponent<RectTransform>();
        rt.sizeDelta = new Vector2(rt.rect.width, this.height);
    }
    
    public void SetHeight(Heights height)
    {
        SetHeight((float) height);
        if (height == Heights.SELECTED)
        {
            this.state = 1;
        }
        if (height == Heights.UNSELECTED)
        {
            this.state = 0;
        }
    }

    private void AnimateHeight(float height)
    {
        this.animateGoal = height;
    }
    
    public void AnimateHeight(Heights height)
    {
        AnimateHeight((float) height);
        if (height == Heights.SELECTED)
        {
            this.state = 1;
        }
        if (height == Heights.UNSELECTED)
        {
            this.state = 0;
        }
    }

    public void OnPointerEnter(PointerEventData eventData)
    {
        if (state == 0)
        {
            AnimateHeight(Heights.HOVERED);
        }
        
    }
    
    public void OnPointerExit(PointerEventData eventData)
    {
        if (state == 0)
        {
            AnimateHeight(Heights.UNSELECTED);
        }
        
    }
}
