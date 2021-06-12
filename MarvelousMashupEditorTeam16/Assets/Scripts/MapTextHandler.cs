using UnityEngine;
using UnityEngine.UI;

public class MapTextHandler : MonoBehaviour
{
    public InputField name;
    public InputField author;
    public MapStore store;

    // Update is called once per frame
    void Start()
    {
        store.AddListener((map, action) =>
        {
            if (action == MapStore.MapAction.Load || action == MapStore.MapAction.Initial)
            {
                name.text = map.name;
                author.text = map.author;
            }
        });
    }

    public void NameChanged(string name)
    {
        store.GetMap().name = name;
    }

    public void AuthorChanged(string author)
    {
        store.GetMap().author = author;
    }
}