using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Random = System.Random;

public class HeroCycler : MonoBehaviour
{
    public Image leaving;
    public Image coming;

    public List<Sprite> sprites;

    public float movingDistance;
    public int leaveTime;
    public float speed;

    private int frame;
    private float velocity;

    private void Update()
    {
        if (frame == 0)
        {
            coming.transform.position = new Vector2(transform.position.x + movingDistance, transform.position.y);
            Random rnd = new Random();
            int r = rnd.Next(sprites.Count);
            coming.sprite = sprites[r];
            leaving.color = new Color(1, 1, 1, 0);
        }

        if (frame < leaveTime)
        {
            coming.transform.position = Vector3.Lerp(coming.transform.position, transform.position, speed / 100);
            coming.color = new Color(coming.color.r, coming.color.g, coming.color.g,
                1 - (Vector3.Distance(coming.transform.position, transform.position) / movingDistance));
        }

        if (frame == leaveTime)
        {
            leaving.transform.position = transform.position;
            coming.transform.position = transform.position;
            coming.color = new Color(1, 1, 1, 0);
            leaving.sprite = coming.sprite;
            velocity = 1;
        }

        if (frame > leaveTime)
        {
            leaving.transform.position = new Vector2(leaving.transform.position.x - velocity, transform.position.y);
            velocity += speed / 1000;
            leaving.color = new Color(leaving.color.r, leaving.color.g, leaving.color.g,
                1 - (Vector3.Distance(leaving.transform.position, transform.position) / movingDistance));
            if (Vector2.Distance(leaving.transform.position, transform.position) > movingDistance)
            {
                frame = -1;
            }
        }

        frame++;
    }
}