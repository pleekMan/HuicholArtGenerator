package canvas;

import java.util.ArrayList;
import java.util.Arrays;

import editor.ColorPalette;
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
	int atStage;;

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
		
		color = p5.color(0,127,255);
		atStage = 0;

	}

	public void update() {
		for (int i = 0; i < verticesVel.length; i++) {
			verticesPos[i].add(verticesVel[i]);
		}
		atStage++;
	}
	
	public void updateWithScale(float scale) {
		for (int i = 0; i < verticesVel.length; i++) {
			verticesPos[i].add(PVector.mult(verticesVel[i],scale));
		}
	}

	public void render() {
		
		//drawLayer.fill(color);
		drawLayer.noFill();
		drawLayer.stroke(color);
		
		drawLayer.beginShape();
		
		for (int i = 0; i < verticesPos.length; i++) {
			drawLayer.vertex(verticesPos[i].x, verticesPos[i].y);

		}
		drawLayer.endShape(p5.CLOSE);
	}

	public void setPositions(PVector[] _positions) {
		// CENTER OF SOME GRID POINT
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
	
	public void setColor(int _color){
		color = _color;
	}
	
	public boolean isFinished(int _atStage) {
		return atStage == (_atStage);
	}
	
	public void restart(){
		for (int i = 0; i < verticesPos.length; i++) {
			verticesPos[i].set(startingVerticesPos[i]);
		}
		atStage = 0;

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

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}




}
