using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class BackButton : MonoBehaviour
{
    public string scene;
    
    public void Clicked()
    {
        Debug.Log("Clicked");
        SceneManager.LoadScene(scene);
    }
}
