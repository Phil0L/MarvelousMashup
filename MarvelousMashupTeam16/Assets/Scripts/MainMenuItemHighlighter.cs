using System;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class MainMenuItemHighlighter : MonoBehaviour, IPointerEnterHandler, IPointerExitHandler
{
    public Text text;
    private Vector3 _goalPosition;
    public float indent;
    public float speed;
    private bool _init;
    public Color highlightColor;
    public Color normalColor;

    private void Start()
    {
        GetComponent<Image>().color = normalColor;
    }


    public void OnPointerEnter(PointerEventData eventData)
    {
        if (!_init)
        {
            _goalPosition = text.transform.localPosition;
            _init = true;
        }
        _goalPosition += new Vector3(indent, 0, 0);
        GetComponent<Image>().color = highlightColor;
    }

    public void OnPointerExit(PointerEventData eventData)
    {
        if (!_init)
        {
            _goalPosition = text.transform.localPosition;
            _init = true;
        }
        _goalPosition -= new Vector3(indent, 0, 0);
        GetComponent<Image>().color = normalColor;
    }

    private void LateUpdate()
    {
        if (_init) text.transform.localPosition = Vector3.Lerp(text.transform.localPosition, _goalPosition, Time.deltaTime * speed);
        

    }
}
