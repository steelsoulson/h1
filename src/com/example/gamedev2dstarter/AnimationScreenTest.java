package com.example.gamedev2dstarter;

import javax.microedition.khronos.opengles.GL10;

import com.example.androidgames.framework.Game;
import com.example.androidgames.framework.Screen;
import com.example.androidgames.framework.gl.Animation;
import com.example.androidgames.framework.gl.Camera2D;
import com.example.androidgames.framework.gl.SpriteBatcher;
import com.example.androidgames.framework.gl.Texture;
import com.example.androidgames.framework.gl.TextureRegion;
import com.example.androidgames.framework.impl.GLGame;
import com.example.androidgames.framework.impl.GLGraphics;
import com.example.androidgames.gamedev2d.Caveman;

public class AnimationScreenTest extends GLGame {
	@Override
	public Screen getStartScreen() {
		return new AnimationScreen(this);
	}
	
	class AnimationScreen extends Screen {
		static final int NUM_CAVEMEN = 10;
		GLGraphics glGraphics;
		Caveman[] cavemen;
		SpriteBatcher batcher;
		Camera2D camera;
		Texture texture;
		Animation walkAnim;
		
		public AnimationScreen(Game game) {
			super(game);
			glGraphics = ((GLGame)game ).getGLGraphics();
			cavemen = new Caveman[NUM_CAVEMEN];
			for (int i = 0; i < NUM_CAVEMEN; i++) {
				cavemen[i] = new Caveman(	(float)Math.random(), 
											(float)Math.random(), 1, 1);
			}
			batcher = new SpriteBatcher(glGraphics, NUM_CAVEMEN);
			camera = new Camera2D(glGraphics, Caveman.WORLD_WIDTH, Caveman.WORLD_HEIGHT);
		}

		@Override
		public void update(float deltaTime) {
			int len = cavemen.length;
			for (int i = 0; i < len; i++) {
				cavemen[i].update(deltaTime);
			}
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
			
			for (int i = 0; i < cavemen.length; i++) {
				Caveman caveman = cavemen[i];
				TextureRegion keyFrame = 
						walkAnim.getKeyFrame(caveman.walkingTime, Animation.ANIMATION_LOOPING);
				batcher.drawSprite(caveman.position.x, caveman.position.y, 
						caveman.velocity.x < 0 ? 1 : -1, 1, keyFrame);
			}
			batcher.endBatch();
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void resume() {
			texture = new Texture((GLGame)game, "walkanim.png");
			walkAnim = new Animation(	0.2f, 
										new TextureRegion(texture, 0, 0, 64, 64),
										new TextureRegion(texture, 64, 0, 64, 64),
										new TextureRegion(texture, 128, 0, 64, 64),
										new TextureRegion(texture, 192, 0, 64, 64));
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}
		
	}


}
