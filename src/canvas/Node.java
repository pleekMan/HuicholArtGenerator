package canvas;

import editor.ColorPalette;
import processing.core.PGraphics;
import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class Node {
	Main p5;
	PGraphics drawLayer;

	boolean isBlocker;
	boolean isEmpty;
	int ID;
	// static float size = 20;

	PVector position;
	int nodeColor;
	ColorPalette colorPalette;
	int atPaletteStep;
	int atCycle;
	int cycles;

	public Node(PVector _position, boolean _isBlocker) {
		p5 = getP5();

		position = _position;
		isBlocker = _isBlocker;
		isEmpty = true;

		nodeColor = p5.color(255, 255, 0);

		ID = 0;
		cycles = 2;
		atCycle = 0;

	}

	public void setDrawLayer(PGraphics _drawLayer) {
		drawLayer = _drawLayer;
	}

	public void step() {
		update();
	}

	public void update() {
		// atPaletteStep = atPaletteStep < colorPalette.getTotalColors() ?
		// atPaletteStep++ : 0;

		if (atCycle < cycles) {

			if (atPaletteStep < colorPalette.getColorCount() - 1) {
				atPaletteStep++;
			} else {
				atPaletteStep = 0;
				//if(!isEmpty)atCycle++;

			}

			nodeColor = colorPalette.getColor(atPaletteStep);
			
		} else {
			isEmpty = true;
			nodeColor = p5.color(50);
			atCycle = 0;
		}
	}

	public void render() {
		// drawLayer.beginDraw();

		drawLayer.fill(nodeColor);
		drawLayer.ellipse(position.x, position.y, CanvasManager.pointSize, CanvasManager.pointSize);

		//drawLayer.fill(0,0,255);
		//drawLayer.text(ID, position.x - 8, position.y + 3);

		// drawLayer.endDraw();
	}

	public void render2(int pointSize) {
		// drawLayer.beginDraw();

		drawLayer.stroke(0);
		drawLayer.fill(nodeColor);
		drawLayer.ellipse(position.x, position.y, pointSize, pointSize);

		// drawLayer.fill(200);
		// drawLayer.text(ID, position.x - 8, position.y + 3);

		// drawLayer.endDraw();
	}

	public void setID(int _id) {
		ID = _id;
	}

	public void setColor(int _color) {
		nodeColor = _color;
	}

	public void setColorPalette(ColorPalette palette) {
		colorPalette = palette;
		nodeColor = colorPalette.getColor(atPaletteStep);
	}

	public ColorPalette getPalette() {
		return colorPalette;
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

	public void setColorStep(int colorStep) {
		atPaletteStep = colorStep;
	}
	
	public void setAtCycle(int _atCycle){
		atCycle = _atCycle;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

	public void init(int colorStep, ColorPalette _palette) {
		setColorStep(colorStep);
		setColorPalette(_palette);
		isEmpty = false;

	}
}
