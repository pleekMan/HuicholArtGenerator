package canvas;

import globals.Main;
import globals.PAppletSingleton;
import processing.core.PGraphics;
import processing.core.PVector;

public class Point {

	Main p5;

	public PGraphics drawLayer;

	public PVector position;
	public int color;
	public int id;

	public Point(PVector _pos, int _color, PGraphics _drawLayer) {
		p5 = getP5();

		drawLayer = _drawLayer;

		position = _pos;
		color = _color;

	}

	public void update() {

	}

	public void render() {

		//drawLayer.noFill();

		drawLayer.fill(color);
		drawLayer.ellipse(position.x, position.y, CanvasManager.pointSize, CanvasManager.pointSize);
		//drawLayer.fill(225,255,0);
		//drawLayer.text(id, position.x, position.y);

		/*
		if (isInside(p5.mouseX, p5.mouseY)) {
			drawLayer.fill(225,255,0);
			drawLayer.text(position.x + ":" + position.y, position.x - 25, position.y - (CanvasManager.pointSize * 0.5f));
		}
		*/
	}

	public void setColor(int _color) {
		// IF SAMPLED COLOR IS BLACK, POINT COLOR IS TRANSPARENT
		if (_color != p5.color(0)) {
			color = _color;
		} else {
			color = p5.color(255,0,0,0);
		}
	}

	public void setId(int _id) {
		id = _id;
	}

	public boolean isInside(float _x, float _y) {
		// p5.println("|| " + p5.dist(_x, _y, position.x, position.y));
		if (p5.dist(_x, _y, position.x, position.y) < (CanvasManager.pointSize * 0.5f)) {
			// p5.println("FOUND A POINT!!");
			return true;
		} else {
			return false;
		}

	}
	
	public void setPosition(PVector _pos){
		position.set(_pos);
	}
	
	public PVector getPosition(){
		return position;
	}

	public int getId() {
		return id;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
