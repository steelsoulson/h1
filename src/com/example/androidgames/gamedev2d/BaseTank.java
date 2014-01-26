package com.example.androidgames.gamedev2d;

import android.util.FloatMath;
import com.example.androidgames.framework.math.Vector2;

public class BaseTank extends DynamicGameObject {
	public final Vector2 direction;
	public float targetAngle = 0; 
	protected float tankSpeed = 2.0f;
	protected float towerSpeed = 50.0f;
	protected float currentAngle = 0;

	public BaseTank(float x, float y, float width, float height) {
		super(x, y, width, height);
		
		direction = new Vector2(x, y);
	}
	
	public float getCurrentAngle() {
		return currentAngle;
	}
	
	public float getSpeed() {
		return tankSpeed;
	}
	
	public void setSpeed(float speed) {
		this.tankSpeed = speed;
	}
	
	public void stop() {
		this.velocity.set(0, 0);
	}
	
	public void setDirection(Vector2 direction) {
		this.velocity.set(this.tankSpeed, 0);
		this.direction.set(direction);
		targetAngle = direction.sub(position).angle();
		velocity.set(velocity.rotate(targetAngle));
	}
	
	public void setDirection(float x, float y) {
		this.velocity.set(this.tankSpeed, 0);
		this.direction.set(x, y);
		targetAngle = this.direction.sub(position).angle();
		velocity.set(velocity.rotate(targetAngle));
	}
	
	public void update(float deltaTime) {	
		if (Math.abs(currentAngle - targetAngle) > 180.0f) {
			if (FloatMath.floor(180.0f - targetAngle + currentAngle) < 0)
				currentAngle -= towerSpeed * deltaTime;
			else
				currentAngle += towerSpeed * deltaTime;
		}
		else if (FloatMath.floor(currentAngle - targetAngle) < 0)
			currentAngle += towerSpeed * deltaTime;
		else if (FloatMath.floor(currentAngle - targetAngle) > 0)
			currentAngle -= towerSpeed * deltaTime;
		else
			position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		
		if (currentAngle > 360.0f)
			currentAngle -= 360.0f;
		else if (currentAngle < 0)
			currentAngle += 360.0f;
		
		//Log.d("BaseTank", "Angle diffs on " + FloatMath.floor(currentAngle - targetAngle));
	}

}
