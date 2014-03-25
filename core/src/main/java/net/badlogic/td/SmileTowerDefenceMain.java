
package net.badlogic.td;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Interpolation;
import net.badlogic.td.game.Assets;
import net.badlogic.td.screens.DirectedGame;
import net.badlogic.td.screens.MenuScreen;
import net.badlogic.td.screens.transitions.ScreenTransition;
import net.badlogic.td.screens.transitions.ScreenTransitionSlide;


public class SmileTowerDefenceMain extends DirectedGame {

    private static final String TAG = SmileTowerDefenceMain.class.getName();

    @Override
    public void create () {
        // Set Libgdx log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        // Load assets
        Assets.instance.init(new AssetManager());

        // Load preferences for audio settings and start playing music
        //GamePreferences.instance.load();


        // Start game at menu screen
        ScreenTransition transition = ScreenTransitionSlide.init(0.0f,
                ScreenTransitionSlide.RIGHT, true, Interpolation.bounceOut);
        setScreen(new MenuScreen(this), transition);
    }
}
