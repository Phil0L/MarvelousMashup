using UnityEngine;
using UnityEngine.UI;

namespace MarvelousEditor
{
    public class MapTileInfo : MonoBehaviour
    {
        [Header("Data:")]
        public DataClasses.MapTile tile;
        private DataClasses.MapTile _displayedTile;
        public Vector2 position;
    
        [Header("References:")] 
        public DataClasses.MapTile defaultTile;
        public Image image;
        public Sprite spriteGrass;
        public Sprite spriteStone;
        public Sprite spritePortal;
        public Color colorGrass;
        public Color colorStone;
        public Color colorPortal;

        public void SetTile(DataClasses.MapTile tile)
        {
            this.tile = tile;
        }

        void Start()
        {
            if (tile == DataClasses.MapTile.UNDEFINED)
            {
                this.tile = this.defaultTile;
                UpdateImage(defaultTile);   
            }
        }

        void Update()
        {
            if (_displayedTile != tile)
            {
                UpdateImage(tile);
            }
        }

        void UpdateImage(DataClasses.MapTile newImage)
        {
            if (newImage == DataClasses.MapTile.GRASS)
            {
                image.sprite = spriteGrass;
                image.color = colorGrass;
            }

            if (newImage == DataClasses.MapTile.ROCK)
            {
                image.sprite = spriteStone;
                image.color = colorStone;
            }

            if (newImage == DataClasses.MapTile.PORTAL)
            {
                image.sprite = spritePortal;
                image.color = colorPortal;
            }
            this._displayedTile = newImage;
        }
    }
}
