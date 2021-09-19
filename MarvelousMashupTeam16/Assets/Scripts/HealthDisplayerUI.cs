using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class HealthDisplayerUI : MonoBehaviour
{
    public Text hp;
    public Text maxHp;

    [Range(0,1)]
    public float factor;

    private void Start()
    {
        SetHealth(0,100);
    }

    private void Update()
    {
        var slider = GetComponent<Slider>();
        if (slider != null) slider.value = factor;

    }

    public void SetHealth(float health, float maxHealth)
    {
        factor = health / maxHealth;
        hp.text = health.ToString();
        maxHp.text = maxHealth.ToString();
    }
}
