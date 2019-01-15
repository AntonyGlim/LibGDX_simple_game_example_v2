package antonyglim.gmail;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SimpleGameExample extends ApplicationAdapter {

	//загрузить assets и сохранить ссылки на них
	Texture dropImage;
	Texture bucketImage;
	Sound dropSonud;
	Music rainMusic;

	//добавим два новых поля в класс и назовем их camera и batch
	OrthographicCamera camera;
	SpriteBatch batch;

	@Override
	public void create () {
		// Загрузка изображений капли и ведра, каждое размером 64x64 пикселей
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		// Загрузка звукового эффекта падающей капли и фоновой "музыки" дождя
		dropSonud = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// Сразу же воспроизводиться музыка для фона
		rainMusic.setLooping(true);
		rainMusic.play();

		//создаем камеру
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);		//камера всегда показывает область мира игры

		//создать SpriteBatch
		batch = new SpriteBatch();

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


	}
	
	@Override
	public void dispose () {
	}
}
