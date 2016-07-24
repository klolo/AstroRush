package com.astro.core.objects;

import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

/**
 * Register of the game objects.
 */
@Slf4j
public enum ObjectsRegister {
    instance;

    private HashMap<String, IGameObject> gameObjectsWithID = new HashMap<>();

    private HashMap<Body, IGameObject> gameObjectsWithPhysics = new HashMap<>();

    public void registerObject(List<IGameObject> objectWithID) {
        objectWithID.forEach(o -> gameObjectsWithID.put(o.getData().getItemIdentifier(), o));
    }

    public void registerPhysicsObjects(List<IGameObject> objectWithID) {
        objectWithID.forEach(o -> gameObjectsWithPhysics.put(o.getData().getBody(), o));
    }

    public void registerPhysicsObject(final IGameObject objectWithID) {
        gameObjectsWithPhysics.put(objectWithID.getData().getBody(), objectWithID);
    }

    public IGameObject getObjectByID(final String id) {
        return gameObjectsWithID.get(id);
    }

    public IGameObject getObjectByBody(final Body body) {
        return gameObjectsWithPhysics.get(body);
    }

}
