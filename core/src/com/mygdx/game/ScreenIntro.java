package com.mygdx.game;

import static com.mygdx.game.MyGame.SCR_HEIGHT;
import static com.mygdx.game.MyGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ScreenIntro implements Screen {
    MyGame game;

    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont fontLarge;

    Texture imgBackGround;

    MyButton btnPlay;
    MyButton btnSettings;
    MyButton btnExit;

    public ScreenIntro(MyGame myGame) {
        game = myGame;
        batch = game.batch;
        camera = game.camera;
        touch = game.touch;
        fontLarge = game.fontLarge;
        
        imgBackGround = new Texture("bg1.png");
        btnPlay = new MyButton("Play", fontLarge, 0, SCR_HEIGHT*3/4);
        btnPlay.x = SCR_WIDTH/2 - btnPlay.width/2;
        btnSettings = new MyButton("Settings", fontLarge, 0, SCR_HEIGHT/2);
        btnSettings.x = SCR_WIDTH/2 - btnSettings.width/2;
        btnExit = new MyButton("Exit", fontLarge, 0, SCR_HEIGHT/4);
        btnExit.x = SCR_WIDTH/2 - btnExit.width/2;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // обработка касаний
		if(Gdx.input.justTouched()){
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);

            if(btnPlay.hit(touch.x, touch.y)) {
                game.setScreen(game.screenGame);
            }
            if(btnExit.hit(touch.x, touch.y)) {
                Gdx.app.exit();
            }
            if(btnSettings.hit(touch.x, touch.y)) {
                game.setScreen(game.screenSettings);
            }
		}

        // события игры


        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnPlay.font.draw(batch, btnPlay.text, btnPlay.x, btnPlay.y);
        btnSettings.font.draw(batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnExit.font.draw(batch, btnExit.text, btnExit.x, btnExit.y);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}
