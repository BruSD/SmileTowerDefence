
package net.brusd.td.smile;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import net.badlogic.td.SmileTowerDefenceMain;

public class MainActivity extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGL20 = true;
        initialize(new SmileTowerDefenceMain(), config);
	}
}
