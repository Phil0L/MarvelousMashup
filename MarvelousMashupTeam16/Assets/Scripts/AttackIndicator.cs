using UnityEngine;
using UnityEngine.Tilemaps;

public class AttackIndicator : MonoBehaviour
{
    private static AttackIndicator _ai;

    public ParticleSystem prefab;
    

    private void Awake()
    {
        _ai = this;
    }

    private void SummonAt(Vector2Int position)
    {
        if (Game.IsInstantiated())
        {
            var attic = Instantiate(prefab, transform);
            Tilemap tm = Game.Controller().GroundLoader.tilemap;
            attic.transform.position = tm.GetCellCenterWorld(new Vector3Int(position.x, position.y, 0));
        }
    }

    public static void Summon(Vector2Int position)
    {
        if (_ai) _ai.SummonAt(position);
    }
}
