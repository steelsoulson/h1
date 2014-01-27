package com.example.gamedev2dstarter;

import java.util.List;

import com.example.androidgames.framework.Input.TouchEvent;
import com.example.androidgames.framework.gl.Camera2D;
import com.example.androidgames.framework.impl.GLGraphics;
import com.example.androidgames.framework.math.Rectangle;
import com.example.androidgames.framework.math.Vector2;
import com.example.androidgames.gamedev2d.BaseTank;

public class World {
	final Vector2 worldLow = new Vector2(0, 0);
	final Vector2 worldHi = new Vector2(30, 30);
	final BaseTank tank;
	final Camera2D camera;
	final InputController inputController;
	final float FrustrumWidth;
	final float FrustrumHeight;
	
	public World(GLGraphics glGraphics, float FrustrumWidth, float FrustrumHeight) {
		tank = new BaseTank(3.0f, 3.0f, 2.0f, 2.0f);
		this.FrustrumWidth = FrustrumWidth;
		this.FrustrumHeight = FrustrumHeight;
		this.camera = new Camera2D(glGraphics, this.FrustrumWidth, this.FrustrumHeight);
		this.inputController = new InputController(glGraphics, 
					new Vector2(FrustrumWidth/4, FrustrumHeight/4), 
					1.25f, new Rectangle(0, 0, 0, 0));
		this.inputController.addListener(tank);
	}
	
	public void update(List<TouchEvent> touchEvents, float deltaTime) {
		inputController.update(touchEvents, deltaTime, camera);
		tank.update(deltaTime);
	}
	
	public void present(float deltaTime) {
		camera.setViewportAndMatrices();
		float cameraPositionX = updateCameraPositionX(FrustrumWidth);
		float cameraPositionY = updateCameraPositionY(FrustrumHeight);
		camera.position.set(cameraPositionX, cameraPositionY);		
	}
	
	private float updateCameraPositionX(float FRUSTRUM_WIDTH) {
		float cameraPositionX;
		if (tank.position.x > FRUSTRUM_WIDTH/2.0f && tank.position.x < worldHi.x - FRUSTRUM_WIDTH/2.0f)
			cameraPositionX = tank.position.x;
		else if (tank.position.x < FRUSTRUM_WIDTH/2.0f)
			cameraPositionX = FRUSTRUM_WIDTH/2.0f;
		else 
			cameraPositionX = worldHi.x - FRUSTRUM_WIDTH/2.0f;
		
		return cameraPositionX;
	}
	
	private float updateCameraPositionY(float FRUSTRUM_HEIGHT) {
		float cameraPositionY;
		if (tank.position.y > FRUSTRUM_HEIGHT/2.0f && tank.position.y < worldHi.y - FRUSTRUM_HEIGHT/2.0f)
			cameraPositionY = tank.position.y;
		else if (tank.position.y < FRUSTRUM_HEIGHT/2.0f)
			cameraPositionY = FRUSTRUM_HEIGHT/2.0f;
		else
			cameraPositionY = worldHi.y - FRUSTRUM_HEIGHT/2.0f;
		
		return cameraPositionY; 
	}
	
}
