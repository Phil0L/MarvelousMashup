using System;
using UnityEngine;

public class HealthDisplayer : MonoBehaviour
{
    public Transform healthBar;
    public Transform container;
    [Range(0,100)]
    public float percentage = 100;
    public float offset;

    public void SetPercentage(float p)
    {
        percentage = Mathf.Max(0, Mathf.Min(100, p));
    }

    private void SetHealth()
    {
        var localScale = healthBar.localScale;
        localScale = new Vector3(percentage / 100, localScale.y, localScale.z);
        healthBar.localScale = localScale;

        var position = healthBar.position;
        
        position = new Vector3(offset + container.position.x - container.GetComponent<Renderer>().bounds.size.x / 2 + healthBar.GetComponent<Renderer>().bounds.size.x / 2, position.y, position.z);
        healthBar.position = position;

    }

    private float GetWidth(Transform tf)
    {
        var p1 = tf.TransformPoint(0, 0, 0);
        var p2 = tf.TransformPoint(1, 1, 0);
        return p2.x - p1.x;
    }

    public void SetColor(Color color)
    {
        healthBar.GetComponent<SpriteRenderer>().color = color;
    }

    private void Update()
    {
        SetHealth();
    }

    private void OnDrawGizmos()
    {
        SetHealth();
    }
}
