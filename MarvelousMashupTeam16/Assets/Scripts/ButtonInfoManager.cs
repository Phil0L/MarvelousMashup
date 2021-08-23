using System.Collections.Generic;
using System.Linq;
using UnityEngine;

public class ButtonInfoManager : MonoBehaviour
{
    public enum IconButton
    {
        RightClick,
        LeftClick
    }

    public List<Sprite> sprites;

    public ButtonInfo textPrefab;
    public ButtonInfo iconPrefab;

    public ButtonInfo Add(string title, string icon)
    {
        var pf = Instantiate(textPrefab, transform);
        pf.title = title;
        pf.text = icon;
        pf.name = title;
        return pf;
    }

    public ButtonInfo Add(string title, Sprite icon)
    {
        var pf = Instantiate(iconPrefab, transform);
        pf.title = title;
        pf.icon = icon;
        pf.name = title;
        return pf;
    }

    public ButtonInfo Add(string title, IconButton icon)
    {
        return Add(title, sprites[(int) icon]);
    }

    public ButtonInfo Of(int index)
    {
        return transform.GetChild(index).GetComponent<ButtonInfo>();
    }

    public List<ButtonInfo> All()
    {
        return transform.GetComponentsInChildren<ButtonInfo>().ToList();
    }

    public void Delete(ButtonInfo bi)
    {
        if (bi) Destroy(bi.gameObject);
    }

    public void Delete(int index)
    {
        Destroy(transform.GetChild(index).gameObject);
    }
}