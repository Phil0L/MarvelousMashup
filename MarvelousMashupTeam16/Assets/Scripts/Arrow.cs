using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Arrow : MonoBehaviour
{
    public Vector3 target;
    public float angleOffset;
    public float particleOffset;
    public float speed;
    private Action callback;
    
    private void Update()
    {
        SetRotation();
        Move();
    }

    private void OnDrawGizmos()
    {
        SetRotation();
    }

    private void SetRotation()
    {
        var position = transform.position;
        var direction = (target - position).normalized;
        var angle = Vector3.Angle(direction, Vector3.up);
        var rotation = transform.rotation;
        if (direction.x > 0)
            rotation = Quaternion.Euler(rotation.x, rotation.y, -angle + angleOffset);
        else
            rotation = Quaternion.Euler(rotation.x, rotation.y, angle + angleOffset);
        transform.rotation = rotation;
        
        var shapeModule = transform.GetComponent<ParticleSystem>().shape;
        if (direction.x > 0)
            shapeModule.rotation = new Vector3(-angle/100 + particleOffset, shapeModule.rotation.y, shapeModule.rotation.z);
        else
            shapeModule.rotation = new Vector3(angle/100 + particleOffset, shapeModule.rotation.y, shapeModule.rotation.z);
        
        
        
    }

    private void Move()
    {
        transform.position = Vector3.MoveTowards(transform.position, target, speed * Time.deltaTime);

        if (Vector3.Distance(transform.position, target) < 0.1)
        {
            if (callback != null) callback();
            Destroy(gameObject);
        }
    }

    public void Hit(Action callback) => this.callback = callback;
}
