package com.example.gamedev2dstarter;

import java.util.ArrayList;
import java.util.List;

import com.example.androidgames.framework.Input.TouchEvent;
import com.example.androidgames.framework.gl.Camera2D;
import com.example.androidgames.framework.gl.SpatialHashGrid;
import com.example.androidgames.framework.impl.GLGraphics;
import com.example.androidgames.framework.math.Rectangle;
import com.example.androidgames.framework.math.Vector2;
import com.example.androidgames.gamedev2d.BaseTank;
import com.example.androidgames.gamedev2d.GameObject;

public class World {
	final Vector2 worldLow = new Vector2(0, 0);
	final Vector2 worldHi = new Vector2(30, 30);
	final BaseTank tank;
	final Camera2D camera;
	final InputController inputController;
	final float FrustrumWidth;
	final float FrustrumHeight;
	SpatialHashGrid hashGrid;
	List<BoundWall> bounds = new ArrayList<BoundWall>();
	List<GameObject> floor = new ArrayList<GameObject>();
	
	public World(GLGraphics glGraphics, float FrustrumWidth, float FrustrumHeight) {
		tank = new BaseTank(3.0f, 3.0f, 2.0f, 2.0f);
		this.FrustrumWidth = FrustrumWidth;
		this.FrustrumHeight = FrustrumHeight;
		this.camera = new Camera2D(glGraphics, this.FrustrumWidth, this.FrustrumHeight);
		this.inputController = new InputController(glGraphics, 
					new Vector2(FrustrumWidth/4, FrustrumHeight/4), 
					1.25f, new Rectangle(0, 0, 0, 0));
		this.inputController.addListener(tank);
		
		/// Assumption: max cell size is 2.0 x 2.0
		hashGrid = new SpatialHashGrid(worldHi.x, worldHi.y, 2.0f);
		
		/// Make game world's bounds
		for (int i = 0; i < worldHi.x / 0.5f; i++) { // horizontal
			BoundWall bw = new BoundWall(i*1.0f + 0.5f, 0.5f, 1.0f, 1.0f); // lower
			bounds.add(bw);
			hashGrid.insertStaticObject(bw);
			bw = new BoundWall(i*1.0f, worldHi.y - 0.5f, 1.0f, 1.0f); // upper
			bounds.add(bw);
			hashGrid.insertStaticObject(bw);
		}
		
		for (int i = 0; i < worldHi.y / 0.5f - 2; i++) { // vertical without two bricks on each side
			BoundWall bw = new BoundWall(0.5f, 1.5f + i*1.0f, 1.0f, 1.0f); // left
			bounds.add(bw);
			hashGrid.insertStaticObject(bw);
			bw = new BoundWall(worldHi.x - 0.5f, 1.5f +i*1.0f, 1.0f, 1.0f); // right
			bounds.add(bw);
			hashGrid.insertStaticObject(bw);
		}	
		
		/// Make game world's ground
		for (int i = 0; i < worldHi.x / 2.0f; i++)
			for (int j = 0; j < worldHi.y / 2.0f; j++)
			{
				GameObject ground = new GameObject(1.0f + 2.0f*i, 1.0f + 2.0f*j, 2.0f, 2.0f);
				floor.add(ground);
				hashGrid.insertStaticObject(ground);
			}
	}
	
	public void update(List<TouchEvent> touchEvents, float deltaTime) {
		inputController.update(touchEvents, deltaTime, camera);
		tank.update(deltaTime);
	}
	
	public void setViewportAndMatrices() {
		camera.setViewportAndMatrices();
		float cameraPositionX = updateCameraPositionX(FrustrumWidth);
		float cameraPositionY = updateCameraPositionY(FrustrumHeight);
		camera.position.set(cameraPositionX, cameraPositionY);
		/// if frustrum overlaps something -> draw something
	}
	
	public List<GameObject> getDrawableObjects(Rectangle area) {
		List<GameObject> objectsToDraw = new ArrayList<GameObject>();
		return objectsToDraw;
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
