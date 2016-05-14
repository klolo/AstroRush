package com.astro.core.objects;

import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kamil on 11.05.16.
 */
public enum ObjectsRegister {
    instance;

    private HashMap<String, IGameObject> gameObjectsWithID = new HashMap<>();

    private HashMap<Body, IGameObject> gameObjectsWithPhysics = new HashMap<>();

    public void clearRegister() {
        gameObjectsWithID = new HashMap<>();
        gameObjectsWithPhysics = new HashMap<>();
    }

    public void registerObject(List<IGameObject> objectWithID) {
        objectWithID.forEach(o -> gameObjectsWithID.put(o.getData().getItemIdentifier(), o));
    }

    public void registerPhysicsObject(List<IGameObject> objectWithID) {
        objectWithID.forEach(o -> gameObjectsWithPhysics.put(o.getData().getBody(), o));
    }


    public IGameObject getObjectByID(final String id) {
        return gameObjectsWithID.get(id);
    }

    public IGameObject getObjectByBody(final Body body) {
        return gameObjectsWithPhysics.get(body);
    }

}
