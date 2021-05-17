using Newtonsoft.Json;
using UnityEngine;

public class MapModifierController : MonoBehaviour
{

    public MapStore mapStore;
    public ValueController widthController;
    public ValueController heightController;
    
    // Update is called once per frame
    void Update()
    {
        int newWidth = widthController.GetValue();
        int newHeight = heightController.GetValue();
        int oldWidth = mapStore.GetMap().width;
        int oldHeight = mapStore.GetMap().height;

        if (newHeight != oldHeight || newWidth != oldWidth)
        {
            mapStore.SetNewMap(new Map(newWidth, newHeight));
            string currentMapJson = mapStore.GetMap().ToJson();
            Debug.Log(currentMapJson);
        }
    }
}
