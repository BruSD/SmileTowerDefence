package net.badlogic.td.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import net.badlogic.td.screens.transitions.ScreenTransition;
import net.badlogic.td.screens.transitions.ScreenTransitionSlide;
import net.badlogic.td.util.Constants;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;

/**
 * Created by BruSD on 16.03.14.
 */
public class MenuScreen extends AbstractGameScreen{

    private Stage stage;
    private Skin skinTowerDefence;
    private Skin skinLibgdx;

    private Image imgLogo;
    private Image imgInfo;
    // menu buttons
    private Button btnMenuPlay;
    private Button btnMenuOptions;

    // debug
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private boolean debugEnabled = false;
    private float debugRebuildStage;

    public MenuScreen (DirectedGame game) {
        super(game);
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        if (debugEnabled) {
            debugRebuildStage -= deltaTime;
            if (debugRebuildStage <= 0) {
                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
                rebuildStage();
            }
        }
        stage.act(deltaTime);
        stage.draw();
        Table.drawDebug(stage);
    }

    @Override
    public void resize (int width, int height) {
        stage.setViewport(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT, false);
    }
    @Override
    public void show () {
        stage = new Stage();
        rebuildStage();
    }
    @Override
    public void hide () {
        stage.dispose();
        skinTowerDefence.dispose();
        skinLibgdx.dispose();
    }
    @Override
    public void pause () { }

    private void rebuildStage () {
        skinTowerDefence = new Skin(
                Gdx.files.internal(Constants.SKIN_CANYONBUNNY_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
        skinLibgdx = new Skin(
                Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
                new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
        // build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerObjects = buildObjectsLayer();
        Table layerLogos = buildLogosLayer();
        Table layerControls = buildControlsLayer();


        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerObjects);
        stack.add(layerLogos);
        stack.add(layerControls);

    }

    private Table buildBackgroundLayer () {
        Table layer = new Table();

        // + Background
        //imgBackground = new Image(skinCanyonBunny, "background");
        //layer.add(imgBackground);

        return layer;
    }

    private Table buildObjectsLayer () {
        Table layer = new Table();
        // + Coins
//        imgCoins = new Image(skinCanyonBunny, "coins");
//        layer.addActor(imgCoins);
//        imgCoins.setOrigin(imgCoins.getWidth() / 2,
//                imgCoins.getHeight() / 2);
//        imgCoins.addAction(sequence(
//                moveTo(135, -20),
//                scaleTo(0, 0),
//                fadeOut(0),
//                delay(2.5f),
//                parallel(moveBy(0, 100, 0.5f, Interpolation.swingOut),
//                        scaleTo(1.0f, 1.0f, 0.25f, Interpolation.linear),
//                        alpha(1.0f, 0.5f))));
//
//        // + Bunny
//        imgBunny = new Image(skinCanyonBunny, "bunny");
//        layer.addActor(imgBunny);
//        imgBunny.addAction(sequence(
//                moveTo(655, 510),
//                delay(4.0f),
//                moveBy(-70, -100, 0.5f, Interpolation.fade),
//                moveBy(-100, -50, 0.5f, Interpolation.fade),
//                moveBy(-150, -300, 1.0f, Interpolation.elasticIn)));
        return layer;
    }

    private Table buildLogosLayer () {
        Table layer = new Table();

        layer.left().top();
        // + Game Logo
        imgLogo = new Image(skinTowerDefence, "logo");
        layer.add(imgLogo);
        layer.row().expandY();
        // + Info Logos
        imgInfo = new Image(skinTowerDefence, "info");
        layer.add(imgInfo).bottom();
        if (debugEnabled) layer.debug();

        return layer;
    }

    private Table buildControlsLayer () {
        Table layer = new Table();

        layer.right().bottom();
        // + Play Button
        btnMenuPlay = new Button(skinTowerDefence, "play");
        layer.add(btnMenuPlay);
        btnMenuPlay.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });
        layer.row();
        // + Options Button
        btnMenuOptions = new Button(skinTowerDefence, "options");
        layer.add(btnMenuOptions);
        btnMenuOptions.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                onOptionsClicked();
            }
        });
        if (debugEnabled) layer.debug();
        return layer;
    }

    private void onOptionsClicked() {
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                ScreenTransitionSlide.RIGHT, true, Interpolation.bounceOut);

        //game.setScreen(new PlayScreen(game), transition);
    }

    private void onPlayClicked() {
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                ScreenTransitionSlide.RIGHT, true, Interpolation.bounceOut);

        game.setScreen(new GameScreen(game), transition);
    }
}
