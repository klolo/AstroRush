package com.astro.core.engine;

import com.astro.core.objects.PhysicsObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.objects.interfaces.IPlayer;
import com.astro.core.observe.IKeyObserver;
import com.astro.core.observe.KeyObserve;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kamil on 23.04.16.
 */
@Slf4j
public class Player extends PhysicsObject implements IKeyObserver, IPlayer, ILogic {

    IGameObject gameObject;

    public Player() {
        super(new Texture("assets/ico.png"));
        this.getSprite().setSize(50.0f, 50.0f);
        this.getSprite().setOrigin(0.0f, 0.0f);
        super.init();

       // KeyObserve.instance.register(this);
    }

    /**
     * @return
     */
    protected FixtureDef getFixtureDefinition() {
        // Create a circle shape and set its radius to 6
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(
                this.getSprite().getWidth() / 2 / PhysicsWorld.instance.PIXEL_PER_METER,
                this.getSprite().getHeight() / 2 / PhysicsWorld.instance.PIXEL_PER_METER
        );

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = .4f; // Make it bounce a little bit

        return fixtureDef;
    }

    @Override
    protected BodyDef getBodyDefinition() {
        BodyDef def = super.getDefaultBodyDefinition();
        def.fixedRotation = true;
        def.position.y = 5;
        return def;
    }

    /**
     * @return
     */
    public float getPositionX() {
        return body.getPosition().x;
    }

    /**
     * @return
     */
    public float getPositionY() {
        return body.getPosition().y;
    }


    @Override
    public void dispose() {
        super.dispose();
        //  texture.dispose();
    }

    @Override
    public void keyPressEvent(int keyCode) {
        int horizontalForce = 0;
        if (Input.Keys.LEFT == keyCode) {
            horizontalForce -= 1;
        }
        else if (Input.Keys.RIGHT == keyCode) {
            horizontalForce += 1;
        }
        else if (Input.Keys.UP == keyCode) {
            body.applyForceToCenter(0, 6, false);
        }

        body.setLinearVelocity(horizontalForce * 5, body.getLinearVelocity().y);
    }

    @Override
    public void keyReleaseEvent(int keyCode) {

    }

    @Override
    public void setGameObject(IGameObject gameObject) {

    }
}
