package engine;

import com.astro.core.engine.ParalaxBackground;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by kamil on 04.05.16.
 */
public class ParalaxBackgroundTests {

    @Test
    public void initSimpleMOde() {
        ParalaxBackground background = new ParalaxBackground();

        Assert.assertTrue("At least one texture", background.getTextures().size() > 0);
    }

}
