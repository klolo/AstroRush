package com.astro.core.script.player.fire;

import com.astro.core.objects.GameObject;
import com.astro.core.objects.interfaces.ILogic;

/**
 * Represents shoot event on the player.
 */
public interface IFireBehavior extends ILogic {

    GameObject onFire(final float positionX, final float positionY);

}
