using System;
using UnityEngine;
using UnityEngine.UI;

public class CharacterTimeoutAction : MonoBehaviour
{
    public Text Text;
    public Image TextImage;
    public Image Image;

    public void Set(int count, bool active = true)
    {
        if (count > 0 || !active) Image.color = new Color(1, 1, 1, 0.7f);
        else Image.color = Color.white;

        if (count > 0) TextImage.gameObject.SetActive(true);
        else TextImage.gameObject.SetActive(false);

        Text.text = "-" + count;
    }

    private Action callback;
    private Action overwrite;
    public void OnClick(Action callback) => this.callback = callback;
    public void Override(Action callback) => this.overwrite = callback;

    public void ClearOverride() => this.overwrite = null;
    public void _Clicked()
    {
        if (overwrite != null)
        {
            overwrite();
            overwrite = null;
            return;
        }
        if (Image.color.a > 0.9f)
        {
            callback();
        }
    }

    
}