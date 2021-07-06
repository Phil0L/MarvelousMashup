using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class MainMenu : MonoBehaviour
{
    public Transform item;
    
    public List<string> items = new List<string>();
    private Dictionary<string, bool> active = new Dictionary<string, bool>();

    private List<Action> listener = new List<Action>();

    private void Start()
    {
        foreach (Transform existingItem in transform)
        {
            Button button = existingItem.GetComponent<Button>();
            
            button.onClick.RemoveAllListeners();
            button.onClick.AddListener(() => ItemClicked(existingItem.name));
        }
    }

    void Update()
    {
        foreach (Transform existingItem in transform)
        {
            if (!items.Contains(existingItem.name))
            {
                Destroy(existingItem.gameObject);
            }
        }

        foreach (string listItem in items)
        {
            if (transform.Find(listItem) == null)
            {
                var newItem = Instantiate(item, transform);
                newItem.gameObject.SetActive(true);
                newItem.name = listItem;
                newItem.GetComponent<Text>().text = listItem;
                newItem.GetComponent<Button>().onClick.AddListener(() => ItemClicked(listItem));
            }
        }
    }

    private void OnDrawGizmos()
    {
        foreach (Transform existingItem in transform)
        {
            if (!items.Contains(existingItem.name))
            {
                DestroyImmediate(existingItem.gameObject);
            }
        }

        foreach (string listItem in items)
        {
            if (transform.Find(listItem) == null)
            {
                var newItem = Instantiate(item, transform);
                newItem.gameObject.SetActive(true);
                newItem.name = listItem;
                newItem.GetComponent<Text>().text = listItem;
                newItem.GetComponent<Button>().onClick.AddListener(() => ItemClicked(listItem));
            }
        }
    }

    private void ItemClicked(string itemName)
    {
        if (active[itemName])
        {
            foreach (var lis in listener)
            {
                lis();
            }

            Debug.Log(itemName + " Main Menu Item Clicked!");
        }
    }

    public void Activate(string item)
    {
        active[item] = true;
    }

    public void Deactivate(string item)
    {
        active[item] = false;
    }
}
