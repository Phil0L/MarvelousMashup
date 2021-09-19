using UnityEngine;

namespace MarvelousEditor
{
    public class PartyController:MonoBehaviour
    {

        // Controler Class
        public PartyStore store;
    
        // All Ballancing
        public ValueController spaceStoneCD;
        public ValueController realityStoneCD;
        public ValueController mindStoneCD;
        public ValueController powerStoneCD;
        public ValueController timeStoneCD;
        public ValueController soulStoneCD;
        public ValueController mindStoneDMG;
    
        // General Settings
        public ValueController maxGameTime;
        public ValueController maxAnimationTime;
        public ValueController maxResponseTime;
        public ValueController maxPauseTime;
        public ValueController maxRoundTime;
        public ValueController maxRounds;

        public void Save()
        {
            // Balancing stuff.
            store.party.spaceStoneCD = spaceStoneCD.GetValue();
            store.party.realityStoneCD = realityStoneCD.GetValue();
            store.party.mindStoneCD = mindStoneCD.GetValue();
            store.party.powerStoneCD = powerStoneCD.GetValue();
            store.party.timeStoneCD = timeStoneCD.GetValue();
            store.party.soulStoneCD = soulStoneCD.GetValue();
            store.party.mindStoneDMG = mindStoneDMG.GetValue();

            // General Settings
            store.party.maxGameTime = maxGameTime.GetValue();
            store.party.maxAnimationTime = maxAnimationTime.GetValue();
            store.party.maxResponseTime = maxResponseTime.GetValue();
            store.party.maxPauseTime = maxPauseTime.GetValue();
            store.party.maxRoundTime = maxRoundTime.GetValue();
            store.party.maxRounds = maxRounds.GetValue();
        }

        public void Load() 
        {
            // Balancing Stuffffffffffffffff.
            spaceStoneCD.SetValue(store.party.spaceStoneCD);
            realityStoneCD.SetValue(store.party.realityStoneCD);
            mindStoneCD.SetValue(store.party.mindStoneCD);
            powerStoneCD.SetValue(store.party.powerStoneCD);
            timeStoneCD.SetValue(store.party.timeStoneCD);
            soulStoneCD.SetValue(store.party.soulStoneCD);
            mindStoneDMG.SetValue(store.party.mindStoneDMG);

            // General settings
            maxGameTime.SetValue(store.party.maxGameTime);
            maxAnimationTime.SetValue(store.party.maxAnimationTime);
            maxResponseTime.SetValue(store.party.maxResponseTime);
            maxPauseTime.SetValue(store.party.maxPauseTime);
            maxRoundTime.SetValue(store.party.maxRoundTime);
            maxRounds.SetValue(store.party.maxRounds);

        }
    }
}