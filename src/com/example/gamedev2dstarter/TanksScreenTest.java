package com.example.gamedev2dstarter;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.example.androidgames.framework.Game;
import com.example.androidgames.framework.Input.TouchEvent;
import com.example.androidgames.framework.Screen;
import com.example.androidgames.framework.gl.SpriteBatcher;
import com.example.androidgames.framework.gl.Texture;
import com.example.androidgames.framework.gl.TextureRegion;
import com.example.androidgames.framework.impl.GLGame;
import com.example.androidgames.framework.impl.GLGraphics;

public class TanksScreenTest extends GLGame {
	public Screen getStartScreen() {
		return new TanksScreen(this);
	}
	
	class TanksScreen extends Screen {
		float FRUSTRUM_WIDTH = 19.2f;
		float FRUSTRUM_HEIGHT = 12.8f;
		GLGraphics glGraphics;
		World tanksWorld;
		TextureRegion tankRegion;
		TextureRegion groundRegion;
		TextureRegion controllerRegion;
		SpriteBatcher batcher;
		Texture texture;
		
		public TanksScreen(Game game) {
			super(game);
			glGraphics = ((GLGame)game ).getGLGraphics();
			tanksWorld = new World(glGraphics, FRUSTRUM_WIDTH, FRUSTRUM_HEIGHT);
			batcher = new SpriteBatcher(glGraphics, 200);
		}

		@Override
		public void update(float deltaTime) {
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
			game.getInput().getKeyEvents();

			tanksWorld.update(touchEvents, deltaTime);
		}

		@Override
		public void present(float deltaTime) {
			GL10 gl = glGraphics.getGL();
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			tanksWorld.present(deltaTime);
			
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			
			gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			
			batcher.beginBatch(texture);
			
			int numSpritesX =  (int) (FRUSTRUM_WIDTH / 2.0f + 2.0f);
			int numSpritesY =  (int) (FRUSTRUM_HEIGHT / 2.0f + 2.0f);
			float lowestX = (int)((tanksWorld.camera.position.x - FRUSTRUM_WIDTH/2.0f)/2.0f) * 2;
			float lowestY = (int)((tanksWorld.camera.position.y - FRUSTRUM_HEIGHT/2.0f)/2.0f) * 2;

			for (int i = 0; i < numSpritesY; i++)
				for (int j = 0; j < numSpritesX; j++)
				{
					batcher.drawSprite(lowestX + 1.0f + 2.0f*j, lowestY + 1.0f + 2.0f*i, 
							2.0f, 2.0f, groundRegion);
				}
			batcher.drawSprite(tanksWorld.tank.position.x, tanksWorld.tank.position.y, 2.0f, 2.0f, 
					tanksWorld.tank.getCurrentAngle(), tankRegion);
			
			batcher.endBatch();
			
			
			/// Draw input controller
			batcher.beginBatch(texture);
			
			gl.glColor4f(1.0f, 1.0f, 1.0f, tanksWorld.inputController.ControllerAlpha);

			batcher.drawSprite(tanksWorld.camera.position.x - tanksWorld.inputController.MovementCtrlPosition.x, 
						tanksWorld.camera.position.y - tanksWorld.inputController.MovementCtrlPosition.y, 
						5.0f, 5.0f, controllerRegion);
			
			batcher.endBatch();
			
			tanksWorld.inputController.updateFade(deltaTime);
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub			
		}

		@Override
		public void resume() {
			texture = new Texture((GLGame) game, "TanksAtlas.png");
			tankRegion = new TextureRegion(texture, 32, 0, 32, 32);
			groundRegion = new TextureRegion(texture, 0, 0, 32, 32);
			controllerRegion = new TextureRegion(texture, 0, 32, 32, 32);
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}
		
	}


}
