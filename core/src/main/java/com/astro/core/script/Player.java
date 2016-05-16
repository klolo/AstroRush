package com.astro.core.script;

import com.astro.core.engine.base.CameraManager;
import com.astro.core.engine.interfaces.IObservedByCamera;
import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.TextureObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.observe.IKeyObserver;
import com.astro.core.observe.KeyObserve;
import com.astro.core.script.player.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Logic of the Player.
 */
@Slf4j
public class Player implements ILogic, IKeyObserver, IObservedByCamera {

    public static final String IDENTIFIER = "player";

    @Getter
    private PopupMsg playerPopupMsg = new PopupMsg();

    @Getter
    private PlayerState state = PlayerState.STAND;


    private PlayerCollisionProcesor collisionProcesor;

    private PlayerSettings settings;

    @Setter
    private IInteractWithPlayer interactObject;

    @Setter
    private float interactiObjectTime = 0.0f;

    /**
     * Hold default Player graphic (animation of run) and additional
     * graphics like fly and stand.
     */
    private PlayerGraphics graphics;

    /**
     * Physics body.
     */
    @Getter
    private Body body;

    public Player() {
        settings = new PlayerSettings();
        KeyObserve.instance.register(this);
        CameraManager.instance.setObservedObject(this);
        collisionProcesor = new PlayerCollisionProcesor(this);
    }

    public void setGameObject(IGameObject runAnimation) {
        runAnimation.getData().setCollisionConsumer(this::collisionEvent);
        settings.playerHeight =
                ((AnimationObject) runAnimation).getAnimation().getKeyFrames()[0].getRegionHeight() / settings.PIXEL_PER_METER;

        body = runAnimation.getData().getBody();
        body.setFixedRotation(true);

        graphics = new PlayerGraphics((AnimationObject) runAnimation);
    }

    @Override
    public void update(float diff) {
        updatePosition();
        playerPopupMsg.update(diff);

        if(interactObject!=null) {
            interactiObjectTime += diff;

            if(interactiObjectTime > 3) {
                interactObject = null;
            }
        }
    }

    /**
     *
     */
    private void updatePosition() {
        float lastX = graphics.getRunAnimation().getData().getSprite().getX();
        float lastY = graphics.getRunAnimation().getData().getSprite().getY();

        float newX = body.getPosition().x;
        float newY = body.getPosition().y;

        state = state.getState(lastX, lastY, newX, newY);
        graphics.getRunAnimation().setRenderingInScript(!state.isRun());

        if (body.getPosition().y > settings.MAX_Y_POSITION) {
            body.setTransform(newX, settings.MAX_Y_POSITION, 0);
        }
        graphics.getRunAnimation().getData().getSprite().setPosition(newX, newY);
        playerPopupMsg.setPos(newX, newY + 2 * settings.playerHeight);
    }

    @Override
    public void keyPressEvent(int keyCode) {
        int horizontalForce = 0;
        if (Input.Keys.LEFT == keyCode) {
            horizontalForce -= 1;
            graphics.getRunAnimation().setFlipX(true);
        }
        else if (Input.Keys.RIGHT == keyCode) {
            horizontalForce += 1;
            graphics.getRunAnimation().setFlipX(false);
        }
        else if (Input.Keys.UP == keyCode) {
            jump();
        }
        else if (Input.Keys.SHIFT_LEFT == keyCode) {
            processInterAct();
        }

        body.setLinearVelocity(horizontalForce * 5, body.getLinearVelocity().y);
    }

    private void processInterAct() {
        if(interactObject!=null) {
            interactObject.interact();
        }
    }

    private void jump() {
        if (body.getLinearVelocity().y < settings.MAX_Y_VELOCITY) {
            body.applyForceToCenter(0, settings.MAX_Y_VELOCITY, false);
        }
    }

    public void collisionEvent(IGameObject collidatedObject) {
        log.debug("player collision");
        collisionProcesor.processCollision(collidatedObject);
    }

    @Override
    public void keyReleaseEvent(int keyCode) {

    }

    @Override
    public float getPositionX() {
        return body.getPosition().x;
    }

    @Override
    public float getPositionY() {
        return body.getPosition().y;
    }

    @Override
    public void additionalRender(final OrthographicCamera cam, float delta) {
        playerPopupMsg.show(cam, delta);

        if (graphics.getRunAnimation().isRenderingInScript()) {
            float posX = graphics.getRunAnimation().getData().getSprite().getX();
            float posY = graphics.getRunAnimation().getData().getSprite().getY();

            IGameObject gfxObject = graphics.getTextureBasedOnState(state);
            gfxObject.getData().getSprite().setPosition(posX, posY);

            if (state.isFly()) {
                ((TextureObject) gfxObject).setFlipX(state == PlayerState.FLY_LEFT);
            }

            gfxObject.show(cam, delta);
        }

    }
}
