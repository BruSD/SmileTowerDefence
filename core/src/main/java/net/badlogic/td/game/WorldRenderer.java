package net.badlogic.td.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import net.badlogic.td.util.Constants;

/**
 * Created by BruSD on 16.03.14.
 */
public class WorldRenderer implements Disposable {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private WorldController worldController;

    private OrthographicCamera cameraGUI;


    private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
    private Box2DDebugRenderer b2debugRenderer;

    public WorldRenderer (WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    private void init () {
        batch = new SpriteBatch();

        //World view Camera
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
                Constants.VIEWPORT_HEIGHT);

        camera.update();


        //GUI Camera
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT);
        //cameraGUI.position.set(0 ,0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();


        b2debugRenderer = new Box2DDebugRenderer();
    }

    public void render () {
        renderWorld(batch);
        renderGui(batch);
    }



    public void resize (int width, int height) {
        //World view Camera
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();

        //GUI Camera
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT
                / (float)height) * (float)width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2,
                cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }

    @Override
    public void dispose() {

    }



    //region World
    private void renderWorld(SpriteBatch batch) {
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        worldController.level.render(batch);

        batch.end();

        if (DEBUG_DRAW_BOX2D_WORLD) {
            b2debugRenderer.render(worldController.b2world,
                    camera.combined);
        }
    }
    //endregion

    //region GUI
    private void renderGui(SpriteBatch batch) {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();

        // draw collected gold coins icon + text
        // (anchored to top left edge)
        renderGuiScore(batch);



        // draw FPS text (anchored to bottom right edge)
//        if (GamePreferences.instance.showFpsCounter)
        renderGuiFpsCounter(batch);

        // draw game over text
        renderGuiGameOverMessage(batch);
        batch.end();
    }
    private void renderGuiScore (SpriteBatch batch) {
        float x = -15;
        float y = -15;
        float offsetX = 50;
        float offsetY = 50;
        if (worldController.scoreVisual < worldController.score) {
            long shakeAlpha = System.currentTimeMillis() % 360;
            float shakeDist = 1.5f;
            offsetX += MathUtils.sinDeg(shakeAlpha * 2.2f) * shakeDist;
            offsetY += MathUtils.sinDeg(shakeAlpha * 2.9f) * shakeDist;
        }
//        batch.draw(Assets.instance.goldCoin.goldCoin,
//                x, y,
//                offsetX, offsetY,
//                100, 100,
//                0.35f, -0.35f,
//                0);
        Assets.instance.fonts.defaultBig.draw(batch,
                "" + (int)worldController.scoreVisual,
                x + 50, y + 37);
    }

    private void renderGuiFpsCounter(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 55;
        float y = cameraGUI.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
        if (fps >= 45) {
            // 45 or more FPS show up in green
            fpsFont.setColor(0, 1, 0, 1);
        } else if (fps >= 30) {
            // 30 or more FPS show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        } else {
            // less than 30 FPS show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }
        fpsFont.draw(batch, "FPS: " + fps, x, y);
        fpsFont.setColor(0, 0, 0, 1); // white
    }

    private void renderGuiGameOverMessage (SpriteBatch batch) {
        float x = cameraGUI.viewportWidth / 2;
        float y = cameraGUI.viewportHeight / 2;
        if (worldController.isGameOver()) {
            BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
            fontGameOver.setColor(1, 0.75f, 0.25f, 1);
            fontGameOver.drawMultiLine(batch, "GAME OVER",
                    x, y, 0, BitmapFont.HAlignment.CENTER);
            fontGameOver.setColor(1, 1, 1, 1);
        }
    }
    //endregion
}
