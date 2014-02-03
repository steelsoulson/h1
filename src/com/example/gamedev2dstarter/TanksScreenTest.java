package com.example.gamedev2dstarter;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.example.androidgames.framework.Game;
import com.example.androidgames.framework.Input.TouchEvent;
import com.example.androidgames.framework.Screen;
import com.example.androidgames.framework.gl.SpriteBatcher;
import com.example.androidgames.framework.gl.Texture;
import com.example.androidgames.framework.gl.TextureRegion;
import com.example.androidgames.framework.impl.GLGame;
import com.example.androidgames.framework.impl.GLGraphics;
import com.example.androidgames.framework.math.Rectangle;
import com.example.androidgames.gamedev2d.GameObject;

public class TanksScreenTest extends GLGame {
	public Screen getStartScreen() {
		return new TanksScreen(this);
	}
	
	class TanksScreen extends Screen {
		float FRUSTRUM_WIDTH = 19.2f;
		float FRUSTRUM_HEIGHT = 12.8f;
		private GLGraphics glGraphics;
		private World tanksWorld;
		private SpriteBatcher batcher;
		private TextureRegion controllerRegion;		
		private Texture texture;
		
		public TanksScreen(Game game) {
			super(game);
			glGraphics = ((GLGame)game ).getGLGraphics();
			tanksWorld = new World(glGraphics, FRUSTRUM_WIDTH, FRUSTRUM_HEIGHT, texture);			
			batcher = new SpriteBatcher(glGraphics, 2000);
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
			
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			
			gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			
			tanksWorld.setViewportAndMatrices();
			
			batcher.beginBatch(texture);
			
			float lowestX = tanksWorld.camera.position.x - FRUSTRUM_WIDTH/2.0f;
			float lowestY = tanksWorld.camera.position.y - FRUSTRUM_HEIGHT/2.0f;
			float widthToDraw = FRUSTRUM_WIDTH;
			float heightToDraw = FRUSTRUM_HEIGHT;
			List<GameObject> toDraw = tanksWorld.getDrawableObjects(new Rectangle(lowestX, 
					lowestY, widthToDraw, heightToDraw));
			
			Log.d("TanksScreenTest", "There are " + toDraw.size() + " objects to draw");

			for (GameObject gameObject : toDraw) {
				batcher.drawSprite(gameObject.position.x, gameObject.position.y, 
						gameObject.bounds.width, gameObject.bounds.height, tanksWorld.getRegionToDraw(gameObject));				
			} 			

			batcher.drawSprite(tanksWorld.tank.position.x, tanksWorld.tank.position.y, 2.0f, 2.0f, 
					tanksWorld.tank.getCurrentAngle(), tanksWorld.getRegionToDraw(tanksWorld.tank));
			
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
			texture = new Texture((GLGame) game, "TanksAtlasNew.png");
			tanksWorld.updateRegions(texture);
			controllerRegion = new TextureRegion(texture, 96, 0, 32, 32);
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}
		
	}


}
