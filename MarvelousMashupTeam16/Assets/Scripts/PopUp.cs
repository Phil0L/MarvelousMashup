using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;
using Object = UnityEngine.Object;

public class PopUp : MonoBehaviour
{

    public static PopUp Create()
    {
        PopUpManager popUpManager = PopUpManager.manager;
        PopUp popUp = popUpManager.GetNewPopUp();
        return popUp;
    }

    public static void Clear()
    {
        PopUpManager popUpManager = PopUpManager.manager;
        popUpManager.DestroyAll();
    }
    
    public enum Bubbles
    {
        Regular,
        Rectangle,
        Cloud,
        Round,
        Star,
        Middle,
        Cloud2,
        Flipped,
        UpsideDown,
        Lightning,
        CloudRound,
        Regular2,
        Regular3,
        LightningUpsideDown,
        RoundRight,
        RoundFlipped,
        Action,
        Spikes,
        Square,
        Cloud3
    }

    private Bubbles bubble = Bubbles.Regular;
    private string title = "";
    private string content = "";
    private Color color = Color.white;
    private List<PopUpAction> actions = new List<PopUpAction>(4);

    [Header("refs")]
    public Image bubbleImage;
    public TextMeshProUGUI titleText;
    public TextMeshProUGUI contentText;
    public Transform actionList;
    
    [Header("static")]
    public List<Sprite> bubbles;
    public List<Vector2> bubbleOffsets;
    
    public class PopUpAction
    {
        public string text;
        public Color color = Color.white;
        public Action<PopUp> callback;

        public PopUpAction(string text, Action<PopUp> callback, Color color)
        {
            this.text = text;
            this.color = color;
            this.callback = callback;
        }
        
        public PopUpAction(string text, Action<PopUp> callback)
        {
            this.text = text;
            this.color = Color.white;
            this.callback = callback;
        }
    }
    
    // Creator functions //

    public PopUp Title(string title)
    {
        this.title = title;
        this.titleText.text = title;
        gameObject.name = title;
        return this;
    }

    public PopUp Tint(Color color)
    {
        this.color = color;
        this.bubbleImage.color = color;
        return this;
    }

    public PopUp Text(string text)
    {
        this.content = text;
        this.contentText.text = text;
        return this;
    }

    public PopUp Bubble(Bubbles bubble)
    {
        this.bubble = bubble;
        bubbleImage.sprite = bubbles[(int) bubble];
        bubbleImage.transform.localPosition += new Vector3(bubbleOffsets[(int) bubble].x, bubbleOffsets[(int) bubble].y, 0);
        foreach (Transform text in bubbleImage.transform)
            text.localPosition -= new Vector3(bubbleOffsets[(int) bubble].x, bubbleOffsets[(int) bubble].y, 0);
        return this;
    }

    public PopUp AddAction(PopUpAction action)
    {
        this.actions.Add(action);
        Button button = Instantiate(PopUpManager.manager.actionPrefab, actionList);
        button.onClick.AddListener(() => action.callback(this));
        Image image = button.GetComponent<Image>();
        image.color = action.color;
        TextMeshProUGUI text = button.GetComponentInChildren<TextMeshProUGUI>();
        text.text = action.text;
        return this;
    }

    public void Show()
    {
        gameObject.SetActive(true);
        
    }

    public void Hide()
    {
        gameObject.SetActive(false);
    }

    public void Destroy()
    {
        Destroy(gameObject);
    }

}
