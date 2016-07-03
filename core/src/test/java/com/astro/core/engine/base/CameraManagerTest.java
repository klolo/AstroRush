package com.astro.core.engine.base;

import common.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test of the CameraManager singleton.
 */
@RunWith(GdxTestRunner.class)
public class CameraManagerTest {
//
//    private Observable observable = new Observable();
//
//    private CameraManager manager = CameraManager.instance;
//
//    class Observable implements IObservedByCamera {
//
//        float posX;
//
//        float posY;
//
//        @Override
//        public float getPositionX() {
//            return posX;
//        }
//
//        @Override
//        public float getPositionY() {
//            return posY;
//        }
//    }

    @Test
    public void shouldBeCameraOnCenter() {
//        //given
//        manager.setObservedObject(null);
//
//        //when
//        manager.update();
//
//        //then
//        OrthographicCamera cam = manager.getCamera();
//        Assert.assertTrue("Camera default should be on center", cam.position.x == 0f && cam.position.y == 0f);
    }

    @Test
    public void shouldCameraFollowPlayer() {
//        //given
//        OrthographicCamera cam = manager.getCamera();
//        manager.setObservedObject(observable);
//
//        observable.posX = 10f;
//        observable.posY = 10f;
//
//        //when
//        manager.update();
//
//        //then
//        Assert.assertTrue("Camera should fallow object", cam.position.x != 0f && cam.position.y != 0f);
    }
}

