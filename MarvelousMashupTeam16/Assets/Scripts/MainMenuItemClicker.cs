using System.Collections;
using System.Collections.Generic;
using UnityEditor.SearchService;
using UnityEngine;
using UnityEngine.SceneManagement;

public class MainMenuItemClicker : MonoBehaviour
{
    public void EditorClicked()
    {
        SceneManager.LoadScene("Editor");
    }
}
