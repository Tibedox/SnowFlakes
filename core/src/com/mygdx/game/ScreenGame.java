package com.mygdx.game;

import static com.mygdx.game.MyGame.SCR_HEIGHT;
import static com.mygdx.game.MyGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

public class ScreenGame implements Screen {
    MyGame game;

    SpriteBatch batch;
    OrthographicCamera camera;
    Vector3 touch;
    BitmapFont font;
    BitmapFont fontRecords;
    BitmapFont fontLarge;
    
    InputKeyboard keyboard;

    Texture imgBackGround;
    Texture imgSnowFlake;
    Texture imgBtnSoundOn, imgBtnSoundOff;
    Sound sndChpok;

    MyButton btnSound;
    MyButton btnRestart;
    MyButton btnExit;

    SnowFlake[] snowFlakes = new SnowFlake[100];
    boolean isSoundOn = true;
    boolean isGameOver;
    boolean isEnterName;
    long timeStartGame;
    int timeGameDuration = 5020;
    String timeOfGame = "";
    Player[] players = new Player[11];
    int score;
    String namePlayer = "Player";

    public ScreenGame(MyGame myGame) {
        game = myGame;
        batch = game.batch;
        camera = game.camera;
        touch = game.touch;
        fontLarge = game.fontLarge;
        font = game.font;
        fontRecords = game.fontRecords;
        
        keyboard = new InputKeyboard(SCR_WIDTH, SCR_HEIGHT, 10);

        imgBackGround = new Texture("bg1.png");
        imgSnowFlake = new Texture("flake.png");
        imgBtnSoundOn = new Texture("sndon.png");
        imgBtnSoundOff = new Texture("sndoff.png");
        sndChpok = Gdx.audio.newSound(Gdx.files.internal("chpok.mp3"));

        btnSound = new MyButton(10, SCR_HEIGHT-60, 50);
        btnRestart = new MyButton("RESTART", font, SCR_WIDTH/3, SCR_HEIGHT/8);
        btnExit = new MyButton("MAINMENU", font, SCR_WIDTH*1.8f/3, SCR_HEIGHT/8);

        for (int i = 0; i < snowFlakes.length; i++) {
            snowFlakes[i] = new SnowFlake();
        }

        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("Noname", 0);
        }

        Gdx.input.setInputProcessor(new MyInput());
    }

    @Override
    public void show() {
        restartGame();
    }

    @Override
    public void render(float delta) {
// обработка касаний
		/*if(Gdx.input.justtouched()){
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);
			System.out.println(touch.x + " "+ touch.y);
			for (int i = 0; i < snowFlakes.length; i++) {
				if(snowFlakes[i].hit(touch.x, touch.y)) {
					snowFlakes[i].respawn();
				}
			}
		}*/

        // события игры
        for (int i = 0; i < snowFlakes.length; i++) {
            snowFlakes[i].fly();
        }

        if(!isGameOver) {
            if (TimeUtils.millis() - timeStartGame < timeGameDuration) {
                timeOfGame = getTime();
            } else {
                isGameOver = true;
                isEnterName = true;
            }
        }

        // отрисовка
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        for (int i = 0; i < snowFlakes.length; i++) {
            batch.draw(imgSnowFlake, snowFlakes[i].x, snowFlakes[i].y, snowFlakes[i].width/2, snowFlakes[i].height/2,
                    snowFlakes[i].width, snowFlakes[i].height, 1, 1, snowFlakes[i].angle, 0, 0, 100, 100, false, false);
        }
        batch.draw(isSoundOn?imgBtnSoundOn:imgBtnSoundOff, btnSound.x, btnSound.y, btnSound.width, btnSound.height);
        font.draw(batch, "СНЕЖИНОК: "+score, SCR_WIDTH-10-textWidth("СНЕЖИНОК: "+score, font), SCR_HEIGHT-20);
        font.draw(batch, timeOfGame, 0, SCR_HEIGHT - 20, SCR_WIDTH, Align.center, true);
        if(isGameOver) {
            fontLarge.draw(batch, "GAME OVER", 0, SCR_HEIGHT*8.5f/10, SCR_WIDTH, Align.center, true);
            for (int i = 0; i < players.length-1; i++) {
                fontRecords.draw(batch, players[i].name+"....."+players[i].score, 0, SCR_HEIGHT*7.3f/10-40*i, SCR_WIDTH, Align.center, true);
            }
            btnRestart.font.draw(batch, btnRestart.text, btnRestart.x, btnRestart.y);
            btnExit.font.draw(batch, btnExit.text, btnExit.x, btnExit.y);
        }
        if(isEnterName) keyboard.draw(batch);
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
        imgSnowFlake.dispose();
        imgBtnSoundOn.dispose();
        imgBtnSoundOff.dispose();
        sndChpok.dispose();
        keyboard.dispose();
    }

    float textWidth(String text, BitmapFont font) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, text);
        return layout.width;
    }

    String getTime() {
        long time = (TimeUtils.millis()-timeStartGame);
        long hour = time/60/60/1000;
        long min = time/1000/60%60;
        long sec = time/1000%60%60;
        long msec = time%1000;
        return min/10+min%10+":"+sec/10+sec%10;
    }

    void saveRecords(){
        Preferences preferences = Gdx.app.getPreferences("MyFirstGameRecords");
        for (int i = 0; i < players.length; i++) {
            preferences.putString("player"+i, players[i].name);
            preferences.putInteger("score"+i, players[i].score);
        }
        preferences.flush();
    }

    void loadRecords(){
        Preferences preferences = Gdx.app.getPreferences("MyFirstGameRecords");
        for (int i = 0; i < players.length; i++) {
            if(preferences.contains("player"+i)) players[i].name = preferences.getString("player" + i, "none");
            if(preferences.contains("score"+i)) players[i].score = preferences.getInteger("score" + i, 0);
        }
    }

    void sortRecords(){
        players[players.length-1].name = namePlayer;
        players[players.length-1].score = score;
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0; i < players.length-1; i++) {
                if (players[i].score < players[i + 1].score) {
                    Player player = players[i];
                    players[i] = players[i + 1];
                    players[i + 1] = player;
                    flag = true;
                }
            }
        }
    }

    void restartGame(){
        score = 0;
        for (int i = 0; i < snowFlakes.length; i++) {
            snowFlakes[i].respawn();
        }
        timeStartGame = TimeUtils.millis();
        isGameOver = false;
    }

    class MyInput implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            touch.set(screenX, screenY, 0);
            camera.unproject(touch);

            if(btnSound.hit(touch.x, touch.y)) {
                isSoundOn = !isSoundOn;
            }
            if(isGameOver) {
                if(isEnterName) {
                    if (keyboard.endOfEdit(touch.x, touch.y)) {
                        namePlayer = keyboard.getText();
                        isEnterName = false;
                        loadRecords();
                        sortRecords();
                        saveRecords();
                    }
                }
                if(btnRestart.hit(touch.x, touch.y) & !isEnterName){
                    restartGame();
                }
                if(btnExit.hit(touch.x, touch.y) & !isEnterName){
                    game.setScreen(game.screenIntro);
                }
            }
            if(!isGameOver) {
                for (int i = 0; i < snowFlakes.length; i++) {
                    if (snowFlakes[i].hit(touch.x, touch.y)) {
                        snowFlakes[i].respawn();
                        score++;
                        if (isSoundOn) {
                            sndChpok.play();
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}
