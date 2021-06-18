using System;
using UnityEngine;
using UnityEngine.Serialization;

namespace MarvelousEditor.DataClasses
{
    [Serializable]
    public class Character
    {
        [SerializeField] [FormerlySerializedAs("type")] public Characters characterID;
        [SerializeField] public string name;
        [SerializeField] public int HP;
        [SerializeField] public int MP;
        [SerializeField] public int AP;
        [SerializeField] public int meleeDamage;
        [SerializeField] public int rangeCombatDamage;
        [SerializeField] public int rangeCombatReach;

        public Character()
        {
        }
    
        public Character(Characters characterID)
        {
            this.characterID = characterID;
        }

        public Character(Characters characterID, string name, int hp, int mp, int ap, int meleeDamage, int rangeCombatDamage, int rangeCombatReach)
        {
            this.characterID = characterID;
            this.name = name;
            this.HP = hp;
            this.MP = mp;
            this.AP = ap;
            this.meleeDamage = meleeDamage;
            this.rangeCombatDamage = rangeCombatDamage;
            this.rangeCombatReach = rangeCombatReach;
        }

        public Character(CharacterDefaults defaultCharacter)
        {
            this.characterID = defaultCharacter.characterID;
            this.name = defaultCharacter.name;
            this.HP = defaultCharacter.HP;
            this.MP = defaultCharacter.MP;
            this.AP = defaultCharacter.AP;
            this.meleeDamage = defaultCharacter.meleeDamage;
            this.rangeCombatDamage = defaultCharacter.rangeCombatDamage;
            this.rangeCombatReach = defaultCharacter.rangeCombatReach;
        }

        public enum Characters
        {
            Unassigned,
            RocketRacoon,
            Quicksilver,
            Hulk,
            BlackWidow,
            Hawkeye,
            CaptainAmerica,
            Spiderman,
            DrStrange,
            IronMan,
            BlackPanther,
            Thor,
            CaptainMarvel,
            Groot,
            Starlord,
            Gamora,
            AntMan,
            Vision,
            Deadpool,
            Loki,
            SilverSurfer,
            Mantis,
            GhostRider,
            JesicaJones,
            ScarletWitch
        }
    }
}
