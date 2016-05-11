package com.astro.core.objects;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.storage.PropertyInjector;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * Represents a rendereable.
 */
@Slf4j
abstract public class GameObject implements IGameObject {

    /**
     * Physics body of the Player.
     */
    @Getter
    @Setter
    protected Body body;

    /**
     * Logic of the object.
     */
    @Getter
    private ILogic logic;

    /**
     * Name of the object.
     */
    @Getter
    @Setter
    protected String name = "";

    /**
     * Object identifier using for communication beetwen objects.
     */
    @Getter
    @Setter
    protected String itemIdentifier = "";

    /**
     *
     */
    @Getter
    protected Sprite sprite;

    @Getter
    @Setter
    protected String layerID = "Default";

    /**
     * Amount of the pixel per meter.
     */
    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER = 0;

    /**
     * Custom settings from editor.
     */
    protected HashMap<String, String> customVariables = new HashMap<>();

    /**
     * Default constructor.
     */
    public GameObject() {
        PropertyInjector.instance.inject(this);
        sprite = new Sprite();
    }

    protected abstract void render(OrthographicCamera cam, float delta);

    /**
     *
     */
    public boolean hasLogic() {
        if (logic != null) {
            return true;
        }
        return false;
    }

    public void update(float delta) {
        if (logic != null)
            logic.update(delta);
    }

    /**
     * Set custom variable from Overlap editor.
     */
    public void setCustomVar(String key, String val) {
        customVariables.put(key, val);
    }

    public void setLogic(ILogic logic) {
        this.logic = logic;
    }

    /**
     * Does have object physics body?
     */
    public boolean isPhysicObject() {
        if (body != null) {
            return true;
        }

        return false;
    }
}
