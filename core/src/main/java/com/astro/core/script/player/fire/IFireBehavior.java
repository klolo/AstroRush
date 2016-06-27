package com.astro.core.script.player.fire;

import com.astro.core.objects.GameObject;
import com.astro.core.script.player.PlayerState;

/**
 * Represents shoot event on the player.
 */
public interface IFireBehavior {

    GameObject onFire();

    void update(final float x, final float y);

    void setPlayerState(final PlayerState playerState);

}
