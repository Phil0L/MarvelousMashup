using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;

/*
 * convert an ID to an object
 * to save an ID use i.e. IDTracker.Add(id, character);
 * to get the appropriate object from an ID use i.e. var character = IDTracker.Get(id) as Character;
 */
public class IDTracker
{
    public static readonly Dictionary<IDs, IID> IdentifiersDictionary = new Dictionary<IDs, IID>();

    public static void Add(IDs id, IID obj)
    {
        IdentifiersDictionary.Add(id, obj);
    }
    
    public static IID Get(IDs id)
    {
        try
        {
            return IdentifiersDictionary[id];
        }
        catch (KeyNotFoundException)
        {
            return null;
        }
        
    }

    public static IID Remove(IDs id)
    {
        try
        {
            var iid = IdentifiersDictionary[id];
            IdentifiersDictionary.Remove(id);
            return iid;
        }
        catch (KeyNotFoundException)
        {
            return null;
        }
        
    } 
}


/*
 * Use this class to get the IDs object of any ingame object.
 * The class has to extend the IID interface!
 * if that is granted you can call .GetID on these objects
 * i.e. var idOfCharacter = character.GetID();
 */
public static class IDGetter
{
    public static IDs GetID(this IID obj)
    {
        var myKey = IDTracker.IdentifiersDictionary.FirstOrDefault(x => x.Value == obj).Key;
        return myKey;
    }
    
    public static IDs GetIDCasted(this IFieldContent obj)
    {
        if (obj is Character c) return GetID(c);
        if (obj is InfinityStone i) return GetID(i);
        if (obj is Rock r) return GetID(r);
        // TODO: Add Portal (maybe)
        return default;
    }
}