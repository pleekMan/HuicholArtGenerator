package canvas;

import java.util.ArrayList;

import editor.ColorPalette;
import processing.core.PGraphics;
import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class CanvasManager {

	Main p5;
	// Gridder grid;

	PGraphics drawLayer;

	ArrayList<Node> points;
	ArrayList<Node> tempPoints;
	static float pointSize;
	int gridWidth;

	ArrayList<ColorPalette> colorPalettes;

	public CanvasManager() {

		p5 = getP5();
		drawLayer = p5.createGraphics(p5.width, p5.height, processing.core.PGraphics.P2D);

		// grid = new Gridder(40);
		// grid.setSnapAtCenter(true);

		points = new ArrayList<Node>();
		tempPoints = new ArrayList<Node>();
		// tempPoints = (ArrayList<Node>)points.clone();

		pointSize = 10;

		colorPalettes = new ArrayList<ColorPalette>();

		createDefaultPalette();
		createGrid();
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
		while (posY < drawLayer.height) {

			Node newPoint = new Node(new PVector(posX, posY), false);
			Node newTempPoint = new Node(new PVector(posX, posY), false);

			newPoint.setDrawLayer(drawLayer);
			newPoint.setColorPalette(getColorPaletteByName("DEFAULT"));
			newPoint.setID(gridCounter);
			newTempPoint.setDrawLayer(drawLayer);
			newTempPoint.setColorPalette(getColorPaletteByName("DEFAULT"));

			points.add(newPoint);
			tempPoints.add(newTempPoint);

			posX += separation;

			gridCounter++;

			if (!gridWidthCounterDone)
				gridWidthCounter++;

			if (posX > drawLayer.width) {
				offset = !offset;
				posX = offset ? startX + (separation * 0.5f) : startX;
				posY += separation;

				gridWidthCounterDone = true;
			}
		}

		gridWidth = gridWidthCounter;
		p5.println("Grid Width: " + gridWidthCounter);

	}

	public void update() {

		// EVALUATING ALL POINTS -- NEIGHBOURS + COPYING
		for (int i = 0; i < points.size(); i++) {

			String pointPaletteName = points.get(i).getPalette().getName();
			String pointTempPaletteName = tempPoints.get(i).getPalette().getName();

			// WHICH POINTS SURROUND POINT i
			int[] neighboursIndex = getNeighboursIndex(i);

			// CHECK NEIGHBOURS STATUS
			for (int j = 0; j < neighboursIndex.length; j++) {
				int actualNeighbour = neighboursIndex[j];

				String neighBourPaletteName = points.get(actualNeighbour).getPalette().getName();
				// ONLY COPY ATTRIBUTES IF THIS NEIGHBOUR'S PALETTE IS NOT THE
				// SAME AS THE INDEX POINT PALETTE (ignoring points already
				// passed to)
				if (!(neighBourPaletteName.equals(pointPaletteName)) && !neighBourPaletteName.equals(pointTempPaletteName)) {
					tempPoints.get(actualNeighbour).init(0, points.get(i).getPalette());
				}
			}
		}

		
		// DONE EVALUATING
		for (int i = 0; i < points.size(); i++) {
			tempPoints.get(i).step();
			points.get(i).init(tempPoints.get(i).atPaletteStep, tempPoints.get(i).getPalette());
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

	public void step() {
		update();
	}

	private void expandPoint(int pointIndex) {

		ColorPalette pointPalette = points.get(pointIndex).getPalette();
		// LEFT
		points.get(pointIndex - 1).init(0, pointPalette);

	}

	public void render() {

		// p5.fill(230);
		// p5.noStroke();
		drawLayer.beginDraw();
		drawLayer.background(0);
		drawLayer.noStroke();

		for (Node actualPoint : points) {
			actualPoint.render();
		}

		drawLayer.endDraw();

		p5.image(drawLayer, 0, 0);

		// grid.drawGrid();

		// p5.ellipse(grid.snapX(p5.mouseX), grid.snapY(p5.mouseY), 10, 10);

	}

	public void createDefaultPalette() {
		ColorPalette defaultPalette = new ColorPalette("DEFAULT");
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
			p5.println("||-- NO PALETTE FOUND NAMED: " + _name + ". DEFAULTING TO THE 1st ONE");
		}

		return selectedPalette;

	}

	public void keyPressed() {
		step();
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

		// INSERT A NEW SPAWN COLOR PALETTE
		ColorPalette newPalette = new ColorPalette("PALETA " + colorPalettes.size());
		colorPalettes.add(newPalette);

		// ASSIGN TO POINT
		for (int i = 0; i < points.size(); i++) {
			Node point = points.get(i);
			if (point.isInside(p5.mouseX, p5.mouseY)) {
				point.init(0, getColorPaletteByName("PALETA " + (colorPalettes.size() - 1))); //
				point.setColorStep(0); //
				point.setColorPalette(getColorPaletteByName("PALETA " +	(colorPalettes.size() - 1)));
				
				tempPoints.get(i).init(point.atPaletteStep, point.getPalette());
				
				break;
			}
		}

	}

	public void mouseReleased() {

	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
