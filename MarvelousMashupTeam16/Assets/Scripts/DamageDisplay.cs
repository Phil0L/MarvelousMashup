using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class DamageDisplay : MonoBehaviour
{
    public TextMeshPro text;
    public float speed = 1;
    public float alphaChange = 0.005f;
    public int alphaStart = 100;
    private int count = 0;

    public void SetValue(int value)
    {
        text.text = value.ToString();
    }
    
    void Update()
    {
        transform.position += Vector3.up * speed;
        if (count >= alphaStart)
            text.color = new Color(text.color.r, text.color.g, text.color.b, text.color.a - alphaChange);
        if (text.color.a == 0)
            Destroy(gameObject);
        count++;
    }
}
