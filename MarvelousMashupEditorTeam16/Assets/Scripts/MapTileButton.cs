using System;
using UnityEngine;
using UnityEngine.EventSystems;

public class MapTileButton : MonoBehaviour, IPointerDownHandler, IPointerEnterHandler, IPointerExitHandler
{
    public Action listener;
    private bool blockExit = false;

    public void SetListener(Action a)
    {
        this.listener = a;
    }

    public bool HasListener()
    {
        return listener != null;
    }

    public void OnPointerDown(PointerEventData data)
    {
        listener();
        blockExit = true;
    }

    public void OnPointerEnter(PointerEventData eventData)
    {
        if (Input.GetMouseButton(0))
        {
            listener();
            blockExit = true;
        }
    }

    public void OnPointerExit(PointerEventData eventData)
    {
        if (Input.GetMouseButton(0) && !blockExit)
        {
            listener();
        }
    }
}
