package com.astro.core.overlap_runtime;

import com.google.common.base.Preconditions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;


@ContextConfiguration("classpath:configuration/core-config.xml")
public class OverlapSceneReaderTest {

    @Autowired
    private OverlapSceneReader overlapSceneReader;

    @Before
    public void setUp() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCheckNull() {
        //given
        Preconditions.checkNotNull(overlapSceneReader, "Overlap should be injected");

        //when
        overlapSceneReader.processComponentList(null, null);
    }

    @Test
    public void shouldHasConverters() {
        //given
        Preconditions.checkNotNull(overlapSceneReader, "Overlap should be injected");

        //then
        Assert.assertNotNull("componentLoader loader cannot be null", overlapSceneReader.componentLoader);
        Assert.assertNotNull("particleEffectsLoader loader cannot be null", overlapSceneReader.particleEffectsLoader);
        Assert.assertNotNull("lightsLoader loader cannot be null", overlapSceneReader.lightsLoader);
        Assert.assertNotNull("labelsLoader loader cannot be null", overlapSceneReader.labelsLoader);
        Assert.assertNotNull("spriteAnimationsLoader loader cannot be null", overlapSceneReader.spriteAnimationsLoader);
    }

}
