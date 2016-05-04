package com.astro.core.objects;

import com.astro.core.engine.PhysicsWorld;
import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kamil on 26.04.16.
 */
public class PhysicsObject extends TextureObject implements IGameObject {

    @Getter
    @Setter
    protected String bodyName = "";

    protected Fixture fixture;

    protected FixtureDef getFixtureDefinition() {
        return null;
    }

    @Setter
    protected Body body;

    public PhysicsObject(Texture texture) {
        super(texture);
    }

    public PhysicsObject(TextureRegion texture) {
        super(texture);
    }

    public void init() {
        body = PhysicsWorld.instance.createBody(getBodyDefinition());

        // Create our fixture and attach it to the body
        FixtureDef fixtureDef = getFixtureDefinition();
        fixture = body.createFixture(fixtureDef);

        fixtureDef.shape.dispose();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void render(OrthographicCamera cam, float delta) {
        getSprite().setPosition(body.getPosition().x, body.getPosition().y);
        getSprite().setRotation((float) Math.toDegrees(body.getAngle()));
        super.draw();
    }

    protected BodyDef getBodyDefinition() {
        return null;
    }

    /**
     * @return
     */
    protected BodyDef getDefaultBodyDefinition() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(
                getSprite().getX() / PhysicsWorld.instance.PIXEL_PER_METER,
                getSprite().getY() / PhysicsWorld.instance.PIXEL_PER_METER);
        return bodyDef;
    }

}
