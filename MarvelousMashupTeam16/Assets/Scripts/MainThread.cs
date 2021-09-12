using System;
using System.Collections.Generic;
using UnityEngine;

public class MainThread : MonoBehaviour
{
    private static List<Action> tasks = new List<Action>();
    private static List<Tuple<Action, int>> delays = new List<Tuple<Action, int>>();
    private static MainThread _instance;

    private void Awake()
    {
        if (_instance == null)
        {
            DontDestroyOnLoad(this);
            _instance = this;
        }
        else Destroy(gameObject);
    }

    public static void Execute(Action callback)
    {
        tasks.Add(callback);
    }

    public static void ExecuteDelayed(Action callback, int delay)
    {
        delays.Add(new Tuple<Action, int>(callback, delay));
    }

    private void Update()
    {
        for(int i = 0; i < delays.Count; i++)
        {
            var delay = delays[i];
            delays.RemoveAt(i);
            if (delay.Item2 > 0) delays.Add(new Tuple<Action, int>(delay.Item1, delay.Item2 -1));
            else tasks.Add(delay.Item1);
        }

        for (int i = 0; i < tasks.Count; i++)
        {
            tasks[i]?.Invoke();
            tasks.RemoveAt(i);
        }
        
    }
    
    
}
