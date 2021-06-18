using UnityEngine;
using UnityEngine.UI;

namespace MarvelousEditor
{
    public class GridClickHandler : MonoBehaviour
    {
    
        [Header("References:")]
        public GridLayoutGroup gridLayoutGroup;
        public MapStore mapStore;
        public MapModifierController controller;
    
        // Update is called once per frame
        void Update()
        {
            foreach (Transform transform in gridLayoutGroup.transform)
            {
                MapTileButton b = transform.GetComponent<MapTileButton>();
                if (!b.HasListener())
                {
                    MapTileInfo mti = transform.GetComponent<MapTileInfo>();
                    b.SetListener(() => controller.TileClicked((int) mti.position.x, (int) mti.position.y));
                }
            }
        }
    }
}
