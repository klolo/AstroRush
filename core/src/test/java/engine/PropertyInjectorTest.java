package engine;

import com.astro.core.adnotation.GameProperty;
import com.astro.core.adnotation.processor.PropertyInjector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PropertyInjector.
 */
public class PropertyInjectorTest {

    class ForInjectTestClass {
        @GameProperty("window.width")
        public int width = 0;
    }

    @Test
    public void testInjector() {
        ForInjectTestClass testObj = new ForInjectTestClass();
        PropertyInjector.instance.inject(testObj);

        Assert.assertTrue("Properties should not be 0", testObj.width != 0);
    }

}
