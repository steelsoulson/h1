package com.example.gamedev2dstarter;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.example.androidgames.framework.Input.TouchEvent;
import com.example.androidgames.framework.gl.Camera2D;
import com.example.androidgames.framework.impl.GLGraphics;
import com.example.androidgames.framework.math.Circle;
import com.example.androidgames.framework.math.OverlapTester;
import com.example.androidgames.framework.math.Rectangle;
import com.example.androidgames.framework.math.Vector2;
import com.example.androidgames.gamedev2d.IDirectionListener;

/**
 * Input controller contains movement controller and fire action controller
 * Movement controller is a circle field. 
 * The game objects direction is defined within the finger position in the described circle area.
 * Fire action controller is a rectangular display area. If finger press on the display in the
 * described area then the corresponding action happens.
 * */
public class InputController {
	Vector2 MovementCtrlPosition;
	float MovementCtrlInnerRadius;
	float MovementCtrlOuterRadius;
	Rectangle ActionFieldArea;
	final float MinControllerAlpha = 0.1f;
	final float MaxControllerAlpha = 0.4f;
	float ControllerAlpha = MinControllerAlpha;
	float AlphaStep = 1.0f;
	GLGraphics glGraphics;
	final Circle ControllerArea = new Circle(0, 0, 0);
	final Circle stopButtonArea = new Circle(0, 0, 0);
	List<IDirectionListener> listeners = new ArrayList<IDirectionListener>();
	Vector2 touchPos = new Vector2();
	boolean fadeOut = true;
	
	public InputController(GLGraphics glGraphics, Vector2 movementCtrlPosition, 
			float innerRad, Rectangle ActionFieldArea) {
		this.glGraphics = glGraphics;
		this.MovementCtrlPosition = movementCtrlPosition;
		this.MovementCtrlInnerRadius = innerRad;
		this.MovementCtrlOuterRadius = 2 * this.MovementCtrlInnerRadius;
		this.ActionFieldArea = ActionFieldArea;
		this.ControllerArea.center.set(-this.MovementCtrlPosition.x, -this.MovementCtrlPosition.y);
		this.ControllerArea.radius = this.MovementCtrlOuterRadius;
		this.stopButtonArea.center.set(this.ControllerArea.center);
		this.stopButtonArea.radius = this.MovementCtrlInnerRadius;
	}
	
	public void addListener(IDirectionListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Get update from the main thread
	 * */
	public void update(List<TouchEvent> touchEvents, float deltaTime, Camera2D camera) {
		// 1) if finger is at controller position then set controller alpha to 1.0f;
		for (TouchEvent touchEvent : touchEvents) {
			if (touchEvent.type == TouchEvent.TOUCH_DOWN) fadeOut = false;
			camera.touchWorld(touchPos.set(touchEvent.x, touchEvent.y));
		
			Circle touchArea = new Circle(touchPos.x - camera.position.x, touchPos.y - camera.position.y, MovementCtrlInnerRadius/10.0f);
			Log.d("InputController", "touchArea [" + touchArea.center.x + " : " + touchArea.center.y + "]");
			Log.d("InputController", "ControllerArea [" + ControllerArea.center.x + " : " + ControllerArea.center.y + "]");
			if (OverlapTester.overlapCircles(touchArea, stopButtonArea)) {
				ControllerAlpha = MaxControllerAlpha;
				for (IDirectionListener listener: listeners) {
					listener.stopMovement();
				}
			}
			else if (OverlapTester.overlapCircles(touchArea, ControllerArea)) {
				ControllerAlpha = MaxControllerAlpha;
				/// calculate the angle between controller center and finger position 
				Vector2 vec = new Vector2(touchArea.center.x, touchArea.center.y);
				float angle = vec.sub(this.ControllerArea.center.x, this.ControllerArea.center.y).angle();
				Log.d("InputController", "The calculated angle is: [" + angle + "]");
				for (IDirectionListener listener : listeners) {
					listener.updateDirection(angle);
				}
			}
			if (touchEvent.type == TouchEvent.TOUCH_UP) fadeOut = true;
		}


	}
	
	public void updateFade(float deltaTime) {
		// 2) remove value of controller alpha using step alpha and delta time.
		if (fadeOut) {
			ControllerAlpha -= AlphaStep * deltaTime;
			if (ControllerAlpha < MinControllerAlpha) ControllerAlpha = MinControllerAlpha;
		}
	}
	
	
}
