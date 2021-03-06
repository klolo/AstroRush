package com.astro.core.objects;

import com.astro.core.engine.physics.CollisionProcessResult;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Hold all GameObject data.
 * This class should have only getters and setters, all logic should be in GameObject.
 */
@Data
public class ObjectData {

    /**
     * Flip image via x-coordinates.
     * Dont use sprite.setFlip!
     */

    protected boolean flipX;

    /**
     * Flip image via y-coordinates.
     * Dont use sprite.setFlip!
     */
    @Getter
    @Setter
    protected boolean flipY;

    /**
     * Physics physicBody of the Player.
     */
    protected Body body;

    /**
     * Logic of the object.
     */
    protected ILogic logic;

    /**
     * Name of the object.
     */
    protected String name = "";

    /**
     * Object identifier using for communication beetwen objects.
     */
    protected String itemIdentifier = "";

    /**
     * Based libgdx information about object.
     */
    protected Sprite sprite = new Sprite();

    /**
     * Name of the layer where is the object.
     */
    protected String layerID = "Default";

    /**
     * Should be object removed?
     */
    protected boolean isDestroyed;

    /**
     * have this object create new object?
     */
    protected boolean hasChild;

    /**
     * Custom settings from editor.
     */
    protected HashMap<String, String> customVariables = new HashMap<>();

    /**
     * Callback method for collision. If present event will be send.
     * See: CollisionListener.
     */
    protected Function<IGameObject, CollisionProcessResult> collisionCallbackFunction;

}
