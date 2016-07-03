package com.astro.core.script.player.fire;

import com.astro.core.objects.PhysicsObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.script.util.LogicTimer;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Logic of the SimpleShoot object.
 */
class SimpleShootLogic implements ILogic {

    private static final int DESTROY_TIME = 3;

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
}