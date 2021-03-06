package com.astro.core.logic;

import com.astro.core.logic.player.fire.SimpleShootLogic;
import com.astro.core.objects.GameObject;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
import com.badlogic.gdx.graphics.OrthographicCamera;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

@Slf4j
@ContextConfiguration("classpath:config/game-context.xml")
public class EnemyTest {

    private Player player = new Player();

    @Autowired
    private ObjectsRegister objectsRegister;

    class TestGameObject extends GameObject {

        @Override
        protected void render(final OrthographicCamera cam, final float delta) {
        }

        @Override
        public void show(final OrthographicCamera cam, final float delta) {
        }
    }

    @Before
    @SuppressWarnings("PMD")
    public void initData() {
        try {
            new TestContextManager(getClass()).prepareTestInstance(this);
        }
        catch (final Exception e) {
            LOGGER.error("Error while ", e);
        }

        TestGameObject testGameObject = new TestGameObject();
        testGameObject.getData().setLogic(player);
        objectsRegister.registerObject(testGameObject);
    }

    @Test
    public void shouldEnemyDied() {
        //given
        final Enemy enemy = new Enemy();
        final IGameObject gameObject = new TestGameObject();
        gameObject.getData().setLogic(new SimpleShootLogic());

        //when
        for (int i = 0; i <= 10; ++i) {
            enemy.collisionEvent(gameObject);
        }

        //then
        Assert.assertTrue("Should be enemy death", enemy.liveAmount < 0);
    }

    @Test
    public void shouldEnemySendEventToPlayer() {
//        //given
//        final Enemy enemy = new Enemy();
//        enemy.setGameObject(new AnimationObject());
//        enemy.liveAmount = -1f;
//
//        //when
//        enemy.update(1);
//
//        //then
//        Assert.assertTrue("Should be enemy death", player.getPoints() > 0);
    }


}



