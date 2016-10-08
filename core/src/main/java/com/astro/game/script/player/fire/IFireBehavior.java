package com.astro.game.script.player.fire;

import com.astro.game.objects.GameObject;
import com.astro.game.script.player.PlayerState;

/**
 * Represents shoot event on the player.
 */
public interface IFireBehavior {

    GameObject onFire();

    void update(final float x, final float y);

    void setPlayerState(final PlayerState playerState);

    float getFiringSpeed();
}
