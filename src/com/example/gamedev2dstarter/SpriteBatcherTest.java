package com.example.gamedev2dstarter;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.example.androidgames.framework.Game;
import com.example.androidgames.framework.Input.TouchEvent;
import com.example.androidgames.framework.Screen;
import com.example.androidgames.framework.gl.Camera2D;
import com.example.androidgames.framework.gl.FPSCounter;
import com.example.androidgames.framework.gl.SpatialHashGrid;
import com.example.androidgames.framework.gl.SpriteBatcher;
import com.example.androidgames.framework.gl.Texture;
import com.example.androidgames.framework.gl.TextureRegion;
import com.example.androidgames.framework.impl.GLGame;
import com.example.androidgames.framework.impl.GLGraphics;
import com.example.androidgames.framework.math.OverlapTester;
import com.example.androidgames.framework.math.Vector2;
import com.example.androidgames.gamedev2d.Cannon;
import com.example.androidgames.gamedev2d.DynamicGameObject;
import com.example.androidgames.gamedev2d.GameObject;


public class SpriteBatcherTest extends GLGame {
	
	@Override
	public Screen getStartScreen() {
		return new SpriteBatcherScreen(this);
	}

	class SpriteBatcherScreen extends Screen {
		final int NUM_TARGETS = 100;
		final float WORLD_WIDTH = 9.6f;
		final float WORLD_HEIGHT = 4.8f;
		GLGraphics glGraphics;
		Cannon cannon;
		DynamicGameObject ball;
		boolean ballIsFlow = false;
		List<GameObject> targets;
		SpatialHashGrid grid;
		
		TextureRegion cannonRegion;
		TextureRegion ballRegion;
		TextureRegion bobRegion;
		SpriteBatcher batcher;
		
		Vector2 touchPos = new Vector2();
		Vector2 gravity = new Vector2(0, -10);
		
		Camera2D camera;
		Texture texture;
		
		FPSCounter fpsCounter;
		
		public SpriteBatcherScreen(Game game) {
			super(game);
			glGraphics = ((GLGame) game).getGLGraphics();
			
			cannon = new Cannon(0.0f, 0.0f, 1.0f, 0.5f);
			ball = new DynamicGameObject(0, 0, 0.2f, 0.2f);
			targets = new ArrayList<GameObject>(NUM_TARGETS);
			grid = new SpatialHashGrid(WORLD_WIDTH, WORLD_HEIGHT, 2.5f);
			for (int i = 0; i < NUM_TARGETS; i++) {
				GameObject target = new GameObject(
						(float)Math.random() * WORLD_WIDTH,
						(float)Math.random() * WORLD_HEIGHT,
						0.3f, 0.3f);
				grid.insertStaticObject(target);
				targets.add(target);
			}
			
			batcher = new SpriteBatcher(glGraphics, NUM_TARGETS + 2);
			
			camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
			fpsCounter = new FPSCounter();
		}
			
		@Override 
		public void update(float deltaTime) {
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
			game.getInput().getKeyEvents();
			
			for (int i = 0; i < touchEvents.size(); i++) {
				TouchEvent event = touchEvents.get(i);
				
				camera.touchWorld(touchPos.set(event.x, event.y));
				
				cannon.angle = touchPos.sub(cannon.position).angle();
				
				if (TouchEvent.TOUCH_UP == event.type && !ballIsFlow) {
					float radians = cannon.angle * Vector2.TO_RADIANS;
					float ballSpeed = touchPos.len() * 2;
					ball.position.set(cannon.position);
					ball.velocity.x = FloatMath.cos(radians) * ballSpeed;
					ball.velocity.y = FloatMath.sin(radians) * ballSpeed;
					ball.bounds.lowerLeft.set(
							ball.position.x - 0.1f,
							ball.position.y - 0.1f);
					ballIsFlow = true;
				}
			}
			
			if (ballIsFlow) {
				ball.velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
				ball.position.add(ball.velocity.x * deltaTime, ball.velocity.y * deltaTime);
						
				ball.bounds.lowerLeft.add(ball.velocity.x * deltaTime, ball.velocity.y * deltaTime);
			}
			
			List<GameObject> colliders = grid.getPotentialColliders(ball);
			for (int i = 0; i < colliders.size(); i++) {
				GameObject collider = colliders.get(i);
				if (OverlapTester.overlapRectangles(ball.bounds, collider.bounds)) {
					grid.removeObject(collider);
					targets.remove(collider);
					ballIsFlow = false;
					ball.position.set(cannon.position);
				}
			}
			
			if (ballIsFlow && ball.position.y <= 0) { 
				ball.position.set(cannon.position);
				ball.velocity.set(0, 0);
				ballIsFlow = false;
			}; 
			
		}
		
		@Override
		public void present(float deltaTime) {
			GL10 gl = glGraphics.getGL();			
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			camera.setViewportAndMatrices();
			
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			
			batcher.beginBatch(texture);
			
			for (int i = 0; i < targets.size(); i++) {
				GameObject target = targets.get(i);
				batcher.drawSprite(target.position.x, target.position.y, 0.5f, 0.5f, bobRegion);			
			}
			batcher.drawSprite(ball.position.x, ball.position.y, 0.2f, 0.2f, ballRegion);
			batcher.drawSprite(cannon.position.x, cannon.position.y, 1.0f, 0.5f, cannon.angle, 
					cannonRegion);
			
			batcher.endBatch();
			
			fpsCounter.logFrame();
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void resume() {
			texture = new Texture((GLGame) game, "atlas.png");
			cannonRegion = new TextureRegion(texture, 0, 0, 64, 32);
			ballRegion = new TextureRegion(texture, 0, 32, 16, 16);
			bobRegion = new TextureRegion(texture, 32, 32, 32, 32);
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}
		
	}

}


