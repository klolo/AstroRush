package com.astro.core.script;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.engine.base.CameraManager;
import com.astro.core.engine.interfaces.IObservedByCamera;
import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.TextureObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.observe.IKeyObserver;
import com.astro.core.observe.KeyObserve;
import com.astro.core.script.player.PlayerGraphics;
import com.astro.core.script.player.PlayerState;
import com.astro.core.script.player.PopupMsg;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.extern.slf4j.Slf4j;

/**
 * Logic of the Player.
 */
@Slf4j
public class Player implements ILogic, IKeyObserver, IObservedByCamera {

    public static final String IDENTIFIER = "player";

    private PopupMsg playerPopupMsg = new PopupMsg();

    private PlayerState state = PlayerState.STAND;

    /**
     * Hold default Player graphic (animation of run) and additional
     * graphics like fly and stand.
     */
    private PlayerGraphics graphics;

    /**
     * Physics body.
     */
    private Body body;

    //TODO: move to properties or other class
    private float MAX_Y_VELOCITY = 15f;

    private float MAX_Y_POSITION = 15f;

    private float playerHeight = 0.0f;

    /**
     * Amount of the pixel per meter.
     */
    @GameProperty("renderer.pixel.per.meter")
    protected int PIXEL_PER_METER = 0;

    public Player() {
        KeyObserve.instance.register(this);
        CameraManager.instance.setObservedObject(this);
    }

    public void setRunAnimation(IGameObject runAnimation) {
        runAnimation.getData().setCollisionConsumer(this::collisionEvent);
        playerHeight = ((AnimationObject) runAnimation).getAnimation().getKeyFrames()[0].getRegionHeight() / PIXEL_PER_METER;

        body = runAnimation.getData().getBody();
        body.setFixedRotation(true);

        graphics = new PlayerGraphics((AnimationObject) runAnimation);
    }

    @Override
    public void update(float diff) {
        updatePosition();
        playerPopupMsg.update(diff);
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

        if (body.getPosition().y > MAX_Y_POSITION) {
            body.setTransform(newX, MAX_Y_POSITION, 0);
        }
        graphics.getRunAnimation().getData().getSprite().setPosition(newX, newY);
        playerPopupMsg.setPos(newX, newY + 2 * playerHeight);
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

        body.setLinearVelocity(horizontalForce * 5, body.getLinearVelocity().y);
    }

    private void jump() {
        if (body.getLinearVelocity().y < MAX_Y_VELOCITY) {
            body.applyForceToCenter(0, MAX_Y_VELOCITY, false);
        }
    }

    public void collisionEvent(IGameObject collidatedObject) {
        log.debug("player collision");

        if (Point.IDENTIFIER.equals(collidatedObject.getData().getItemIdentifier())) {
            Point point = (Point) collidatedObject.getData().getLogic();
            playerPopupMsg.addMessagesToQueue(point.getPlayerMsg());
        }
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
