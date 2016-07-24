package com.astro.core.objects;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import common.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

@ContextConfiguration("classpath:configuration/core-config.xml")
@RunWith(GdxTestRunner.class)
public class TextureObjectTest {
//
//    @Setter
//    private ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
    }

    @Test
    public void shouldCreateSpriteBatch() {
        Mockito.mock(SpriteBatch.class);
        Mockito.mock(ShaderProgram.class);
        Mockito.mock(GL20.class);

        //given
        //final TextureObject textureObject = applicationContext.getBean("textureObject", TextureObject.class);

        //given

        //then
        //  Assert.assertNotNull("Batch cannot be null", textureObject.getBatch());
    }

}
