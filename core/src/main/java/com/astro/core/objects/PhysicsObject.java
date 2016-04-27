package com.astro.core.objects;

import com.astro.core.engine.PhysicsWorld;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kamil on 26.04.16.
 */
public abstract class PhysicsObject extends GameObject implements  IGameObject {

    @Getter
    @Setter
    protected String bodyName = "";

    protected Fixture fixture;

    abstract protected FixtureDef getFixtureDefinition();

    protected Body body;

    public PhysicsObject(Texture texture) {
        super(texture);
    }

    public void init() {
        body = PhysicsWorld.instance.createBody(getBodyDefinition());

        // Create our fixture and attach it to the body
        FixtureDef fixtureDef = getFixtureDefinition();
        fixture = body.createFixture(fixtureDef);

        fixtureDef.shape.dispose();
    }

    protected abstract BodyDef getBodyDefinition();

    /**
     *
     * @return
     */
    protected BodyDef getDefaultBodyDefinition() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(
                getSprite().getX() / PhysicsWorld.instance.getPIXEL_PER_METER(),
                getSprite().getY() / PhysicsWorld.instance.getPIXEL_PER_METER());
        return bodyDef;
    }

    @Override
    public void render() {
        float PPM = PhysicsWorld.instance.getPIXEL_PER_METER();
        batch.draw(texture,
                body.getPosition().x * PPM - ( getSprite().getWidth() / 2),
                body.getPosition().y * PPM - ( getSprite().getHeight() / 2),
                getSprite().getWidth(),
                getSprite().getHeight()
        );
    }
}
