package com.astro.game.script.player.fire;

import com.astro.game.engine.physics.PhysicsEngine;
import com.astro.game.objects.GameObject;
import com.astro.game.objects.ObjectsRegister;
import com.astro.game.objects.PhysicsObject;
import com.astro.game.script.player.PlayerState;
import com.astro.game.storage.GameResources;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Slf4j
public class SimpleShoot implements IFireBehavior, ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @Setter
    private PlayerState playerState;

    private static final int BULLET_SPEED = 10;

    private float playerPositionX = 0.0f;

    private float playerPositionY = 0.0f;

    private final PhysicsEngine physicsEngine;

    @Autowired
    public SimpleShoot(final PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
    }

    public GameObject onFire() {
        final TextureRegion region = GameResources.instance.getResourceManager().getTextureRegion("plasmaShoot");
        final PhysicsObject physicsObject = applicationContext.getBean("physicsObject", PhysicsObject.class);
        physicsObject.setTextureRegion(region);

        final BodyDef bodyDef = getBodyDef(playerPositionX, playerPositionY);
        final PolygonShape polygonShape = getPolygonShape();
        final FixtureDef myFixtureDef = getFixtureDef(polygonShape);
        final Sprite sprite = getSprite(region);

        physicsObject.setBodyDef(bodyDef);
        physicsObject.setFixtureDef(myFixtureDef);
        physicsObject.getData().setSprite(sprite);
        physicsObject.init(physicsEngine);

        final SimpleShootLogic simpleShootLogic = new SimpleShootLogic(physicsObject);

        if (playerState == PlayerState.FLY_LEFT || playerState == PlayerState.RUN_LEFT) {
            simpleShootLogic.speed = -1 * BULLET_SPEED;
        }
        else {
            simpleShootLogic.speed = BULLET_SPEED;
        }

        physicsObject.getData().setLogic(simpleShootLogic);
        physicsObject.setRenderingInScript(true);


        physicsObject.getData().setCollisionCallbackFunction(simpleShootLogic::processCollision);
        applicationContext.getBean(ObjectsRegister.class).registerPhysicsObject(physicsObject);
        return physicsObject;
    }


    public Sprite getSprite(final TextureRegion region) {
        final Sprite sprite = new Sprite(region);
        sprite.setScale(.5f, .5f);
        sprite.setOrigin(0, 0);
        sprite.setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());
        return sprite;
    }

    private FixtureDef getFixtureDef(final PolygonShape polygonShape) {
        final FixtureDef myFixtureDef = new FixtureDef();
        myFixtureDef.shape = polygonShape;
        myFixtureDef.density = 1;
        return myFixtureDef;
    }

    private PolygonShape getPolygonShape() {
        final PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(.1f, .1f);
        return polygonShape;
    }

    private BodyDef getBodyDef(final float positionX, final float positionY) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        if (playerState == PlayerState.FLY_LEFT || playerState == PlayerState.RUN_LEFT) {
            bodyDef.position.x = positionX - 0.5f;
        }
        else {
            bodyDef.position.x = positionX + 1.5f;//fixme
        }

        bodyDef.position.y = positionY + 1;//fixme
        return bodyDef;
    }

    public void update(final float positionX, final float positionY) {
        this.playerPositionX = positionX;
        this.playerPositionY = positionY;
    }

    @Override
    public float getFiringSpeed() {
        return .2f; //fixme: move to constant
    }

}
