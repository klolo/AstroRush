package com.astro.core.script.player.fire;

import com.astro.core.engine.physics.CollisionProcessResult;
import com.astro.core.objects.PhysicsObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.script.Point;
import com.astro.core.script.util.LogicTimer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.extern.slf4j.Slf4j;

/**
 * Logic of the SimpleShoot object.
 */
@Slf4j
public class SimpleShootLogic implements IShootLogic {

    private static final int DESTROY_TIME = 3;

    private static final float DAMAGE_AMOUNT = 0.1f;

    /**
     * Bullet physics.
     */
    private final PhysicsObject bullet;

    /**
     * Watcher for destroy object after some time.
     */
    private final LogicTimer watcher;

    int speed;

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

    public CollisionProcessResult processCollision(final IGameObject gameObject) {
        final CollisionProcessResult collisionProcessResult = new CollisionProcessResult();
        if (gameObject.getData().getLogic() instanceof Point) {
            collisionProcessResult.setIgnoreCollision(true);
        }
        else {
            bullet.getData().setDestroyed(true);
        }

        return collisionProcessResult;
    }

    @Override
    public float getDamageAmount() {
        return DAMAGE_AMOUNT;
    }
}