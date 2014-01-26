package com.example.gamedev2dstarter;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.example.androidgames.framework.Game;
import com.example.androidgames.framework.Input.TouchEvent;
import com.example.androidgames.framework.Screen;
import com.example.androidgames.framework.gl.Camera2D;
import com.example.androidgames.framework.gl.SpriteBatcher;
import com.example.androidgames.framework.gl.Texture;
import com.example.androidgames.framework.gl.TextureRegion;
import com.example.androidgames.framework.impl.GLGame;
import com.example.androidgames.framework.impl.GLGraphics;
import com.example.androidgames.framework.math.Rectangle;
import com.example.androidgames.framework.math.Vector2;

public class TanksScreenTest extends GLGame {
	public Screen getStartScreen() {
		return new TanksScreen(this);
	}
	
	class TanksScreen extends Screen {
		float FRUSTRUM_WIDTH = 19.2f;
		float FRUSTRUM_HEIGHT = 12.8f;
		final Vector2 worldLow = new Vector2(0, 0);
		final Vector2 worldHi = new Vector2(30.0f, 30.0f);
		GLGraphics glGraphics;
		World tanksWorld;
		TextureRegion tankRegion;
		TextureRegion groundRegion;
		TextureRegion controllerRegion;
		SpriteBatcher batcher;
		Camera2D camera;
		Texture texture;
		InputController inputController;
		
		public TanksScreen(Game game) {
			super(game);
			glGraphics = ((GLGame)game ).getGLGraphics();
			tanksWorld = new World();
			batcher = new SpriteBatcher(glGraphics, 200);
			camera = new Camera2D(glGraphics, FRUSTRUM_WIDTH, FRUSTRUM_HEIGHT);
			inputController = new InputController(glGraphics, 
					new Vector2(FRUSTRUM_WIDTH/4, FRUSTRUM_HEIGHT/4), 
					1.25f, new Rectangle(0, 0, 0, 0));
			inputController.addListener(tanksWorld.tank);
		}

		@Override
		public void update(float deltaTime) {
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
			game.getInput().getKeyEvents();
			
			inputController.update(touchEvents, deltaTime, camera);
			
//			for (int i = 0; i < touchEvents.size(); i++) {
//				TouchEvent event = touchEvents.get(i);
//				camera.touchWorld(touchPos.set(event.x, event.y));
//				
////				if (TouchEvent.TOUCH_UP == event.type) {
//					inputController.update(deltaTime, touchPos, camera);
////				}
//			}
			tanksWorld.update(deltaTime);
		}

		@Override
		public void present(float deltaTime) {
			GL10 gl = glGraphics.getGL();
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			camera.setViewportAndMatrices(0, 0);
			
			/// Calculate the camera position
			float cameraPositionX = tanksWorld.updateCameraPositionX(FRUSTRUM_WIDTH);
			float cameraPositionY = tanksWorld.updateCameraPositionY(FRUSTRUM_HEIGHT);

			camera.position.set(cameraPositionX, cameraPositionY);
			
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			
			gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			
			batcher.beginBatch(texture);
			
			int numSpritesX =  (int) (FRUSTRUM_WIDTH / 2.0f + 2.0f);
			int numSpritesY =  (int) (FRUSTRUM_HEIGHT / 2.0f + 2.0f);
			float lowestX = (int)((cameraPositionX - FRUSTRUM_WIDTH/2.0f)/2.0f) * 2;
			float lowestY = (int)((cameraPositionY - FRUSTRUM_HEIGHT/2.0f)/2.0f) * 2;
//			Log.d("TanksScreenTest", "maxX = " + numSpritesX + "");
//			Log.d("TanksScreenTest", "maxY = " + numSpritesY + "");
			//float startPositionX = camera.position.x - FRUSTRUM_WIDTH/2.0f;
			//float startPositionY = camera.position.y - FRUSTRUM_HEIGHT/2.0f;
			for (int i = 0; i < numSpritesY; i++)
				for (int j = 0; j < numSpritesX; j++)
				{
					batcher.drawSprite(lowestX + 1.0f + 2.0f*j, lowestY + 1.0f + 2.0f*i, 2.0f, 2.0f, groundRegion);
				}
//			batcher.drawSprite(1.0f, 1.0f, 2.0f, 2.0f, groundRegion);
			batcher.drawSprite(tanksWorld.tank.position.x, tanksWorld.tank.position.y, 2.0f, 2.0f, 
					tanksWorld.tank.getCurrentAngle(), tankRegion);
			//batcher.drawSprite(touchPos.x, touchPos.y, 2.0f, 2.0f, tankRegion);
			
			batcher.endBatch();
			
			
			/// Draw input controller
			batcher.beginBatch(texture);
			
			gl.glColor4f(1.0f, 1.0f, 1.0f, inputController.ControllerAlpha);
			//Log.d("TanksScreenTest", "alpha = [" + inputController.ControllerAlpha + "]");
//			batcher.drawSprite(	camera.position.x - FRUSTRUM_WIDTH/2.0f + 
//							inputController.MovementCtrlPosition.x - 2.0f*inputController.MovementCtrlInnerRadius, 
//								camera.position.y - FRUSTRUM_HEIGHT/2.0f + inputController.MovementCtrlPosition.y - 
//							2.0f*inputController.MovementCtrlInnerRadius,
//							2.0f*inputController.MovementCtrlOuterRadius, 2.0f*inputController.MovementCtrlOuterRadius,
//							controllerRegion);
			batcher.drawSprite(camera.position.x - inputController.MovementCtrlPosition.x, 
						camera.position.y - inputController.MovementCtrlPosition.y, 5.0f, 5.0f, controllerRegion);
			
			batcher.endBatch();
			
			inputController.updateFade(deltaTime);
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
