package com.example.gamedev2dstarter;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;
import android.util.Log;

import com.example.androidgames.framework.Game;
import com.example.androidgames.framework.Input.TouchEvent;
import com.example.androidgames.framework.Screen;
import com.example.androidgames.framework.gl.Vertices;
import com.example.androidgames.framework.impl.GLGame;
import com.example.androidgames.framework.impl.GLGraphics;
import com.example.androidgames.framework.math.Vector2;

public class CannonGravityTest extends GLGame {
	
	@Override
	public Screen getStartScreen() {
		return new CannonScreen(this);
	}

	class CannonScreen extends Screen {
		float FRUSTRUM_WIDTH = 19.2f;
		float FRUSTRUM_HEIGHT = 12.8f;
		GLGraphics glGraphics;
		Vertices cannonVertices;
		Vertices ballVertices;
		Vector2 cannonPos = new Vector2();
		float cannonAngle = 0;
		Vector2 touchPos = new Vector2();
		Vector2 ballPos = new Vector2();
		Vector2 ballVelocity = new Vector2(0, 0);
		Vector2 gravity = new Vector2(0, -10);
		
		public CannonScreen(Game game) {
			super(game);
			glGraphics = ((GLGame) game).getGLGraphics();
			cannonVertices = new Vertices(glGraphics, 3, 0, false, false);
			cannonVertices.setVertices(new float[] {
					-0.5f,  -0.5f,
					 0.5f,   0.0f,
					-0.5f,   0.5f  }, 0, 6);		
			ballVertices = new Vertices(glGraphics, 4, 6, false, false);
			ballVertices.setVertices(new float[] {
					-0.1f,  -0.1f,
					 0.1f,  -0.1f,
					 0.1f,   0.1f,
					-0.1f,   0.1f }, 0, 8);
			ballVertices.setIndices(new short[] {
					0, 1, 2, 2, 3, 0 }, 0, 6);
			
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
				
				if (event.type == TouchEvent.TOUCH_UP) {
					float radians = cannonAngle * Vector2.TO_RADIANS;
					float ballSpeed = touchPos.len();
					ballPos.set(cannonPos);
					ballVelocity.x = FloatMath.cos(radians) * ballSpeed;
					ballVelocity.y = FloatMath.sin(radians) * ballSpeed;
				}
			}
			ballVelocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
			ballPos.add(ballVelocity.x * deltaTime, ballVelocity.y * deltaTime);
		}
		
		@Override
		public void present(float deltaTime) {
			GL10 gl = glGraphics.getGL();
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			gl.glLoadIdentity();
			
			gl.glTranslatef(cannonPos.x, cannonPos.y, 0);
			gl.glColor4f(1, 1, 1, 1);
			gl.glRotatef(cannonAngle, 0, 0, 1);
			cannonVertices.bind();
			cannonVertices.draw(GL10.GL_TRIANGLES, 0, 3);
			cannonVertices.unbind();
			
			gl.glLoadIdentity();
			gl.glTranslatef(ballPos.x, ballPos.y, 0);
			gl.glColor4f(1, 0, 0, 1);
			ballVertices.bind();
			ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
			ballVertices.unbind();
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
