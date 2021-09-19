using System;
using UnityEngine;

namespace MarvelousEditor
{
    public class BackgroundHandler : MonoBehaviour
    {

        public static BackgroundHandler Get()
        {
            return GameObject.Find("Background").GetComponent<BackgroundHandler>();
        }

        [Header("background positions:")]
        public Vector2 configOffset;
        public Vector2 mapOffset;
        public Vector2 characterOffset;

        [Header("Values:")] 
        public float speed;
        public float finishedDistance;

        private Vector2 positionToAnimate;
        private Action callback;
    
    
        void Start()
        {
            RectTransform rt = GetComponent<RectTransform>();
            rt.position = configOffset;
        }
    
        void Update()
        {
            RectTransform rt = GetComponent<RectTransform>();
            rt.position = Vector2.Lerp(rt.position, positionToAnimate, Time.deltaTime * speed);
            if (Vector2.Distance(rt.position, positionToAnimate) < finishedDistance)
            {
                callback();
            }
        }

        public void MoveMap(Vector2 position, Action callback)
        {
            this.callback = callback;
            this.positionToAnimate = position;
        }
    }
}
