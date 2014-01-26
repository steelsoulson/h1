package com.example.androidgames.framework.impl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.androidgames.framework.Audio;
import com.example.androidgames.framework.FileIO;
import com.example.androidgames.framework.Game;
import com.example.androidgames.framework.Graphics;
import com.example.androidgames.framework.Input;
import com.example.androidgames.framework.Screen;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

public abstract class GLGame extends Activity implements Game, Renderer{
	enum GLGameState {
		Initialized,
		Running,
		Paused,
		Finished,
		Idle
	}
	
	GLSurfaceView glView;
	GLGraphics glGraphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	GLGameState gameState = GLGameState.Initialized;
	Object stateChanged = new Object();
	long startTime = System.nanoTime();
	WakeLock wakeLock;
	
	@Override
	public void onCreate(Bundle savedInstaceState) {
		super.onCreate(savedInstaceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		glView = new GLSurfaceView(this);
		glView.setRenderer(this);
		setContentView(glView);
		
		glGraphics = new GLGraphics(glView);
		fileIO = new AndroidFileIO(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, glView, 1, 1);
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		glView.onResume();
		wakeLock.acquire();
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		GLGameState state = null;
		
		synchronized (stateChanged) {
			state = this.gameState;
		}
		
		if (state == GLGameState.Running) {
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();
			
			screen.update(deltaTime);
			screen.present(deltaTime);
		}
		
		if (state == GLGameState.Paused) {
			screen.pause();
			synchronized (stateChanged) {
				this.gameState = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}
		
		if (state == GLGameState.Finished) {
			screen.pause();
			screen.dispose();
			synchronized (stateChanged) {
				this.gameState = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		glGraphics.setGL(gl);
		
		synchronized (stateChanged) {
			if (gameState == GLGameState.Initialized)
				screen = getStartScreen();
			gameState = GLGameState.Running;
			screen.resume();
			startTime = System.nanoTime();
		}
	}
	
	@Override
	public void onPause() {
		synchronized (stateChanged) {
			if (isFinishing()) 
				gameState = GLGameState.Finished;
			else
				gameState = GLGameState.Paused;
			
			while (true) {
				try {
					stateChanged.wait();
					break;
				} catch (InterruptedException ex) {
					
				}					
			}			
		}
		wakeLock.release();
		glView.onPause();
		super.onPause();
	}
	
	public GLGraphics getGLGraphics() {
		return glGraphics;
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Graphics getGraphics() {
		throw new IllegalStateException("We are using OpenGL!");
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public void setScreen(Screen screen) {
		if (screen == null) {
			throw new IllegalArgumentException("Screen must not be null");
		}
		
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;		
	}

	@Override
	public Screen getCurrentScreen() {
		return screen;
	}

}
