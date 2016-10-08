package com.astro.game.script.player;

import com.astro.game.engine.base.CameraManager;
import com.astro.game.objects.interfaces.IGameObject;
import com.astro.game.script.player.fire.IFireBehavior;
import com.astro.game.script.util.LogicTimer;
import com.astro.game.script.util.PopupMsg;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * All player non final field.
 */
@Component
@Scope("prototype")
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
    public PlayerCollisionProcessor playerCollisionProcessor;

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

    @Setter
    public boolean canShoot = true;

    public LogicTimer shootTimer;

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