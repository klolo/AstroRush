package com.astro.core.script.player.fire;

import com.astro.core.engine.physics.PhysicsEngine;
import com.astro.core.objects.GameObject;
import com.astro.core.objects.PhysicsObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.script.player.PlayerState;
import com.astro.core.script.util.LogicTimer;
import com.astro.core.storage.GameResources;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleShoot implements IFireBehavior {

    @Setter
    private PlayerState playerState;

    private static final int DESTROY_TIME = 3;

    private static final int BULLET_SPEED = 10;

    private float playerPositionX = 0.0f;

    private float playerPositionY = 0.0f;

    private final PhysicsEngine physicsEngine;

    public SimpleShoot(final PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
    }

    /**
     * Logic of the SimpleShoot object.
     */
    class SimpleShootLogic implements ILogic {

        /**
         * Bullet physics.
         */
        private final PhysicsObject bullet;

        /**
         * Watcher for destroy object after some time.
         */
        private final LogicTimer watcher;

        private int speed;

        SimpleShootLogic(final PhysicsObject bullet) {
            this.bullet = bullet;
            watcher = new LogicTimer<>(bullet.getData()::setDestroyed, true, DESTROY_TIME);
        }

        @Override
        public void update(final float diff) {
            bullet.updatePosition();
            watcher.update(diff);
            bullet.getData().getBody().setLinearVelocity(speed, 0);
        }

        @Override
        public void setGameObject(final IGameObject gameObject) {

        }

        @Override
        public void additionalRender(final OrthographicCamera cam, final float delta) {
            bullet.setRenderingInScript(false);
            bullet.show(cam, delta);
            bullet.setRenderingInScript(true);
        }
    }

    public GameObject onFire() {
        final TextureRegion region = GameResources.instance.getResourceManager().getTextureRegion("keyRed");
        final PhysicsObject physicsObject = new PhysicsObject(region);

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

        return physicsObject;
    }


    public Sprite getSprite(final TextureRegion region) {
        final Sprite sprite = new Sprite(region);
        sprite.setScale(1.0f, 1.0f);
        sprite.setOrigin(0, 0);
        sprite.setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());
        return sprite;
    }

    public FixtureDef getFixtureDef(final PolygonShape polygonShape) {
        final FixtureDef myFixtureDef = new FixtureDef();
        myFixtureDef.shape = polygonShape;
        myFixtureDef.density = 1;
        return myFixtureDef;
    }

    public PolygonShape getPolygonShape() {
        final PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(.1f, .1f);
        return polygonShape;
    }

    public BodyDef getBodyDef(final float positionX, final float positionY) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        if (playerState == PlayerState.FLY_LEFT || playerState == PlayerState.RUN_LEFT) {
            bodyDef.position.x = positionX;
        }
        else {
            bodyDef.position.x = positionX + 2;//fixme
        }

        bodyDef.position.y = positionY + 1;//fixme
        return bodyDef;
    }

    public void update(final float positionX, final float positionY) {
        this.playerPositionX = positionX;
        this.playerPositionY = positionY;
    }

}
