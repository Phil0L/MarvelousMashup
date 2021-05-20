using UnityEngine;
using UnityEngine.UI;

public class MapTileInfo : MonoBehaviour
{
    [Header("Data:")]
    public MapTile tile;
    private MapTile _displayedTile;
    public Vector2 position;
    
    [Header("References:")] 
    public MapTile defaultTile;
    public Image image;
    public Sprite spriteGrass;
    public Sprite spriteStone;

    public void SetTile(MapTile tile)
    {
        this.tile = tile;
    }

    void Start()
    {
        if (tile == MapTile.UNDEFINED)
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

    void UpdateImage(MapTile newImage)
    {
        if (newImage == MapTile.GRASS)
        {
            image.sprite = spriteGrass;
        }

        if (newImage == MapTile.STONE)
        {
            image.sprite = spriteStone;
        }
        this._displayedTile = newImage;
    }
}
