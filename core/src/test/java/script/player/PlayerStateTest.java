package script.player;

import com.astro.core.script.player.PlayerState;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test of the player state enume.
 */
public class PlayerStateTest {

    @Test
    public void testPlayerStates() {
        PlayerState state = PlayerState.FLY_LEFT;

        Assert.assertTrue("Player should stand", state.getState(0f, 0f, 0f, 0f, state) == PlayerState.STAND);
        Assert.assertTrue("Player should run", PlayerState.RUN_LEFT.isRun());
        Assert.assertTrue("Player should run", PlayerState.RUN_RIGHT.isRun());
        Assert.assertTrue("Player should fly", PlayerState.FLY_LEFT.isFly());
        Assert.assertTrue("Player should fly", PlayerState.FLY_RIGHT.isFly());

        Assert.assertTrue("MINIMAL_PLAYER_MOVE should not be 0", state.getMINIMAL_PLAYER_MOVE() != 0.0f);
    }

}
