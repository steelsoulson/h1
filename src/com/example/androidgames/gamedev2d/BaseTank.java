package com.example.androidgames.gamedev2d;

import android.util.FloatMath;

import com.example.androidgames.framework.math.Rectangle4;
import com.example.androidgames.framework.math.Vector2;

public class BaseTank extends DynamicGameObject implements IDirectionListener {
	public float targetAngle = 0; 
	protected float tankSpeed = 2.0f;
	protected float towerSpeed = 50.0f;
	protected float currentAngle = 0;
	public final Rectangle4 maxBound;

	public BaseTank(float x, float y, float width, float height) {
		super(x, y, width, height);
		maxBound = new Rectangle4();
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
		this.targetAngle = this.currentAngle;
	}
	
	@Override
	public void stopMovement() {
		this.stop();
	}
	
	@Override
	public void updateDirection(float angle) {
		this.setDirectionAngle(angle);
	}
	
	public void setDirectionAngle(float newAngle) {
		this.velocity.set(this.tankSpeed, 0);
		targetAngle = newAngle;
		velocity.set(velocity.rotate(targetAngle));
	}
	
	public void checkNewPosition(float deltaTime) {
		float lastCurrentAngle = currentAngle;
		Vector2 lastPosition = new Vector2(position);
		update(deltaTime);
		currentAngle = lastCurrentAngle;
		position.set(lastPosition);	
	}
	
	public void update(float deltaTime) {	
		if (Math.abs(currentAngle - targetAngle) > 180.0f) {
			if (FloatMath.floor(180.0f - targetAngle + currentAngle) < 0)
				currentAngle -= towerSpeed * deltaTime;
			else
				currentAngle += towerSpeed * deltaTime;
		}
		else if (FloatMath.floor(currentAngle - targetAngle) < 0) {
			currentAngle += towerSpeed * deltaTime;
		}
		else if (FloatMath.floor(currentAngle - targetAngle) > 0) {
			currentAngle -= towerSpeed * deltaTime;
		}
		else
			position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		
		if (currentAngle > 360.0f)
			currentAngle -= 360.0f;
		else if (currentAngle < 0)
			currentAngle += 360.0f;
		
		updateCoordinates();
	}
	
	private void updateCoordinates() {
		maxBound.points[0].set(-bounds.width/2.0f, -bounds.height/2.0f).rotate(currentAngle).add(position);
		maxBound.points[1].set(-bounds.width/2.0f, bounds.height/2.0f).rotate(currentAngle).add(position);
		maxBound.points[2].set(2*position.x - maxBound.points[0].x, 2*position.y - maxBound.points[0].y);
		maxBound.points[3].set(2*position.x - maxBound.points[1].x, 2*position.y - maxBound.points[1].y);	
		maxBound.updateBound();
	}

}
