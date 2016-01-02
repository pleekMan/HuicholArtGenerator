package canvas;

import java.util.ArrayList;

import editor.ColorPalette;
import processing.core.PGraphics;
import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class Shape {
	Main p5;

	PGraphics drawLayer;

	ArrayList<PVector> verticesVel;
	ArrayList<PVector> verticesPos;
	int color;
	//int order;

	public Shape(PGraphics _drawLayer, ArrayList<PVector> _vertices) {
		p5 = getP5();

		drawLayer = _drawLayer;

		verticesVel = _vertices; // COPYING, NOT REFERENCING (OTHERWISE ALL SHAPES IN FIGURE WILL HAVE THE SAME VERTEX VELOCITY VECTOR)
		verticesPos = new ArrayList<PVector>();
		
		int color = p5.color(0,127,255);
		//order = -1;

	}

	public void update() {
		for (int i = 0; i < verticesVel.size(); i++) {
			verticesPos.get(i).add(verticesVel.get(i));
		}
	}
	
	public void updateWithScale(float scale) {
		for (int i = 0; i < verticesVel.size(); i++) {
			verticesPos.get(i).add(PVector.mult(verticesVel.get(i),scale));
		}
	}

	public void render() {
		
		drawLayer.fill(color);
		//drawLayer.stroke(color);
		
		drawLayer.beginShape();
		
		for (int i = 0; i < verticesPos.size(); i++) {
			drawLayer.vertex(verticesPos.get(i).x, verticesPos.get(i).y);

		}
		
		drawLayer.endShape(p5.CLOSE);
	}

	public void setPosition(PVector _position) {
		// CENTER OF SOME GRID POINT
		for (int i = 0; i < verticesVel.size(); i++) {
			//PVector newPos = new PVector();
			PVector newPos = _position.get();
			newPos.set(_position);
			verticesPos.add(newPos);
		}
	}
	
	public void setColor(int _color){
		color = _color;
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

	public ArrayList<PVector> getVerticesPos() {
		return verticesPos;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

	public void setAtStage(int atStage) {
		// TODO Auto-generated method stub
		
	}
}
