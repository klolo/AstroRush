package com.astro.game.objects;

import com.astro.game.engine.physics.PhysicsEngine;
import com.astro.game.objects.interfaces.IGameObject;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Represents a single game object.
 */
@Slf4j
@ToString
@Component
@Scope("prototype")
abstract public class GameObject implements IGameObject {

    @Getter
    @Setter
    protected ObjectData data = new ObjectData();

    @Getter
    @Setter
    protected boolean renderingInScript;

    /**
     * Amount of the pixel per meter.
     */
    @Value("${renderer.pixel.per.meter}")
    protected int pixelPerMeter = 0;

    @Getter
    @Setter
    protected PhysicsEngine physicsEngine;

    /**
     * Default constructor.
     */
    public GameObject() {
        data.setSprite(new Sprite());
    }

    /**
     * Looking for logic script in object data.
     */
    public boolean hasLogic() {
        return data.getLogic() != null;
    }

    /**
     * Update game logic.
     */
    public void update(float delta) {
        if (data.getLogic() != null) {
            data.getLogic().update(delta);
        }
    }

    /**
     * Does have object physics body?
     */
    public boolean isPhysicObject() {
        return data.getBody() != null;
    }

    /**
     * Called by GameEngine.
     */
    protected abstract void render(OrthographicCamera cam, float delta);


}
