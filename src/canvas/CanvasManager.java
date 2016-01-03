package canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import editor.ColorPalette;
import processing.core.PGraphics;
import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class CanvasManager {

	Main p5;
	// Gridder grid;

	PGraphics pointsLayer;
	PGraphics figuresLayer;

	static float pointSize;
	int gridWidth;

	ArrayList<Point> points;
	ArrayList<Figure> figures;
	ArrayList<ColorPalette> colorPalettes;

	PVector[] directionVectors;

	public CanvasManager() {

		p5 = getP5();
		pointsLayer = p5.createGraphics(p5.width, p5.height, processing.core.PGraphics.P2D);
		figuresLayer = p5.createGraphics(p5.width, p5.height, processing.core.PGraphics.P2D);

		pointSize = 50;

		points = new ArrayList<Point>();
		colorPalettes = new ArrayList<ColorPalette>();
		figures = new ArrayList<Figure>();

		createDefaultPalette();
		createGrid();
		createDirectionVectors();
		step();
	}

	private void createDirectionVectors() {
		//  GET DISTANCE VECTORS TO NEIGHBOUR POINTS BY CALCULATING DISTANCE OVER THE GRID (taking the first point as reference to calculate)

		directionVectors = new PVector[6];

		Point pointZero = points.get(0);

		directionVectors[0] = new PVector(points.get(1).position.x - pointZero.position.x, 0);
		directionVectors[1] = new PVector(points.get(gridWidth).position.x - pointZero.position.x, points.get(gridWidth).position.y - pointZero.position.y);
		directionVectors[2] = new PVector(directionVectors[1].x * -1, directionVectors[1].y);
		directionVectors[3] = PVector.mult(directionVectors[0], -1);
		directionVectors[4] = PVector.mult(directionVectors[1], -1);
		directionVectors[5] = PVector.mult(directionVectors[2], -1);

		p5.println("||- DIRECTION VECTORS:");
		p5.println(directionVectors);
	}

	private void createGrid() {

		float startX = pointSize;
		float posX = startX;
		float posY = startX;
		float separation = pointSize * 1.2f;
		boolean offset = false;

		int gridCounter = 0;
		int gridWidthCounter = 0;
		boolean gridWidthCounterDone = false;

		// CREATING GRID POINTS FOR MAIN AND TEMP GRID
		while (posY < pointsLayer.height) {

			Point newPoint = new Point(new PVector(posX, posY), p5.color(50), pointsLayer);

			points.add(newPoint);

			posX += separation;

			gridCounter++;

			if (!gridWidthCounterDone)
				gridWidthCounter++;

			if (posX > pointsLayer.width) {
				offset = !offset;
				posX = offset ? startX + (separation * 0.5f) : startX;
				posY += separation;

				gridWidthCounterDone = true;
			}
		}

		gridWidth = gridWidthCounter;
		p5.println("||- Grid Width: " + gridWidthCounter);

	}

	public void update() {

		//------  DRAW SHAPES LAYER - BEGIN
		figuresLayer.beginDraw();
		figuresLayer.background(0);
		figuresLayer.noStroke();

		for (Figure actualFigure : figures) {
			actualFigure.update();
			actualFigure.render();
		}

		//figuresLayer.fill(255,255,0);
		//figuresLayer.ellipse(p5.mouseX, p5.mouseY, 20,20);

		figuresLayer.endDraw();

		//------- DRAW POINTS SHAPES - END

		//------  DRAW POINTS LAYER - BEGIN
		pointsLayer.beginDraw();
		//pointsLayer.background(0);
		pointsLayer.noStroke();

		figuresLayer.loadPixels();
		for (Point actualPoint : points) {
			//actualPoint.update();

			int pointX = (int) actualPoint.position.x;
			int pointY = (int) actualPoint.position.y;

			//int colorAtPoint = p5.color(0);
			//colorAtPoint = figuresLayer.get(pointX, pointY);
			actualPoint.setColor(getColorAtPoint(figuresLayer.pixels, pointX, pointY));

			actualPoint.render();
		}

		pointsLayer.endDraw();

		//------- DRAW POINTS LAYER - END

		// REMOVE FIGURES WHEN DONE ANIMATING
		for (int i = 0; i < figures.size(); i++) {
			if (figures.get(i).isFinished()) {
				figures.remove(i);
			}
		}
		p5.println("Figure Count: " + figures.size());

	}

	public void step() {
		update();
	}

	public void render() {

		p5.image(figuresLayer, 0, 0);
		p5.image(pointsLayer, 0, 0);

	}
	
	private int getColorAtPoint(int pix[], int x, int y){
		// ACCESING PIXEL ARRAY TO SAMPLE (PGraphics.get() WAS FUCKING SLOW !!!) 
		return pix[(y * figuresLayer.width) + x];
	}

	@Deprecated
	private String getPredominantPalette(String[] palettes) {

		// MAKE A LIST OF ONLY DIFFERENT PALETTES
		ArrayList<String> paletteList = new ArrayList(Arrays.asList(palettes));
		ArrayList<String> differentPalettes = new ArrayList<String>();

		differentPalettes.add(palettes[0]);

		for (int i = 0; i < palettes.length; i++) {
			for (int j = 0; j < differentPalettes.size(); j++) {
				if (!palettes[i].equals(differentPalettes.get(j))) {
					differentPalettes.add(palettes[i]);
				}
			}
		}

		// IF THERE IS A PALETTE OTHER THAN EMPTY, REMOVE "EMPTY" SO THAT WE
		// LOOK ONLY ON THE NON-EMPTY PALETTES
		boolean enableLookForPalettes = false;
		for (int i = 0; i < differentPalettes.size(); i++) {
			if (!differentPalettes.get(i).equals("EMPTY")) {
				enableLookForPalettes = true;
				break;
			}
		}

		if (enableLookForPalettes) {

			// REMOVE "EMPTY" PALETTES
			Iterator paletteIterator = differentPalettes.iterator();
			while (paletteIterator.hasNext()) {
				String name = (String) paletteIterator.next();
				if (name.equals("EMPTY")) {
					paletteIterator.remove();
				}
			}

			// MAKE A LIST OF OCURRENCES FOR EACH DIFFERENT PALETTE NAME
			int[] ocurrences = new int[differentPalettes.size()];

			for (int i = 0; i < ocurrences.length; i++) {
				ocurrences[i] = Collections.frequency(paletteList, differentPalettes.get(i));
			}

			int predominantPalette = -1;
			int predominantPaletteIndex = 0;
			for (int i = 0; i < ocurrences.length; i++) {
				if (ocurrences[i] > predominantPalette) {
					predominantPalette = ocurrences[i];
					predominantPaletteIndex = i;
				}
			}
			return differentPalettes.get(predominantPaletteIndex);
		} else {
			return "EMPTY";
		}

	}

	private int[] getNeighboursIndex(int index) {

		int[] neighbours;

		// CHECK: 1)CORNER POINTS -> 2)BORDERS -> 3)INSIDE OF GRID

		if (index == 0) {
			// FIRST POINT
			neighbours = new int[2];
			neighbours[0] = index + 1;
			neighbours[1] = index + gridWidth;
			return neighbours;

		} else if (index == (gridWidth - 1)) {
			// TOP RIGHT POINT
			neighbours = new int[3];
			neighbours[0] = index + gridWidth;
			neighbours[1] = index + gridWidth - 1;
			neighbours[2] = index - 1;
			return neighbours;

		} else if (index == points.size() - 1) {
			// BOTTOM RIGHT (LAST) POINT
			if (isEvenRow(index - (gridWidth - 1))) {
				neighbours = new int[3];
				neighbours[0] = index - 1;
				neighbours[1] = index - gridWidth - 1;
				neighbours[2] = index - gridWidth;

				return neighbours;
			} else {
				neighbours = new int[2];
				neighbours[0] = index - 1;
				neighbours[1] = index - gridWidth;
				return neighbours;
			}

		} else if (index == (points.size() - gridWidth)) {
			// BOTTOM LEFT POINT
			if (isEvenRow(index)) {
				neighbours = new int[2];
				neighbours[0] = index - gridWidth;
				neighbours[1] = index + 1;
				return neighbours;
			} else {
				neighbours = new int[3];
				neighbours[0] = index - gridWidth;
				neighbours[1] = index - gridWidth + 1;
				neighbours[2] = index + 1;
				return neighbours;
			}

		} else if (index < gridWidth) {
			// TOP BORDER

			neighbours = new int[4];
			neighbours[0] = index + 1;
			neighbours[1] = index + gridWidth;
			neighbours[2] = index + gridWidth - 1;
			neighbours[3] = index - 1;
			return neighbours;
		} else if ((index - (gridWidth - 1)) % gridWidth == 0) {
			// RIGHT BORDER

			if (isEvenRow(index - (gridWidth - 1))) {
				neighbours = new int[5];
				neighbours[0] = index + gridWidth;
				neighbours[1] = index + gridWidth - 1;
				neighbours[2] = index - 1;
				neighbours[3] = index - gridWidth - 1;
				neighbours[4] = index - gridWidth;
				return neighbours;
			} else {
				neighbours = new int[3];
				neighbours[0] = index + gridWidth;
				neighbours[1] = index - 1;
				neighbours[2] = index - gridWidth;
				return neighbours;
			}

		} else if (index > (points.size() - gridWidth)) {
			// BOTTOM BORDER
			if (isEvenRow(index)) {
				neighbours = new int[4];
				neighbours[0] = index - 1;
				neighbours[1] = index - gridWidth - 1;
				neighbours[2] = index - gridWidth;
				neighbours[3] = index + 1;
				return neighbours;
			} else {
				neighbours = new int[4];
				neighbours[0] = index - 1;
				neighbours[1] = index - gridWidth;
				neighbours[2] = index - gridWidth + 1;
				neighbours[3] = index + 1;
				return neighbours;
			}
		} else if (index % gridWidth == 0) {
			// LEFT BORDER

			if (isEvenRow(index)) {
				neighbours = new int[3];
				neighbours[0] = index - gridWidth;
				neighbours[1] = index + 1;
				neighbours[2] = index + gridWidth;
				return neighbours;
			} else {
				neighbours = new int[5];
				neighbours[0] = index - gridWidth;
				neighbours[1] = index - gridWidth + 1;
				neighbours[2] = index + 1;
				neighbours[3] = index + gridWidth + 1;
				neighbours[4] = index + gridWidth;
				return neighbours;
			}
		} else {
			// THE POINT IS INSIDE THE GRID

			if (isEvenRow(index - (index % gridWidth))) {
				neighbours = new int[6];
				neighbours[0] = index + 1;
				neighbours[1] = index + gridWidth;
				neighbours[2] = index + gridWidth - 1;
				neighbours[3] = index - 1;
				neighbours[4] = index - gridWidth - 1;
				neighbours[5] = index - gridWidth;
				return neighbours;
			} else {
				neighbours = new int[6];
				neighbours[0] = index + 1;
				neighbours[1] = index + gridWidth + 1;
				neighbours[2] = index + gridWidth;
				neighbours[3] = index - 1;
				neighbours[4] = index - gridWidth;
				neighbours[5] = index - gridWidth + 1;
				return neighbours;
			}

		}

		// return null;
	}

	private boolean isEvenRow(int i) {
		// CALCULATES IF ROW IS EVEN/ODD BASED ON INDEX OF FIRST POINT IN ROW
		// (CALCULATED PREVIOUSLY AS THE PARAMETER)
		return (i / gridWidth) % 2 == 0 ? true : false;
	}

	/*
	 * private void expandPoint(int pointIndex) {
	 * 
	 * ColorPalette pointPalette = points.get(pointIndex).getPalette(); // LEFT
	 * points.get(pointIndex - 1).init(0, pointPalette);
	 * 
	 * }
	 */

	public void createDefaultPalette() {
		ColorPalette defaultPalette = new ColorPalette("EMPTY");
		defaultPalette.eraseAllColors();
		colorPalettes.add(defaultPalette);
	}

	public ColorPalette getColorPaletteByName(String _name) {
		ColorPalette selectedPalette = null;

		for (int i = 0; i < colorPalettes.size(); i++) {
			if (colorPalettes.get(i).getName().equalsIgnoreCase(_name)) {
				selectedPalette = colorPalettes.get(i);
				break;
			}
		}

		if (selectedPalette == null) {
			selectedPalette = colorPalettes.get(0);
			p5.println("||-- NO PALETTE FOUND NAMED: " + _name + ". DEFAULTING TO EMPTY PALETTE");
		}

		return selectedPalette;

	}

	public void keyPressed(char key) {
		if (key == ' ') {
			step();
		}
	}

	public void mousePressed() {

		// MAKE VISIBLE NEIGHBOUR POINTS
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).isInside(p5.mouseX, p5.mouseY)) {

				int[] neighboursIndex = getNeighboursIndex(i);

				p5.fill(0, 255, 0);
				for (int j = 0; j < neighboursIndex.length; j++) {
					p5.ellipse(points.get(neighboursIndex[j]).position.x, points.get(neighboursIndex[j]).position.y, 15, 15);
				}
			}
		}

		// INSERT A NEW FIGURE, BASED ON A COLOR PALETTE
		ColorPalette newPalette = new ColorPalette("PALETA " + colorPalettes.size());
		colorPalettes.add(newPalette);

		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).isInside(p5.mouseX, p5.mouseY)) {

				Figure newFigure = new Figure(figuresLayer);
				newFigure.initialize(points.get(i).position, directionVectors, colorPalettes.get(colorPalettes.size() - 1));

				figures.add(newFigure);
				break;
			}
		}

		/*
		 * // ASSIGN TO POINT for (int i = 0; i < points.size(); i++) { Node
		 * newPoint = points.get(i); if (newPoint.isInside(p5.mouseX,
		 * p5.mouseY)) { newPoint.init(0, getColorPaletteByName("PALETA " +
		 * (colorPalettes.size() - 1))); // // point.setColorStep(0); // //
		 * point.setColorPalette(getColorPaletteByName("PALETA " + //
		 * (colorPalettes.size() - 1)));
		 * 
		 * tempPoints.get(i).init(newPoint.atPaletteStep,
		 * newPoint.getPalette());
		 * 
		 * break; } }
		 */

	}

	public void mouseReleased() {

	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
