package net.badlogic.td.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import net.badlogic.td.game.Assets;
import net.badlogic.td.game.objects.Background;
import net.badlogic.td.util.B2DVARS;
import net.badlogic.td.util.Constants;

/**
 * Created by BruSD on 19.03.14.
 */
public class PlayScreen extends AbstractGameScreen {


    private World world;
    private Box2DDebugRenderer b2dRenderer;
    private OrthographicCamera cameraB2D;
    private SpriteBatch batch;

    private Background backgrounds;

    public PlayScreen(DirectedGame game) {
        super(game);

        world = new World(new Vector2(0, 0), false);
        b2dRenderer = new Box2DDebugRenderer();
        cameraB2D = new OrthographicCamera();
        cameraB2D.setToOrtho(false, Gdx.graphics.getWidth() / B2DVARS.PPM, Gdx.graphics.getHeight()/B2DVARS.PPM);

        TextureRegion back = Assets.instance.background.background;
        backgrounds = new Background(back, cameraB2D, 0f);

    }

    public void render() {
        // camera set position
        cameraB2D.position.set(0, 0, 0);//setPosition(player.getPosition().x * PPM + Game.V_WIDTH / 4, Game.V_HEIGHT / 2);
        cameraB2D.update();

        batch.setProjectionMatrix(cameraB2D.combined);
        // draw bgs
        backgrounds.render(batch);
    }

    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }
}
