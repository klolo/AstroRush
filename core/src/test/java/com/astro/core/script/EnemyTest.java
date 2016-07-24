package com.astro.core.script;

import com.astro.core.objects.GameObject;
import com.astro.core.objects.ObjectsRegister;
import com.astro.core.objects.interfaces.IGameObject;
import com.astro.core.script.player.fire.SimpleShootLogic;
import com.badlogic.gdx.graphics.OrthographicCamera;
import common.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class EnemyTest {

    private Player player = new Player();

    class TestGameObject extends GameObject {

        @Override
        protected void render(final OrthographicCamera cam, final float delta) {
        }

        @Override
        public void show(final OrthographicCamera cam, final float delta) {
        }
    }

    @Before
    public void initData() {
        TestGameObject testGameObject = new TestGameObject();
        testGameObject.getData().setLogic(player);
        ObjectsRegister.instance.registerObject(testGameObject);
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



