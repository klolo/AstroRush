package com.astro.game.engine.physics;

import lombok.Data;

/**
 * Return from object where collision is processes. Used for send extra data to collision processor
 */
@Data
public class CollisionProcessResult {

    public boolean ignoreCollision;

}
