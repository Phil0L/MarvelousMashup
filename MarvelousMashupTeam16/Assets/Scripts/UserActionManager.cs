using UnityEngine;

public class UserActionManager : MonoBehaviour
{
    public PathDisplayer PathDisplayer;
    public AttackDisplayer AttackDisplayer;
    public InfinityStoneActionDisplayer InfinityStoneActionDisplayer;
    public InfinityStonePassDisplayer InfinityStonePassDisplayer;

    private ButtonInfo biAction;
    private ButtonInfo biCancel;
    

    public UserAction activeAction;

    public void Cancel()
    {
        Debug.Log("Canceled Action!");
        switch (activeAction)
        {
            case UserAction.Red:
            case UserAction.Blue:
            case UserAction.Green:
            case UserAction.Orange:
            case UserAction.Yellow:
            case UserAction.Violet:
                InfinityStoneActionDisplayer.Deactivate();
                break;
            case UserAction.CloseRange:
            case UserAction.LongRange:
                AttackDisplayer.Deactivate();
                break;
            case UserAction.Move:
                PathDisplayer.Deactivate();
                break;
            case UserAction.StonePass:
                InfinityStonePassDisplayer.Deactivate();
                break;
        }
    }

    private void Update()
    {
        
        // Finding current active
        if (InfinityStoneActionDisplayer.active != 0)
        {
            activeAction = (UserAction) (InfinityStoneActionDisplayer.active + 3);
        }
        else if (InfinityStonePassDisplayer.active)
        {
            activeAction = UserAction.StonePass;
        }
        else
        {
            if (PathDisplayer.active)
                activeAction = UserAction.Move;
            if (AttackDisplayer.active && AttackDisplayer.MaxLength() > 1)
                activeAction = UserAction.LongRange;
            if (AttackDisplayer.active && AttackDisplayer.MaxLength() == 1)
                activeAction = UserAction.CloseRange;

            if (!PathDisplayer.active && !AttackDisplayer.active)
                activeAction = UserAction.NoActive;
        }

        // Managing Action Info
        if (activeAction != UserAction.NoActive && !biAction)
        {
            biAction = Game.Controller().ButtonInfoManager.Add("Action", ButtonInfoManager.IconButton.LeftClick);
        }

        if (activeAction == UserAction.NoActive && biAction)
        {
            Game.Controller().ButtonInfoManager.Delete(biAction);
        }
        
        if (biAction) biAction.usable = AttackDisplayer.Confirmable() &&
                                        PathDisplayer.Confirmable() &&
                                        InfinityStoneActionDisplayer.Confirmable() && 
                                        InfinityStonePassDisplayer.Confirmable();

        // Setting Action Text
        switch (activeAction)
        {
            case UserAction.Red:
                biAction.title = "Use Reality Stone";
                break;
            case UserAction.Blue:
                biAction.title = "Use Space Stone";
                break;
            case UserAction.Green:
                biAction.title = "Use Time Stone";
                break;
            case UserAction.Orange:
                biAction.title = "Use Soul Stone";
                break;
            case UserAction.Yellow:
                biAction.title = "Use Mind Stone";
                break;
            case UserAction.Violet:
                biAction.title = "Use Power Stone";
                break;
            case UserAction.CloseRange:
                biAction.title = "Close Range Attack";
                break;
            case UserAction.LongRange:
                biAction.title = "Long Range Attack";
                break;
            case UserAction.Move:
                biAction.title = "Move Character";
                break;
            case UserAction.StonePass:
                biAction.title = !InfinityStonePassDisplayer.hasStoneSelected ? "Select Infinitystone" : "Pass Infinitystone";
                break;
            
        }

        
        // Managing Cancel
        if (activeAction != UserAction.NoActive && !biCancel)
        {
            biCancel = Game.Controller().ButtonInfoManager.Add("Cancel", "Esc");
        }

        if (activeAction == UserAction.NoActive && biCancel)
        {
            Game.Controller().ButtonInfoManager.Delete(biCancel);
        }

        if (activeAction != UserAction.NoActive && Input.GetKey(KeyCode.Escape))
        {
            Cancel();
        }
    }
}