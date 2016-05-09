package com.astro.core.script;

import com.astro.core.engine.CameraManager;
import com.astro.core.engine.IPlayerObserver;
import com.astro.core.engine.PlayerMove;
import com.astro.core.objects.AnimationObject;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.objects.interfaces.ILogic;
import com.astro.core.observe.IKeyObserver;
import com.astro.core.observe.KeyObserve;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import lombok.extern.slf4j.Slf4j;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO:
 * - dodac texture kiedy gracz stoi w miejscu i ja wyswietlac zamiast animacji
 * - dodac obsluge kolizji z innymi obiektami
 * - usunac obiekt player z engine i dodac sledzenie kamery na tym obiekcie
 */
@Slf4j
public class Player implements ILogic, IKeyObserver {

    private AnimationObject gameObject;

    private Body body;

    private List<IPlayerObserver> playerObservers = new LinkedList<>();

    public Player() {
        KeyObserve.instance.register(this);
        playerObservers.add(CameraManager.instance);
    }

    public void addPlayerObserver(final IPlayerObserver playerObserver) {
        playerObservers.add(playerObserver);
    }

    public void setGameObject(IGameObject gameObject) {
        this.gameObject = (AnimationObject) gameObject;
        ((AnimationObject) gameObject).getBody().setFixedRotation(true);
        body =  ((AnimationObject) gameObject).getBody();
    }

    @Override
    public void update(float diff) {
        gameObject.getSprite().setPosition(body.getPosition().x, body.getPosition().y);
        PlayerMove move = new PlayerMove(body.getPosition().x, body.getPosition().y);
        WeakReference<PlayerMove> playerMoveWeakReference = new WeakReference<>(move);
        playerObservers.forEach(e -> e.updatePosition(playerMoveWeakReference.get()));
    }

    @Override
    public void keyPressEvent(int keyCode) {
        int horizontalForce = 0;
        if (Input.Keys.LEFT == keyCode) {
            horizontalForce -= 1;
            gameObject.setFlipX(true);
        }
        else if (Input.Keys.RIGHT == keyCode) {
            horizontalForce += 1;
            gameObject.setFlipX(false);
        }
        else if (Input.Keys.UP == keyCode) {
            body.applyForceToCenter(0, 40, false);
        }

        body.setLinearVelocity(horizontalForce * 5, body.getLinearVelocity().y);
    }

    @Override
    public void keyReleaseEvent(int keyCode) {

    }
}
