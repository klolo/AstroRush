package engine;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;

import static org.mockito.Mockito.mock;

/**
 * Created by kamil on 04.05.16.
 */
public class InitLibgdx {

    public Application app;
    public Graphics graphics;
    public Audio audio;
    public Input input;
    public Files files;
    public Net net;

    public GL20 gl;
    public GL20 gl20;
    public GL30 gl30;

    public void init() {
        Gdx.app = mock(Application.class);
        Gdx.graphics = mock(Graphics.class);
        Gdx.audio = mock(Audio.class);
        Gdx.input = mock(Input.class);
        Gdx.files = mock(Files.class);
        Gdx.net = mock(Net.class);
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);
        Gdx.gl30 = mock(GL30.class);
    }
}
