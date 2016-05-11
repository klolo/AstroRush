package com.astro.core.objects;

import com.astro.core.objects.interfaces.IGameObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kamil on 11.05.16.
 */
public enum ObjectsRegister {
    instance;

    private HashMap<String, IGameObject> gameObjectsWithID = new HashMap<>();

    public void clearRegister() {
        gameObjectsWithID = new HashMap<>();
    }

    public void registerObject(List<IGameObject> objectWithID) {
        objectWithID.forEach(o -> gameObjectsWithID.put(o.getItemIdentifier(), o));
    }

    public IGameObject getObjectByID(final String id) {
        return gameObjectsWithID.get(id);
    }

}
