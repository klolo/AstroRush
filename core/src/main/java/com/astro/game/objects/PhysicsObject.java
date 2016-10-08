package com.astro.game.objects;

import com.astro.game.engine.physics.PhysicsEngine;
import com.astro.game.objects.interfaces.IGameObject;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PhysicsObject extends TextureObject implements IGameObject {

    @Getter
    @Setter
    protected String bodyName = "";

    protected Fixture fixture;

    @Setter
    protected BodyDef bodyDef;

    @Setter
    protected FixtureDef fixtureDef;


    public void init(final PhysicsEngine physicsEngine) {
        data.body = physicsEngine.createBody(bodyDef);
        fixture = data.body.createFixture(fixtureDef);
        fixtureDef.shape.dispose();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void render(final OrthographicCamera cam, final float delta) {
        updatePosition();
        super.render(cam, delta);
    }

    public void updatePosition() {
        data.sprite.setPosition(data.body.getPosition().x, data.body.getPosition().y);
        data.sprite.setRotation((float) Math.toDegrees(data.body.getAngle()));
    }

}
