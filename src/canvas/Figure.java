package canvas;

import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PGraphics;
import processing.core.PVector;
import editor.ColorPalette;
import globals.Main;
import globals.PAppletSingleton;

public class Figure {
	Main p5;

	PGraphics drawLayer;

	ArrayList<Shape> shapes;
	ColorPalette colorPalette;
	int[] initialPoints;
	// TODO WORK WITH THE initialPoints
	
	int atColorStage;
	int maxColorStages;

	int cycle;
	int maxCycles;

	//PVector startPosition;

	public Figure(PGraphics _drawLayer) {
		p5 = getP5();

		drawLayer = _drawLayer;
		shapes = new ArrayList<Shape>();

		atColorStage = 0;
		maxColorStages = -1;

	}

	public void initialize(PVector[] startingPosition, PVector[] verticesDirection, ColorPalette palette, int _maxCycles) {

		//ArrayList<PVector> startingVerticesVelocity = new ArrayList(Arrays.asList(verticesDirection));
		colorPalette = palette;
		maxColorStages = colorPalette.getColorCount();
		atColorStage = -maxColorStages; // START AT NEGATIVE SHAPE COUNT, TO SHOOT THE SHAPES INCREMENTALLY

		cycle = 2; // STARTS AT 2 CUZ IT NEEDS TO FINISH ALONG WITH atStage, WHICH STARTS AT NEGATIVE maxColorStage (FOR SHAPE SHOOTING REASONS)
		maxCycles = _maxCycles;

		for (int i = 0; i < maxColorStages; i++) {

			Shape newShape = new Shape(drawLayer, startingPosition, verticesDirection);
			//newShape.setOrder(i);
			//newShape.setPosition(startPosition); // CENTER OF SOME GRID POINT
			newShape.setColor(palette.getColor(i));

			shapes.add(newShape);
			//newShape.setVelocity(_velocity);

		}

	}

	public void update() {

		// UPDATE
		for (int i = 0; i < shapes.size(); i++) {

			// REMEMBER: THIS CONDITION IS TO TRIGGER THE SHAPES INCREMENTALLY (START AT -shapes.size() and check whether the shape is at negative stage. Then we cycle back to 0)
			if (atColorStage + (maxColorStages - i) >= 0) {
				shapes.get(i).update();
				//shapes.get(i).updateWithScale((i + 1) * 0.5f);
			}

		}

		// REMOVE THE FIRST SHAPE (ALWAYS THE OUTER ONE) IF maxCycles REACHED AND THE SHAPE IS FINISHED
		if (shapes.size() > 0) {
			if (cycle >= maxCycles && shapes.get(0).isFinished(maxColorStages)) {
				shapes.remove(0);
			}
		}

		// UPDATE FIGURE COLOR STAGE AND CYCLES
		if (atColorStage < maxColorStages) {
			atColorStage++;
		} else {
			atColorStage = 0;
			cycle++;
		}

		// MAKE THE SHAPE LOOP IT IT'S FINISHED
		for (int i = 0; i < shapes.size(); i++) {
			if (shapes.get(i).isFinished(maxColorStages)) {
				shapes.get(i).restart();
			}
		}

		p5.println("||- Cycle: " + cycle + " | At Stage: " + atColorStage);

	}

	public void render() {

		drawLayer.strokeWeight(2);
		for (int i = 0; i < shapes.size(); i++) {

			//if (atColorStage + (maxColorStages - i) >= 0) {
			shapes.get(i).render();

			drawLayer.fill(255, 255, 0);
			drawLayer.text(i, shapes.get(i).verticesPos[0].x, shapes.get(i).verticesPos[0].y);

			//}

		}
		//drawLayer.fill(colorPalette.getColor(atColorStage));
		//drawLayer.ellipse(p5.mouseX, p5.mouseY, 20, 20);
	}

	public void setColorPalette(ColorPalette palette) {
		colorPalette = palette;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

	public boolean isFinished() {
		return shapes.size() <= 0;
	}
}
