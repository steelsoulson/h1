package com.example.gamedev2dstarter;

import com.example.androidgames.framework.Input.TouchEvent;
import com.example.androidgames.framework.impl.GLGraphics;
import com.example.androidgames.framework.math.Circle;
import com.example.androidgames.framework.math.OverlapTester;
import com.example.androidgames.framework.math.Rectangle;
import com.example.androidgames.framework.math.Vector2;

/**
 * Input controller contains movement controller and fire action controller
 * Movement controller is a circle field. 
 * The game objects direction is defined within the finger position in the described circle area.
 * Fire action controller is a rectangular display area. If finger press on the display in the
 * described area then the corresponding action happens.
 * */
public class InputController {
	Vector2 MovementCtrlPosition;
	int MovementCtrlInnerRadius;
	int MovementCtrlOuterRadius;
	Rectangle ActionFieldArea;
	final float MinControllerAlpha = 0.3f;
	final float MaxControllerAlpha = 0.8f;
	float ControllerAlpha = MinControllerAlpha;
	float AlphaStep = 1.0f;
	GLGraphics glGraphics;
	final Circle ControllerArea = new Circle(0, 0, 0);
	
	public InputController(GLGraphics glGraphics, Vector2 movementCtrlPosition, 
			int innerRad, int outerRad, Rectangle ActionFieldArea) {
		this.glGraphics = glGraphics;
		this.MovementCtrlPosition = movementCtrlPosition;
		this.MovementCtrlInnerRadius = innerRad;
		this.MovementCtrlOuterRadius = outerRad;
		this.ActionFieldArea = ActionFieldArea;
		this.ControllerArea.center.set(this.MovementCtrlPosition);
		this.ControllerArea.radius = this.MovementCtrlOuterRadius;
	}
	
	/**
	 * Get update from the main thread
	 * */
	public void update(float deltaTime, TouchEvent touchEvent) {
		// 1) if finger is at controller position then set controller alpha to 1.0f;
		Circle touchArea = new Circle((float)touchEvent.x, (float)touchEvent.y, (float)MovementCtrlOuterRadius);
		if (OverlapTester.overlapCircles(touchArea, ControllerArea)) {
			ControllerAlpha = MaxControllerAlpha;
			/// calculate the angle between controller center and finger position 
		}
		else {			
			// 2) remove value of controller alpha using step alpha and delta time.
			ControllerAlpha -= AlphaStep * deltaTime;
			if (ControllerAlpha < MinControllerAlpha) ControllerAlpha = MinControllerAlpha;
		}
	}
}
