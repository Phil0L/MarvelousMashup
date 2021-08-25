using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Random = System.Random;

public class Info : MonoBehaviour
{
    public static Info Set()
    {
        InfoManager infoManager = InfoManager.manager;
        Info info = infoManager.GetInfo();
        return info;
    }

    public static void Clear()
    {
        InfoManager infoManager = InfoManager.manager;
        Info info = infoManager.GetInfo();
        info.Hide();
    }
    
    private string content = "";
    private Sprite sprite;
    private long cooldown;

    [Header("refs")] 
    public Text text;
    public Image image;

    [Header("static")] 
    public List<Sprite> sprites;
    public long defaultCooldown;
    
    // Creator functions //
    
    public Info Text(string text)
    {
        this.content = text;
        this.text.text = content;
        return this;
    }

    public Info NewRandomSprite()
    {
        Random rnd = new Random();
        int r = rnd.Next(sprites.Count);
        sprite = sprites[r];
        image.sprite = sprite;
        return this;
    }

    public Info DefaultCooldown()
    {
        cooldown = defaultCooldown;
        return this;
    }

    public Info Cooldown(long cooldown)
    {
        this.cooldown = cooldown;
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

    private void Update()
    {
        if (cooldown >= 0) cooldown--;
        if (cooldown <= 0) Hide();
    }
}
