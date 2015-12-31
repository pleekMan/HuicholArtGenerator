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

		pointSize = 20;

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
			newPoint.setColorPalette(getColorPaletteByName("EMPTY"));
			newPoint.setID(gridCounter);
			newTempPoint.setDrawLayer(drawLayer);
			newTempPoint.setColorPalette(getColorPaletteByName("EMPTY"));

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

	public void updateOld() {

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
				String neighbourTempPaletteName = tempPoints.get(actualNeighbour).getPalette().getName();

				if (points.get(i).atPaletteStep > 2) {
					if (pointPaletteName.equals(neighBourPaletteName)) {

					} else {
						tempPoints.get(i).init(0, points.get(actualNeighbour).getPalette());
					}
				}

				/*
				 * if (!(neighBourPaletteName.equals(pointPaletteName)) &&
				 * points.get(i).atPaletteStep < 2) { tempPoints.get(i).init(0,
				 * points.get(actualNeighbour).getPalette()); }
				 */
			}
		}

		// DONE EVALUATING
		for (int i = 0; i < points.size(); i++) {
			tempPoints.get(i).step();
			points.get(i).init(tempPoints.get(i).atPaletteStep, tempPoints.get(i).getPalette());
		}

	}

	public void update() {

		// EVALUATING ALL POINTS -- NEIGHBOURS + COPYING
		for (int i = 0; i < points.size(); i++) {

			// String pointPaletteName = points.get(i).getPalette().getName();
			// String pointTempPaletteName =
			// tempPoints.get(i).getPalette().getName();

			if (points.get(i).isEmpty) {

				// WHICH POINTS SURROUND POINT i
				int[] neighboursIndex = getNeighboursIndex(i);
				String[] neighboursPaletteName = new String[neighboursIndex.length];

				// GET NEIGHBOURS PALETTE
				for (int j = 0; j < neighboursIndex.length; j++) {
					Node actualNeighbour = points.get(neighboursIndex[j]);

						neighboursPaletteName[j] = actualNeighbour.getPalette().getName();
					
				}

				String predominantPalette = getPredominantPalette(neighboursPaletteName);
				// if (!predominantPalette.equals("EMPTY")) {
				tempPoints.get(i).init(0, getColorPaletteByName(predominantPalette));

				// }

			}
		}

		// DONE EVALUATING --> UPDATE
		for (int i = 0; i < points.size(); i++) {

			// STEP DISPLAY LAYER
			points.get(i).step();

			// TODO OJO AL PIOJO..!!! THIS WORKS BUT IT MIGHT END UP BEING A
			// WORK-AROUND
			// ONLY UPDATE DISPLAY LAYER IF TEMP LAYER IS DIFFERENT
			if (!(points.get(i).getPalette().getName().equals(tempPoints.get(i).getPalette().getName()))) {
				points.get(i).init(tempPoints.get(i).atPaletteStep, tempPoints.get(i).getPalette());
			}

			// tempPoints.get(i).setColorPalette(getColorPaletteByName("EMPTY"));

		}

	}

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

	public void step() {
		update();
	}

	/*
	 * private void expandPoint(int pointIndex) {
	 * 
	 * ColorPalette pointPalette = points.get(pointIndex).getPalette(); // LEFT
	 * points.get(pointIndex - 1).init(0, pointPalette);
	 * 
	 * }
	 */

	public void render() {

		// p5.fill(230);
		// p5.noStroke();
		drawLayer.beginDraw();
		drawLayer.background(0);
		drawLayer.noStroke();

		for (Node actualPoint : points) {
			actualPoint.render();
		}

		/*
		 * for (Node actualPoint : tempPoints) { actualPoint.render2(10); }
		 */

		drawLayer.endDraw();

		p5.image(drawLayer, 0, 0);

		// grid.drawGrid();

		// p5.ellipse(grid.snapX(p5.mouseX), grid.snapY(p5.mouseY), 10, 10);

	}

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

		// INSERT A NEW SPAWN COLOR PALETTE
		ColorPalette newPalette = new ColorPalette("PALETA " + colorPalettes.size());
		colorPalettes.add(newPalette);

		// ASSIGN TO POINT
		for (int i = 0; i < points.size(); i++) {
			Node newPoint = points.get(i);
			if (newPoint.isInside(p5.mouseX, p5.mouseY)) {
				newPoint.init(0, getColorPaletteByName("PALETA " + (colorPalettes.size() - 1))); //
				// point.setColorStep(0); //
				// point.setColorPalette(getColorPaletteByName("PALETA " +
				// (colorPalettes.size() - 1)));

				tempPoints.get(i).init(newPoint.atPaletteStep, newPoint.getPalette());

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
