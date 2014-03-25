package net.badlogic.td.screens;

/**
 * Created by BruSD on 16.03.14.
 */

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import net.badlogic.td.game.Assets;

public abstract class AbstractGameScreen implements Screen {


    protected DirectedGame game;

    public AbstractGameScreen (DirectedGame game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        Assets.instance.init(new AssetManager());
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }

    public abstract InputProcessor getInputProcessor();
}
