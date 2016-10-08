package com.astro.core.engine.base;

import com.astro.core.engine.interfaces.IObservedByCamera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.google.common.base.Preconditions;
import common.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Test of the CameraManager singleton.
 */
@RunWith(GdxTestRunner.class)
@ContextConfiguration("classpath:config/game-context.xml")
public class CameraManagerTest {

    class Observable implements IObservedByCamera {

        float posX;

        float posY;

        @Override
        public float getPositionX() {
            return posX;
        }

        @Override
        public float getPositionY() {
            return posY;
        }
    }

    private Observable observable = new Observable();

    @Autowired
    private CameraManager cameraManager;


    @Test
    public void shouldBeCameraCorrectlyInit() {
        //given
        Preconditions.checkNotNull(cameraManager, "camera shoul be injected");

        //then
        Assert.assertNotNull("Physics cannot be null", cameraManager.getPhysicsEngine());
        Assert.assertTrue("pixelPerMeter should be set", cameraManager.pixelPerMeter != 0);
    }


    @Test
    public void shouldBeCameraOnCenter() {
        //given
        cameraManager.setObservedObject(null);

        //when
        cameraManager.update();

        //then
        final OrthographicCamera cam = cameraManager.getCamera();
        Assert.assertTrue("Camera default should be on center", cam.position.x == 0f && cam.position.y == 0f);
    }

    @Test
    public void shouldCameraFollowPlayer() {
        //given
        final OrthographicCamera cam = cameraManager.getCamera();
        cameraManager.setObservedObject(observable);

        observable.posX = 10f;
        observable.posY = 10f;

        //when
        cameraManager.update();

        //then
        Assert.assertTrue("Camera should fallow object", cam.position.x != 0f && cam.position.y != 0f);
    }
}

