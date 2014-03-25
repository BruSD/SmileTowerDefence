
package net.badlogic.td;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "Smile Tower Defence";
        config.useGL20 = true;
        config.width = 800;
        config.height = 480;

		new LwjglApplication(new SmileTowerDefenceMain(), config);
	}
}
