package com.astro.core.logic.player.fire;

import com.astro.core.logic.player.PlayerState;
import com.astro.core.objects.GameObject;


public class BombShoot implements IFireBehavior {

    @Override
    public GameObject onFire() {
        return null;
    }

    @Override
    public void update(final float x, final float y) {

    }

    @Override
    public void setPlayerState(final PlayerState playerState) {

    }

    @Override
    public float getFiringSpeed() {
        return 1;
    }
}
