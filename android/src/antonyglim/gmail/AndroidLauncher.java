package antonyglim.gmail;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import antonyglim.gmail.SimpleGameExample;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;	//откл. акселерометр - экономим заряд
		config.useCompass = false;			//откл. компас - экономим заряд
		initialize(new SimpleGameExample(), config);
	}
}
