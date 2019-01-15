package antonyglim.gmail;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.awt.Rectangle;

public class SimpleGameExample extends ApplicationAdapter {

	//загрузить assets и сохранить ссылки на них
	Texture dropImage;
	Texture bucketImage;
	Sound dropSonud;
	Music rainMusic;

	//добавим два новых поля в класс и назовем их camera и batch
	OrthographicCamera camera;
	SpriteBatch batch;

	//Rectangle будет представлять ведро
	Rectangle bucketRectangle;

	//Вектор ля определения координатов ведра
	Vector3 touchPos;

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

		//Мы хотим, чтобы ведро было на 20 пикселей выше нижней границы экрана
		//и центрировалось по горизонтали
		bucketRectangle = new Rectangle();
		bucketRectangle.x = 800 / 2 - 64 / 2;
		bucketRectangle.y = 20;
		bucketRectangle.width = 64;
		bucketRectangle.height = 64;

		//Вектор отвечает за перемещения ведра от прикосновения
		touchPos = new Vector3();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);					//говорит OpenGL очистить экран
		camera.update();											//обновляем камеру

		//рисуем ведро
		batch.setProjectionMatrix(camera.combined);						//сообщается SpriteBatch использовать систему координат камеры
		batch.begin();													//начать новую batch серию
		batch.draw(bucketImage, bucketRectangle.x, bucketRectangle.y);
		batch.end();

		//Делаем ведро подвижным
		mousClickingAction();

	}
	
	@Override
	public void dispose () {
	}

	/**
	 * Метод перемещает ведро в то место, где была нажата ЛКМ
	 */
	public void mousClickingAction(){
		if (Gdx.input.isTouched()){										//есть ли на данный момент прикосновение к экрану
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);		//возвращают текущую позицию прикосновения
			camera.unproject(touchPos);									//преобразование координат прикосновения/мыши в систему координат камеры.
			bucketRectangle.x = (int) touchPos.x - 64 / 2;
		}
	}
}
