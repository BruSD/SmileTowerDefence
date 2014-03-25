package net.badlogic.td.screens.transitions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by BruSD on 16.03.14.
 */
public interface ScreenTransition {

    public float getDuration ();

    public void render (SpriteBatch batch,
                        Texture currScreen,
                        Texture nextScreen,
                        float alpha);
}
