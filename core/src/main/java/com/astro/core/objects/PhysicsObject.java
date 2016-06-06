package com.astro.core.objects;

import com.astro.core.engine.physics.PhysicsWorld;
import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import lombok.Getter;
import lombok.Setter;


public class PhysicsObject extends TextureObject implements IGameObject {

    @Getter
    @Setter
    protected String bodyName = "";

    protected Fixture fixture;

    protected FixtureDef getFixtureDefinition() {
        return null;
    }

    public PhysicsObject(final TextureRegion texture) {
        super(texture);
    }

    public void init() {
        data.body = PhysicsWorld.instance.createBody(getBodyDefinition());

        // Create our fixture and attach it to the body
        FixtureDef fixtureDef = getFixtureDefinition();
        fixture = data.body.createFixture(fixtureDef);

        fixtureDef.shape.dispose();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void render(final OrthographicCamera cam, final float delta) {
        data.sprite.setPosition(data.body.getPosition().x, data.body.getPosition().y);
        data.sprite.setRotation((float) Math.toDegrees(data.body.getAngle()));
        super.draw();
    }

    protected BodyDef getBodyDefinition() {
        return null;
    }


}
