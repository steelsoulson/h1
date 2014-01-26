package com.example.gamedev2dstarter;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.example.androidgames.framework.Game;
import com.example.androidgames.framework.Input.TouchEvent;
import com.example.androidgames.framework.Screen;
import com.example.androidgames.framework.gl.Vertices;
import com.example.androidgames.framework.impl.GLGame;
import com.example.androidgames.framework.impl.GLGraphics;
import com.example.androidgames.framework.math.Vector2;

public class CannonTest extends GLGame {
	@Override
	public Screen getStartScreen() {
		return new CannonScreen(this);
	}
	
	class CannonScreen extends Screen {
		float FRUSTRUM_WIDTH = 4.8f;
		float FRUSTRUM_HEIGHT = 3.2f;
		GLGraphics glGraphics;
		Vertices vertices;
		Vector2 cannonPos = new Vector2(2.4f, 0.5f);
		float cannonAngle = 0;
		Vector2 touchPos = new Vector2();
		
		public CannonScreen(Game game) {
			super(game);
			glGraphics = ((GLGame) game).getGLGraphics();
			vertices = new Vertices(glGraphics, 3, 0, false, false);
			vertices.setVertices(new float[] {
					-0.5f,  -0.5f,
					 0.5f,   0.0f,
					-0.5f,   0.5f  }, 0, 6);			
		}
		
		@Override
		public void update(float deltaTime) {
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
			game.getInput().getKeyEvents();
			
			for (int i = 0; i < touchEvents.size(); i++) {
				TouchEvent event = touchEvents.get(i);
				touchPos.x = (event.x / (float) glGraphics.getWidth()) * FRUSTRUM_WIDTH;
				touchPos.y = (1 - event.y / (float) glGraphics.getHeight()) * FRUSTRUM_HEIGHT;
				cannonAngle = touchPos.sub(cannonPos).angle();
				Log.d("CannonTest", "Update: x= "+touchPos.x+", y= "+touchPos.y+".");
				Log.d("CannonTest", "Update: angle= "+cannonAngle+".");
			}
		}
		
		@Override
		public void present(float deltaTime) {
			GL10 gl = glGraphics.getGL();
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			gl.glLoadIdentity();
			
			gl.glTranslatef(cannonPos.x, cannonPos.y, 0);
			gl.glRotatef(cannonAngle, 0, 0, 1);
			vertices.bind();
			vertices.draw(GL10.GL_TRIANGLES, 0, 3);
			
			vertices.unbind();
		}
		
		@Override
		public void resume() {
			GL10 gl = glGraphics.getGL();
			gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glOrthof(0, FRUSTRUM_WIDTH, 0, FRUSTRUM_HEIGHT, 1, -1);
			gl.glMatrixMode(GL10.GL_MODELVIEW);			

		}
		
		@Override
		public void pause() {
			
		}
		
		@Override
		public void dispose() {
			
		}
	}
	
}
