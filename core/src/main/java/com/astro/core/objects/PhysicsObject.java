package com.astro.core.objects;

import com.astro.core.engine.PhysicsWorld;
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
public class PhysicsObject extends GameObject implements IGameObject {

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

    protected BodyDef getBodyDefinition() {
        return null;
    }

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

    public void render() {
        if(body.getType()== BodyDef.BodyType.DynamicBody) {
            renderDynamic();
        }
        else {
            super.render();
        }
    }

    public void renderDynamic() {
        super.draw(body.getPosition().x,body.getPosition().y);
    }

}
