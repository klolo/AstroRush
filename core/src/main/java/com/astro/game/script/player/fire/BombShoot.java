package com.astro.game.script.player.fire;

import com.astro.game.objects.GameObject;
import com.astro.game.script.player.PlayerState;


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
