package com.astro.core.script.player;

import com.astro.core.engine.base.CameraManager;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.script.player.fire.IFireBehavior;
import com.astro.core.script.util.LogicTimer;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * All player non final field.
 */
public class PlayerData {

    @Getter
    @Autowired
    public PopupMsg playerPopupMsg;

    public final static int HORIZONTAL_FORCE_STRENGHT = 5;

    public int points = 0;

    @Getter
    public int liveAmount = 100;

    @Getter
    public int startLiveAmount = 100;

    @Getter
    @Autowired
    public PlayerSettings settings;

    @Getter
    @Autowired
    public CameraManager cameraManager;

    public PlayerState state = PlayerState.STAND;

    @Getter
    public CollisionProcessor collisionProcessor;

    @Getter
    public HashMap<WatchersID, LogicTimer> watchers;

    @Setter
    public IInteractWithPlayer interactObject;

    @Setter
    public boolean standOnThePlatform = false;

    public float posX;

    public float posY;

    @Getter
    public boolean isDead;

    @Autowired
    public IFireBehavior fireBehavior;

    public IGameObject gameObject;

    /**
     * Physics body.
     */
    @Getter
    public Body body;

    /**
     * Hold default Player graphic (animation of run) and additional
     * graphics like fly and stand.
     */
    public PlayerGraphics graphics;

}
