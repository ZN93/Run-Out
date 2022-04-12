package com.zappycode.coingame.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;


import java.util.ArrayList;
import java.util.Random;


// Partie déclaration des variables
public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Texture dizzy;
	int manState = 0;
	int pause = 0;
	float gravity = 0.6f;
	float velocity = 0;
	int manY = 0;
	Rectangle manRectangle;
	BitmapFont font;
	int backgroundPosition = 0;

	int score = 0;
	int gameState = 0;

//génerateur auto
	Random random;

// coin
	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();

	Texture coin;
	int coinCount;

//bomb
	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();

	Texture bomb;
	int bombCount;

//Bullet

	ArrayList<Integer> bulletXs = new ArrayList<Integer>();
	ArrayList<Integer> bulletYs = new ArrayList<Integer>();
	ArrayList<Rectangle> bulletRectangles = new ArrayList<Rectangle>();

	Texture bullet;
	int bulletCount;

//Partie graphisme
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");

		manY = Gdx.graphics.getHeight() / 2;

		coin = new Texture("coin.png");
		random = new Random();
		bomb = new Texture("bomb.png");
		random = new Random();
		bullet = new Texture("bullet.png");
		random = new Random();

		dizzy = new Texture("dizzy-1.png");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(5);



	}

	//fonction Piece
	public void makeCoin() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}
	//fonction Bomb
	public void makeBomb() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	//fonction bullet
	public void makeBullet() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bulletYs.add((int)height);
		bulletXs.add(Gdx.graphics.getWidth());
	}



//partie ECRAN/DISPOSITION
//decor
	@Override
	public void render () {
		batch.begin();
		batch.draw(background, backgroundPosition,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.draw(background, backgroundPosition+Gdx.graphics.getWidth(),0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if (gameState == 1) {

			if(backgroundPosition<Gdx.graphics.getWidth()){
				backgroundPosition -=5;
			}
			if(backgroundPosition< (-Gdx.graphics.getWidth())){
				backgroundPosition = 0;
			}

			System.out.println("position image = "+backgroundPosition);



			//bullet
			if (bulletCount < 175) {
				bulletCount++;
			} else {
				bulletCount = 0;
				makeBullet();
			}

			for (int i=0;i < bulletXs.size();i++){
				batch.draw(bullet, bulletXs.get(i),bulletYs.get(i));
				bulletXs.set(i, bulletXs.get(i) - 12 );
			}

			bulletRectangles.clear();
			for (int i=0;i < bulletXs.size();i++){
				batch.draw(bullet, bulletXs.get(i),bulletYs.get(i));
				bulletXs.set(i, bulletXs.get(i) - 8 );
				bulletRectangles.add(new Rectangle(bulletXs.get(i),bulletYs.get(i), bullet.getWidth(), bullet.getHeight()));
			}


			//bomb
			if (bombCount < 100) {
				bombCount++;
			} else {
				bombCount = 0;
				makeBomb();
			}

			for (int i=0;i < bombXs.size();i++){
				batch.draw(bomb, bombXs.get(i),bombYs.get(i));
				bombXs.set(i, bombXs.get(i) - 1 );
			}

			bombRectangles.clear();
			for (int i=0;i < bombXs.size();i++){
				batch.draw(bomb, bombXs.get(i),bombYs.get(i));
				bombXs.set(i, bombXs.get(i) - 8 );
				bombRectangles.add(new Rectangle(bombXs.get(i),bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
			}


			//piece
			if (coinCount < 30) {
				coinCount++;
			} else {
				coinCount = 0;
				makeCoin();
			}

			coinRectangles.clear();
			for (int i=0;i < coinXs.size();i++){
				batch.draw(coin, coinXs.get(i),coinYs.get(i));
				coinXs.set(i, coinXs.get(i) - 8 );
				coinRectangles.add(new Rectangle(coinXs.get(i),coinYs.get(i), coin.getWidth(), coin.getHeight()));
			}

			//Saut
			if (Gdx.input.justTouched()) {
				velocity = -20;
			}
			//vitesse de l'animation du personnage
			if (pause < 3) {
				pause++;
			} else {
				pause = 0;
				if (manState < 2) {
					manState++;
				} else {
					manState = 0;
				}
			}

			velocity += gravity;
			manY -= velocity;

			if (manY <= 0) {
				manY = 0;
			}

		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		} else if (gameState == 2) {

			if (Gdx.input.justTouched()) {
				gameState = 1;
				manY = Gdx.graphics.getHeight() / 2;
				score = 0;
				velocity = 0;
				coinXs.clear();
				coinYs.clear();
				coinRectangles.clear();
				coinCount = 0;
				bombXs.clear();
				bombYs.clear();
				bombRectangles.clear();
				bombCount = 0;
				bulletXs.clear();
				bulletYs.clear();
				bulletRectangles.clear();
				bulletCount = 0;

			}
		}

		//Partie AFFICHAGE

		//gameover

		if (gameState==2) {
			batch.draw(dizzy, Gdx.graphics.getWidth() /2 - man[manState].getWidth() /2, manY);
		} else {
		batch.draw(man[manState],Gdx.graphics.getWidth() /2 - man[manState].getWidth() /2, manY);
		}
		manRectangle = new Rectangle(Gdx.graphics.getWidth() /2 - man[manState].getWidth() /2, manY, man[manState].getWidth(), man[manState].getHeight());

		for (int i=0; i < coinRectangles.size();i++) {
			if (Intersector.overlaps(manRectangle, coinRectangles.get(i))){
				score++;

				coinRectangles.remove(i);
				coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}

		for (int i=0; i < bombRectangles.size();i++) {
			if (Intersector.overlaps(manRectangle, bombRectangles.get(i))){
				gameState = 2;
			}
		}

		for (int i=0; i < bulletRectangles.size();i++) {
			if (Intersector.overlaps(manRectangle, bulletRectangles.get(i))){
				gameState = 2;
			}
		}

		font.draw(batch, String.valueOf(score),100,200);
		batch.end();

	}

//execution
	@Override
	public void dispose () {
		batch.dispose();

	}
}
