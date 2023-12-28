package com.mygdx.game;

import static com.mygdx.game.MyGame.SCR_HEIGHT;
import static com.mygdx.game.MyGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenIntro implements Screen {

    Texture imgBackGround;

    MyButton btnPlay;
    MyButton btnSettings;
    MyButton btnExit;

    public ScreenIntro() {
        imgBackGround = new Texture("bg1.png");
        btnPlay = new MyButton("Play", MyGame.getGame().fontLarge, 0, SCR_HEIGHT*3/4);
        btnPlay.x = SCR_WIDTH/2 - btnPlay.width/2;
        btnSettings = new MyButton("Settings", MyGame.getGame().fontLarge, 0, SCR_HEIGHT/2);
        btnSettings.x = SCR_WIDTH/2 - btnSettings.width/2;
        btnExit = new MyButton("Exit", MyGame.getGame().fontLarge, 0, SCR_HEIGHT/4);
        btnExit.x = SCR_WIDTH/2 - btnExit.width/2;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // обработка касаний
		if(Gdx.input.justTouched()){
			MyGame.getGame().touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			MyGame.getGame().camera.unproject(MyGame.getGame().touch);

            if(btnPlay.hit(MyGame.getGame().touch.x, MyGame.getGame().touch.y)) {
                MyGame.getGame().setScreen(MyGame.getGame().screenGame);
            }
            if(btnExit.hit(MyGame.getGame().touch.x, MyGame.getGame().touch.y)) {
                Gdx.app.exit();
            }
            if(btnSettings.hit(MyGame.getGame().touch.x, MyGame.getGame().touch.y)) {
                MyGame.getGame().setScreen(MyGame.getGame().screenSettings);
            }
		}

        // события игры


        // отрисовка
        MyGame.getGame().batch.setProjectionMatrix(MyGame.getGame().camera.combined);
        MyGame.getGame().batch.begin();
        MyGame.getGame().batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnPlay.font.draw(MyGame.getGame().batch, btnPlay.text, btnPlay.x, btnPlay.y);
        btnSettings.font.draw(MyGame.getGame().batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnExit.font.draw(MyGame.getGame().batch, btnExit.text, btnExit.x, btnExit.y);
        MyGame.getGame().batch.end();
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
