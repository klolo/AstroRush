package com.astro.core.engine;

import com.astro.core.objects.interfaces.IGameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 01.05.16.
 */
@Slf4j
public enum LayerManager {
    instance;

    @Getter
    ArrayList<String> layers = new ArrayList<>();

    /**
     * If doesnt exists, add layer to list.
     */
    public void addLayer(String layer) {
        if (!layers.contains(layer)) {
            log.info("[register layer] name: {}", layer);
            layers.add(layer);
        }
    }


    public ArrayList<IGameObject> sortObjectsByLayer(ArrayList<IGameObject> gameObjects) {
        HashMap<String, LinkedList<IGameObject>> sortedObjectMap = new HashMap<>();
        layers.forEach(layer -> sortedObjectMap.put(layer, new LinkedList<>()));

        gameObjects.forEach(gObj ->
                sortedObjectMap.get(gObj.getLayerID()).add(gObj));

        ArrayList<IGameObject> sortedObjects = new ArrayList<>(gameObjects.size());
        layers.forEach(layer -> sortedObjects.addAll((LinkedList)sortedObjectMap.get(layer)));
        return sortedObjects;
    }


}
