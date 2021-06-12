using UnityEngine;
using UnityEngine.UI;

public class CharacterListElement : MonoBehaviour
{
    public bool selected;
    private bool _remove_listener;
    private bool _select_listener;
    
    public Character character;
    public CharacterListController controller;
    
    public Button remove;
    public Text name;

    public void SetCharacter(Character c)
    {
        character = c;
    }

    public void SetController(CharacterListController c)
    {
        controller = c;
    }
    
    // Start is called before the first frame update
    void Start()
    {
        _remove_listener = false;
        _select_listener = false;
    }

    // Update is called once per frame
    void Update()
    {
        if (controller != null && !_remove_listener)
        {
            remove.onClick.AddListener(() => controller.RemoveElementClicked(this));
            _remove_listener = true;
        }
        if (controller != null && !_select_listener)
        {
            GetComponent<Button>().onClick.AddListener(() => controller.ElementSelected(this));
            _select_listener = true;
        }

        if (character is {name: { }})
        {
            name.text = character.name;
        }
    }
}
