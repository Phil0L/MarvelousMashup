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
    public Color colorGrass;
    public Color colorStone;

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
            image.color = colorGrass;
        }

        if (newImage == MapTile.ROCK)
        {
            image.sprite = spriteStone;
            image.color = colorStone;
        }
        this._displayedTile = newImage;
    }
}
