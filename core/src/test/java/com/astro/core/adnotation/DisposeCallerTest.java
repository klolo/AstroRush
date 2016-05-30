package com.astro.core.adnotation;

import com.astro.core.adnotation.processor.DisposeCaller;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by kamil on 12.05.16.
 */
public class DisposeCallerTest {

    class DisposeTestClass {
        public boolean disposeCalled;

        public void dispose() {
            disposeCalled = true;
        }
    }

    class Wrapper {
        @Dispose
        public DisposeTestClass testClass = new DisposeTestClass();
    }

    @Test
    public void disposeCallTest() {
        Wrapper wrapper = new Wrapper();
        new DisposeCaller().callDispose(wrapper);

        Assert.assertTrue("Dispose should be called", wrapper.testClass.disposeCalled);
    }

}
