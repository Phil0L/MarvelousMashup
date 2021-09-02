using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Serialization;

[Serializable]
public class Character : IFieldContent
{
    [SerializeField] public IDs characterID;
    [SerializeField] public string name;
    [SerializeField] public int HP;
    [SerializeField] public int MP;
    [SerializeField] public int AP;
    [SerializeField] public int meleeDamage;
    [SerializeField] public int rangeCombatDamage;
    [SerializeField] public int rangeCombatReach;
    [NonSerialized] public bool enemy;
    [NonSerialized] public int maxHP;
    [NonSerialized] public int maxMP;
    [NonSerialized] public int maxAP;
    [NonSerialized] public List<InfinityStone> infinityStones;

    public Character()
    {
        infinityStones = new List<InfinityStone>();
    }
    
    public Character(IDs characterID)
    {
        this.characterID = characterID;
        infinityStones = new List<InfinityStone>();
    }

    public Character(IDs characterID, string name, int hp, int mp, int ap, int meleeDamage, int rangeCombatDamage, int rangeCombatReach)
    {
        this.characterID = characterID;
        this.name = name;
        this.HP = hp;
        this.MP = mp;
        this.AP = ap;
        this.maxHP = hp;
        this.maxMP = mp;
        this.maxAP = ap;
        this.meleeDamage = meleeDamage;
        this.rangeCombatDamage = rangeCombatDamage;
        this.rangeCombatReach = rangeCombatReach;
        infinityStones = new List<InfinityStone>();
    }

	public void changeAP(int amount) {
		this.AP = this.AP - amount;
	}

}
