package com.astro.core.logic.player.fire;

import com.astro.core.logic.player.PlayerState;
import com.astro.core.objects.GameObject;

/**
 * Represents shoot event on the player.
 */
public interface IFireBehavior {

    GameObject onFire();

    void update(final float x, final float y);

    void setPlayerState(final PlayerState playerState);

    float getFiringSpeed();
}
