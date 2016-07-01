package com.astro.core.engine.physics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by kamil on 01.07.16.
 */
@Slf4j
@Component
public class PhysicsEngine {

    public PhysicsWorld physicsWorld;

    public PhysicsEngine() {
        log.info("create");
        physicsWorld = PhysicsWorld.instance;
    }

}
