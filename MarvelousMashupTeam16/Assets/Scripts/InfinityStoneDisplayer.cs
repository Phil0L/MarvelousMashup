using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InfinityStoneDisplayer : MonoBehaviour
{
    public Transform red;
    public Transform orange;
    public Transform yellow;
    public Transform green;
    public Transform blue;
    public Transform violet;
    public Transform container;
    public float offset;
    public float spacing;

    private List<InfinityStone> _stones = new List<InfinityStone>();

    public void SetStones(List<InfinityStone> stones)
    {
        _stones = stones;
    }

    private void SetStones()
    {
        red.gameObject.SetActive(false);
        orange.gameObject.SetActive(false);
        yellow.gameObject.SetActive(false);
        green.gameObject.SetActive(false);
        blue.gameObject.SetActive(false);
        violet.gameObject.SetActive(false);
        
        int index = 0;
        foreach (var stone in _stones)
        {
            Transform tf = red;
            switch (stone.stone)
            {
                case InfinityStone.RED:
                    tf = red;
                    break;
                case InfinityStone.ORANGE:
                    tf = orange;
                    break;
                case InfinityStone.YELLOW:
                    tf = yellow;
                    break;
                case InfinityStone.GREEN:
                    tf = green;
                    break;
                case InfinityStone.BLUE:
                    tf = blue;
                    break;
                case InfinityStone.PURPLE:
                    tf = violet;
                    break;
            }

            tf.gameObject.SetActive(true);
            var position = tf.position;

            position = new Vector3(offset + container.position.x -
                                   container.GetComponent<Renderer>().bounds.size.x / 2 +
                                   tf.GetComponent<Renderer>().bounds.size.x / 2 +
                                   (tf.GetComponent<Renderer>().bounds.size.x + spacing) * index, 
                position.y,
                position.z);
            tf.position = position;

            index++;
        }
    }
    
    private void Update()
    {
        SetStones();
    }

    private void OnDrawGizmos()
    {
        SetStones();
    }
}