package antonyglim.gmail.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import antonyglim.gmail.SimpleGameExample;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "GlimSimpleGame";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new SimpleGameExample(), config);
	}
}
