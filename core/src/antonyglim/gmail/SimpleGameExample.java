package antonyglim.gmail;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.Iterator;

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

	//Для хранения капель используется список
	Array<Rectangle> raindropsList;						//Array - вместо ArrayList

	//Последнее появление капли
	long lastDropTime;

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

		//Cоздается экземпляр массива капель и порождается первая капля
		raindropsList = new Array<Rectangle>();
		createReindrop();
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
		//Рисуем капли
		for (Rectangle raindrop : raindropsList){
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();

		//Делаем ведро подвижным
		mousClickingAction();                                           //от мыши
		byPressingKeyboard();                                           //от клавиатуры

        //Ограничиваем движение ведра до границ области
        if (bucketRectangle.x < 0) bucketRectangle.x = 0;
        if (bucketRectangle.x > 800 - 64) bucketRectangle.x = 800 - 64;

        //сколько времени прошло, с тех пор как была создана новая капля и если необходимо, создавать еще одну новую каплю
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000){
			createReindrop();
		}
		raindropsFalling();


	}
	
	@Override
	public void dispose () {
		dropImage.dispose();
		bucketImage.dispose();
		dropSonud.dispose();
		rainMusic.dispose();
		batch.dispose();
	}

	/**
	 * Метод перемещает ведро в то место, где была нажата ЛКМ
	 */
	private void mousClickingAction(){
		if (Gdx.input.isTouched()){										//есть ли на данный момент прикосновение к экрану
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);		//возвращают текущую позицию прикосновения
			camera.unproject(touchPos);									//преобразование координат прикосновения/мыши в систему координат камеры.
			bucketRectangle.x = (int) touchPos.x - 64 / 2;
		}
	}

	/**
	 * Метод перемещает ведро взависимости от нажатий клавиатуры
	 */
	private void  byPressingKeyboard(){
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			bucketRectangle.x -= 200 * Gdx.graphics.getDeltaTime();		//Gdx.graphics.getDeltaTime() возвращает время, прошедшее между последним и текущим кадром в секундах
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			bucketRectangle.x += 200 * Gdx.graphics.getDeltaTime();
		}
	}

	/**
	 * Метод создает новый Rectangle,
	 * устанавливает его в случайной позиции в верхней части экрана
	 * и добавляет его в raindrops массив.
	 */
	private void createReindrop(){
		Rectangle raindropRectangle = new Rectangle();
		raindropRectangle.x = MathUtils.random(0, 800 - 64);
		raindropRectangle.y = 480;
		raindropRectangle.width = 64;
		raindropRectangle.height = 64;
		raindropsList.add(raindropRectangle);
		lastDropTime = TimeUtils.nanoTime();							//записываем текущее время в наносекундах


	}

	/**
	 * Метод отвечает за движение капель.
	 * Капля двигаются с постоянной скоростью 200 пикселей в секунду.
	 * Если капля находится ниже нижнего края экрана,
	 * мы удаляем ее из массива.
	 */
	private void raindropsFalling(){
		Iterator<Rectangle> iterator = raindropsList.iterator();
		while (iterator.hasNext()){
			Rectangle raindropRectangle = iterator.next();
			raindropRectangle.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindropRectangle.y + 64 < 0){
				iterator.remove();
			}
			//если капля попала в ведро
			if (raindropRectangle.overlaps(bucketRectangle)){
				dropSonud.play();
				iterator.remove();
			}
		}
	}
}
