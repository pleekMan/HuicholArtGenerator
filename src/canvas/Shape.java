package canvas;

import java.util.ArrayList;
import java.util.Arrays;

import editor.ColorPalette;
import editor.EditorManager;
import processing.core.PGraphics;
import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class Shape {
	Main p5;

	PGraphics drawLayer;

	PVector[] verticesVel;
	PVector[] verticesPos;
	PVector[] startingVerticesPos;
	int color;
	int atStage;
	boolean isDrawn;

	public Shape(PGraphics _drawLayer, PVector[] _verticesPos, PVector[] _verticesVel) {
		p5 = getP5();

		drawLayer = _drawLayer;

		// COPYING THE VERTICES POSITION (VERTICES VELOCITY IS THE SAME FOR ALL SHAPES)
		verticesPos = new PVector[_verticesPos.length];
		startingVerticesPos = new PVector[_verticesPos.length];
		for (int i = 0; i < _verticesVel.length; i++) {
			verticesPos[i] = new PVector(_verticesPos[i].x, _verticesPos[i].y);
			startingVerticesPos[i] = new PVector(_verticesPos[i].x, _verticesPos[i].y);
		}
		verticesVel = _verticesVel;

		color = p5.color(0, 127, 255);
		atStage = 0;
		isDrawn = true;

	}

	public void update() {
		if (isDrawn) {
			for (int i = 0; i < verticesVel.length; i++) {
				verticesPos[i].add(verticesVel[i]);
			}
			atStage++;
		}
	}

	public void updateWithScale(float scale) {
		for (int i = 0; i < verticesVel.length; i++) {
			verticesPos[i].add(PVector.mult(verticesVel[i], scale));
		}
	}

	public void render() {
		//p5.println("Shape Render");

		//drawLayer.fill(color);
		if (isDrawn) {
			if (EditorManager.shapePointInterpolation) {
				
				drawLayer.fill(color);
				drawLayer.noStroke();
				for (int i = 0; i < verticesPos.length; i++) {
					drawLayer.ellipse(verticesPos[i].x, verticesPos[i].y, 3, 3);
				}	
			} else {
				drawLayer.noFill();
				drawLayer.stroke(color);

				drawLayer.beginShape();

				for (int i = 0; i < verticesPos.length; i++) {
					drawLayer.vertex(verticesPos[i].x, verticesPos[i].y);
				}
				drawLayer.endShape(p5.CLOSE);
			}	
		}
	}

	public void setPositions(PVector[] _positions) {
		for (int i = 0; i < verticesPos.length; i++) {
			verticesPos[i] = _positions[i];
		}
	}
	

	/*
	public void resetShape(PVector startPos){
		//RE-CODE
		for (int i = 0; i < verticesPos.length; i++) {
			verticesPos[i].set(startPos);
		}
	}
	*/

	public void setColor(int _color) {
		color = _color;
	}

	public boolean isFinished(int _atStage) {
		return atStage == (_atStage);
	}

	public void restart() {
		for (int i = 0; i < verticesPos.length; i++) {
			verticesPos[i].set(startingVerticesPos[i]);
		}
		atStage = 0;

	}

	public void setIsDrawn(boolean _state) {
		isDrawn = _state;
	}

	/*
	public void setVelocity(PVector _velocity) {
		velocity = _velocity;
	}
	*/
	/*
	public void setOrder(int _order){
		order = _order;
	}
	*/

	public PVector[] getVerticesPos() {
		return verticesPos;
	}
	public PVector[] getVerticesVel() {
		return verticesVel;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
