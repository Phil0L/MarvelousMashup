using UnityEngine;

public class CharacterController : MonoBehaviour
{
    public Character character;

    public HealthDisplayer healthDisplayer;
    public InfinityStoneDisplayer infinityStoneDisplayer;

    private void Update()
    {
        if (character != null)
        {
            healthDisplayer.SetPercentage((character.HP / Mathf.Max(character.maxHP, 0.01f)) * 100);
            healthDisplayer.SetColor(character.enemy ? Color.red : Color.green);
            infinityStoneDisplayer.SetStones(character.infinityStones);
        }
    }
}
