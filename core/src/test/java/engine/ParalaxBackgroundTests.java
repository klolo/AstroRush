package engine;

import com.astro.core.engine.ParalaxBackground;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by kamil on 04.05.16.
 */
public class ParalaxBackgroundTests {

    private ParalaxBackground background;

    @Before
    public void setUp() throws Exception {
        background = new ParalaxBackground();
    }

    @Test
    public void injectPropertiesTest() {
        Assert.assertTrue("Texture file name should be loaded", !"".equals(background.getTEXTURE_FILE()));
        Assert.assertTrue("Amount of background should bigger than 0", background.getBACKGROUND_AMOUNT() != 0);
        Assert.assertTrue("Margin should be different than 0", background.getTEXTURE_MARGIN_DRAW() != 0f);
        Assert.assertTrue("Scale should be different than 0", background.getTEXTURE_SCALE() != 0f);
    }

}
