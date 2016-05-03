package com.astro.core.engine;

import com.astro.core.objects.interfaces.IGameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Util class for sorting, filter object on the Map.
 */
@Slf4j
public enum ScreenManager {
    instance;

    /**
     * Registered layers.
     */
    @Getter
    private ArrayList<String> layers = new ArrayList<>();

    /**
     * If doesnt exists, add layer to list.
     */
    public void addLayer(final String layer) {
        if (!layers.contains(layer)) {
            log.info("[register layer] name: {}", layer);
            layers.add(layer);
        }
    }

    /**
     * Sort object by layer.
     */
    public ArrayList<IGameObject> sortObjectsByLayer(final ArrayList<IGameObject> gameObjects) {
        HashMap<String, LinkedList<IGameObject>> sortedObjectMap = new HashMap<>();
        layers.forEach(layer -> sortedObjectMap.put(layer, new LinkedList<>()));

        gameObjects.forEach(gObj ->
                sortedObjectMap.get(gObj.getLayerID()).add(gObj));

        ArrayList<IGameObject> sortedObjects = new ArrayList<>(gameObjects.size());
        layers.forEach(layer -> sortedObjects.addAll((LinkedList) sortedObjectMap.get(layer)));
        return sortedObjects;
    }

    /**
     * Return objects with have logic.
     */
    public ArrayList<IGameObject> getObjectsWithLogic(final ArrayList<IGameObject> gameObjects) {
        List<IGameObject> result = gameObjects.stream().filter(e -> e.hasLogic()).collect(Collectors.toList());
        return new ArrayList<>(result);
    }

}
