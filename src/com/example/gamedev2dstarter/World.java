package com.example.gamedev2dstarter;

import com.example.androidgames.framework.math.Vector2;
import com.example.androidgames.gamedev2d.BaseTank;

public class World {
	final Vector2 worldLow = new Vector2(0, 0);
	final Vector2 worldHi = new Vector2(30, 30);
	final BaseTank tank;
	
	public World() {
		tank = new BaseTank(3.0f, 3.0f, 2.0f, 2.0f);
	}
	
	public void update(float deltaTime) {
		tank.update(deltaTime);
//		float closestDistance = deltaTime * tank.getSpeed();
//		if (OverlapTester.overlapRectangles(
//				new Rectangle(	tank.position.x - closestDistance, tank.position.y - closestDistance, 
//								2 * closestDistance, 2 * closestDistance	), 
//				new Rectangle(	tank.direction.x - closestDistance, tank.direction.y - closestDistance, 
//						2 * closestDistance, 2* closestDistance))) {
//			tank.stop();
//		}
	}
	
	public float updateCameraPositionX(float FRUSTRUM_WIDTH) {
		float cameraPositionX;
		if (tank.position.x > FRUSTRUM_WIDTH/2.0f && tank.position.x < worldHi.x - FRUSTRUM_WIDTH/2.0f)
			cameraPositionX = tank.position.x;
		else if (tank.position.x < FRUSTRUM_WIDTH/2.0f)
			cameraPositionX = FRUSTRUM_WIDTH/2.0f;
		else 
			cameraPositionX = worldHi.x - FRUSTRUM_WIDTH/2.0f;
		
		return cameraPositionX;
	}
	
	public float updateCameraPositionY(float FRUSTRUM_HEIGHT) {
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
