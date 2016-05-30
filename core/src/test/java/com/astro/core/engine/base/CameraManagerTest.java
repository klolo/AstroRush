package com.astro.core.engine.base;

import com.astro.core.engine.interfaces.IObservedByCamera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import common.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test of the CameraManager singleton.
 */
@RunWith(GdxTestRunner.class)
public class CameraManagerTest {

    private Observable observable = new Observable();

    private CameraManager manager = CameraManager.instance;

    class Observable implements IObservedByCamera {

        float posX = 0.0f;

        float posY = 0.0f;

        @Override
        public float getPositionX() {
            return posX;
        }

        @Override
        public float getPositionY() {
            return posY;
        }
    }

    @Test
    public void testDefaultCameraMangerBehavior() {
        manager.setObservedObject(null);
        manager.update();
        OrthographicCamera cam = manager.getCamera();
        Assert.assertTrue("Camera default should be on center", cam.position.x == 0f && cam.position.y == 0f);
    }

    @Test
    public void testCameraMangerFollowing() {
        OrthographicCamera cam = manager.getCamera();
        manager.setObservedObject(observable);

        observable.posX = 10f;
        observable.posY = 10f;
        manager.update();

        Assert.assertTrue("Camera should fallow object", cam.position.x != 0f && cam.position.y != 0f);
    }
}

