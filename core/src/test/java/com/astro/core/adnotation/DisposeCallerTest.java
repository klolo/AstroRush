package com.astro.core.adnotation;

import com.astro.core.adnotation.processor.DisposeCaller;
import org.junit.Assert;
import org.junit.Test;

public class DisposeCallerTest {

    private class DisposeTestClass {
        boolean disposeCalled;

        public void dispose() {
            disposeCalled = true;
        }
    }

    private class Wrapper {
        @Dispose
        DisposeTestClass testClass = new DisposeTestClass();
    }

    @Test
    public void shouldBeCalled() {
        //given
        final Wrapper wrapper = new Wrapper();

        //when
        new DisposeCaller().callDispose(wrapper);

        //then
        Assert.assertTrue("Dispose should be called", wrapper.testClass.disposeCalled);
    }

}
