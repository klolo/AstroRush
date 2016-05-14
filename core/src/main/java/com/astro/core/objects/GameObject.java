package com.astro.core.objects;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.adnotation.processor.PropertyInjector;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Represents a rendereable.
 */
@Slf4j
abstract public class GameObject implements IGameObject {

    @Getter
    @Setter
    protected ObjectData data = new ObjectData();

    /**
     * Amount of the pixel per meter.
     */
    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER = 0;


    /**
     * Default constructor.
     */
    public GameObject() {
        PropertyInjector.instance.inject(this);
        data.setSprite(new Sprite());
    }

    /**
     *
     */
    public boolean hasLogic() {
        if (data.getLogic() != null) {
            return true;
        }
        return false;
    }

    public void update(float delta) {
        if (data.getLogic() != null)
            data.getLogic().update(delta);
    }

    /**
     * Set custom variable from Overlap editor.
     */
    public void setCustomVar(String key, String val) {
        data.getCustomVariables().put(key, val);
    }

    /**
     * Does have object physics body?
     */
    public boolean isPhysicObject() {
        if (data.getBody() != null) {
            return true;
        }
        return false;
    }

    /**
     * Called by GameEngine.
     */
    protected abstract void render(OrthographicCamera cam, float delta);
}
