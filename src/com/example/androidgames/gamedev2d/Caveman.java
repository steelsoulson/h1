package com.example.androidgames.gamedev2d;

public class Caveman extends DynamicGameObject {
	public static final float WORLD_WIDTH = 4.8f;
	public static final float WORLD_HEIGHT = 3.2f;
	
	public float walkingTime = 0;
	
	public Caveman(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.position.set(	(float)Math.random() * WORLD_WIDTH, 
							(float)Math.random() * WORLD_HEIGHT);
		this.velocity.set(Math.random() > 0.5f ? -0.5f : 0.5f, 0.0f);
		this.walkingTime = (float)Math.random() * 10;
	}
	
	public void update(float deltaTime) {
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		if (position.x < 0) position.x = WORLD_WIDTH;
		if (position.x > WORLD_WIDTH) position.x = 0;
		walkingTime += deltaTime;
	}
}
