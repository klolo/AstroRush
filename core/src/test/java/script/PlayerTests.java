package script;

import com.astro.core.script.Player;
import common.GdxTestRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests player script.
 */
@RunWith(GdxTestRunner.class)
public class PlayerTests {

    private Player player;

    @Before
    public void initPlayer() {
        player = new Player();
    }

    @Test
    public void initTest() {
        Assert.assertNotNull("Collision processor should be created", player.getCollisionProcesor());
        Assert.assertNotNull("Collision processor should be created", player.getWatchers());
        Assert.assertTrue("On start player should not have points", player.getPoints() == 0);
        player.addPoints(100);

        Assert.assertTrue("Points should sum", player.getPoints() == 100);
    }
}
