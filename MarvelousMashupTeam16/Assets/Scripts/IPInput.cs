using System.Collections;
using System.Text;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

public class IPInput : MonoBehaviour
{
    private EventSystem _system;
    public InputField hostnameInput;
    public InputField portInput;
    public string defaultHostname;
    public int defaultPort;
    
    private bool portCleared;
    
    private void Start()
    {
        _system = EventSystem.current;
        Text placeholder;
        placeholder = hostnameInput.placeholder as Text;
        if (placeholder is { }) placeholder.text = defaultHostname;
        placeholder = portInput.placeholder as Text;
        if (placeholder is { }) placeholder.text = defaultPort.ToString();
    }
    
    private void Update()
    {
        if (Input.GetKeyDown(KeyCode.Tab))
        {
            if (Input.GetKey(KeyCode.LeftShift))
            {
                Selectable next = _system.currentSelectedGameObject.GetComponent<Selectable>().FindSelectableOnUp();
 
                if (next!= null) {
                       
                    InputField inputfield = next.GetComponent<InputField>();
                    if (inputfield !=null) inputfield.OnPointerClick(new PointerEventData(_system));  //if it's an input field, also set the text caret
                    //if (inputfield !=null) inputfield.caretPosition = inputfield.text.Length;
                       
                    _system.SetSelectedGameObject(next.gameObject, new BaseEventData(_system));
                }
                //else Debug.Log("previous nagivation element not found");
            }
            else
            {
                Selectable next = _system.currentSelectedGameObject.GetComponent<Selectable>().FindSelectableOnDown();
 
                if (next!= null) {
                       
                    InputField inputfield = next.GetComponent<InputField>();
                    if (inputfield !=null) inputfield.OnPointerClick(new PointerEventData(_system));  //if it's an input field, also set the text caret
                    
                    _system.SetSelectedGameObject(next.gameObject, new BaseEventData(_system));
                }
                //else Debug.Log("next nagivation element not found");
            }
        }
        
        if (Input.GetKeyDown(KeyCode.Backspace))
        {
            if (portInput.isFocused && portInput.text.Equals(""))
            {
                if (portCleared)
                {
                    Selectable next = _system.currentSelectedGameObject.GetComponent<Selectable>().FindSelectableOnUp();

                    if (next != null)
                    {

                        InputField inputfield = next.GetComponent<InputField>();
                        // if (inputfield !=null) inputfield.OnPointerClick(new PointerEventData(_system));  //if it's an input field, also set the text caret
                        StartCoroutine(DisableHighlight(inputfield));

                        _system.SetSelectedGameObject(next.gameObject, new BaseEventData(_system));
                    }
                    //else Debug.Log("previous nagivation element not found");
                }
                else portCleared = true;
            }
            else portCleared = false;
        }
    }

    public void OnHostnameChange(string hostname)
    {
        StringBuilder sb = new StringBuilder();
        char lastC = ' ';
        foreach (var c in hostname)
        {
            if (lastC.Equals(' ') && char.IsDigit(c)) sb.Append(c);
            if (char.IsDigit(lastC) && (char.IsDigit(c) || c.Equals('.'))) sb.Append(c);
            if (lastC.Equals('.') && char.IsDigit(c)) sb.Append(c);

            lastC = c;
        }

        hostnameInput.text = sb.ToString();
    }
    
    private IEnumerator DisableHighlight(InputField inputField)
    {
        Debug.Log("Selected!");

        //Get original selection color
        Color originalTextColor = inputField.selectionColor;
        //Remove alpha
        originalTextColor.a = 0f;

        //Apply new selection color without alpha
        inputField.selectionColor = originalTextColor;

        //Wait one Frame(MUST DO THIS!)
        yield return null;

        //Change the caret pos to the end of the text
        inputField.caretPosition = inputField.text.Length;

        //Return alpha
        originalTextColor.a = 1f;

        //Apply new selection color with alpha
        inputField.selectionColor = originalTextColor;
    }

    public string GetHostName()
    {
        if (hostnameInput.text.Equals("")) return defaultHostname;
        return hostnameInput.text;
    }

    public int GetPort()
    {
        if (portInput.text.Equals("")) return defaultPort;
        return int.Parse(portInput.text);
    }
    
}
