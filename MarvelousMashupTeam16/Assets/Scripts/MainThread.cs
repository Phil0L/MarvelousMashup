using System;
using System.Collections.Generic;
using UnityEngine;

public class MainThread : MonoBehaviour
{
    private static List<Action> tasks = new List<Action>();
    private static List<Tuple<Action, int>> delays = new List<Tuple<Action, int>>();

    private void Awake()
    {
        DontDestroyOnLoad(this);
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
        foreach (var delay in delays.ToArray())
        {
            delays.Remove(delay);
            if (delay.Item2 > 0) delays.Add(new Tuple<Action, int>(delay.Item1, delay.Item2 -1));
            else tasks.Add(delay.Item1);
        }

        foreach (var task in tasks.ToArray())
        {
            task?.Invoke();
            tasks.Remove(task);
        }
    }
    
    
}
