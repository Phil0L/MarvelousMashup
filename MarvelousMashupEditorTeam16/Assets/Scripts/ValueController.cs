using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;
using UnityEngine.UI;

public class ValueController : MonoBehaviour
{

    [Header("Colors:")]
    public Color increaseColor = Color.white;
    public Color decreaseColor = Color.white;
    public Color valueColor = new Color(0,0,0,120);
    public Color color = new Color(0,0,0,0);

    [Header("Values:")] 
    public int minValue = 1;
    public int maxValue = 100;
    public int defaultValue = 10;
    public int step = 1;
    
    [SerializeField]
    public UnityEvent<int> onValueChange;

    [Header("References:")]
    public Image decreaseIcon;
    public Image increaseIcon;
    public InputField inputField;
    public Image increaseImage;
    public Image decreaseImage;
    public Image inputImage;
    public Image backgroundImage;

    private int value;




    public void IncreaseButtonClicked()
    {
        SetValue(value + step);
    }

    public void DecreaseButtonClicked()
    {
        SetValue(value - step);
    }

    public void ValueEntered(string value)
    {
        SetValue(int.Parse(value));
    }

    public void SetValue(int newValue)
    {
        if (newValue <= minValue)
        {
            this.value = minValue;
            inputField.text = minValue.ToString();
            decreaseIcon.color = Color.gray;
            return;
        }

        if (newValue >= maxValue)
        {
            this.value = maxValue;
            inputField.text = maxValue.ToString();
            increaseIcon.color = Color.gray;
            return;
        }

        increaseIcon.color = Color.black;
        decreaseIcon.color = Color.black;
        this.value = newValue;
        inputField.text = newValue.ToString();
        
        onValueChange.Invoke(this.value);
    }

    public int GetValue()
    {
        return this.value;
    }

    void Start()
    {
        SetValue(defaultValue);
    }
    
    void Update()
    {
        increaseImage.color = increaseColor;
        decreaseImage.color = decreaseColor;
        inputImage.color = valueColor;
        backgroundImage.color = color;
    }

    private void OnDrawGizmos()
    {
        SetValue(defaultValue);
        increaseImage.color = increaseColor;
        decreaseImage.color = decreaseColor;
        inputImage.color = valueColor;
        backgroundImage.color = color;
        RectTransform rt = GetComponent<RectTransform>();
        increaseImage.GetComponent<RectTransform>().sizeDelta = new Vector2(rt.rect.width * 0.3f, 0);
        decreaseImage.GetComponent<RectTransform>().sizeDelta = new Vector2(rt.rect.width * 0.3f, 0);
        inputImage.GetComponent<RectTransform>().sizeDelta = new Vector2(rt.rect.width * 0.3f, 0);
    }
}
