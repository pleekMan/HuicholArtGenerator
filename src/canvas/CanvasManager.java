package canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import editor.ColorPalette;
import editor.EditorManager;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import globals.AppManager;
import globals.Main;
import globals.PAppletSingleton;

public class CanvasManager {

	Main p5;
	// Gridder grid;

	public PGraphics pointsLayer;
	public PGraphics figuresLayer;

	public static float pointSize; // VARIABLE CLAVE..!!! De aqui se calculan muchas cosas..!!!
	public int gridWidth;

	public ArrayList<Point> points;
	public ArrayList<Figure> figures;

	//static public int timeStep;

	public boolean isPlaying;

	PImage backImage;

	int stepCount;
	boolean clearLayer;

	//ArrayList<ColorPalette> colorPalettes;

	//PVector[] directionVectors;

	//PImage backGrid;

	public CanvasManager(int canvasWidth, int canvasHeight) {

		p5 = getP5();

		figuresLayer = p5.createGraphics(canvasWidth, canvasHeight, processing.core.PGraphics.P2D);
		pointsLayer = p5.createGraphics(canvasWidth, canvasHeight, processing.core.PGraphics.P2D);

		pointSize = 20; // 20 IS "THE ONE THAT GOES"

		points = new ArrayList<Point>();
		//colorPalettes = new ArrayList<ColorPalette>();
		figures = new ArrayList<Figure>();

		backImage = null;
		//setBackgroundImage(p5.loadImage("white_1024x.png"));

		//backGrid = p5.loadImage("grid.jpg");

		//timeStep = 1; // IT DOESN'T REALLY REFRESH THIS FAST (FRAMERATE DROPS CONSIDERABLY)
		isPlaying = false;
		clearLayer = false;

		createGrid();
		step();

		stepCount = 0;
	}

	private void createGrid() {

		float startX = pointSize;
		float posX = startX;
		float posY = startX;
		float separation = pointSize * 1.5f;
		boolean offset = false;

		int gridCounter = 0;
		int gridWidthCounter = 0;
		boolean gridWidthCounterDone = false;

		// CREATING GRID POINTS 
		while (posY < pointsLayer.height) {

			Point newPoint = new Point(new PVector(posX, posY), p5.color(50), pointsLayer);
			newPoint.setId(gridCounter);

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

	public void step() {

		//------  DRAW SHAPES LAYER - BEGIN -----------------------------------------

 		figuresLayer.beginDraw();
		figuresLayer.background(0);
		//figuresLayer.noStroke();

		if (backImage != null) {
			figuresLayer.image(backImage, 0, 0, figuresLayer.width, figuresLayer.height);
		}
		
		// DRAW NORMALLY OR CLEAR LAYER (CLEARING LAYER ON A SEPARATE FUNCTION THROWS ERROR... ¿¿¿???)
		// TODO I THINK ACCESSING figuresLayer THROUGH HERE AND INSIDE figure (PASSED OVER IN IT'S CONSTRUCTOR) IS
		// MAKING IT FREAK OUT
		if (!clearLayer) {
			for (Figure actualFigure : figures) {
				actualFigure.update();
				actualFigure.render();
			}
		} else {
			figuresLayer.fill(0);
			figuresLayer.rect(0, 0, figuresLayer.width, figuresLayer.height);
			clearLayer = false;
		}

		figuresLayer.endDraw();

		//------- DRAW SHAPES SHAPES - END

		//------  DRAW POINTS LAYER - BEGIN -----------------------------------------

		// FIRST LOAD figuresLayer PIXELS, BEFORE BEING INSIDE pointsLayer. OTHERWISE, IT FUCKS pointsLayer INNER TRANSFORMS ¿¿??
		figuresLayer.loadPixels();

		pointsLayer.beginDraw();
		pointsLayer.pushMatrix();

		pointsLayer.background(0);
		pointsLayer.noStroke();

		//pointsLayer.image(backGrid, 0,0);
		//pointsLayer.fill(255, 255, 0);
		//pointsLayer.rect(0, 0, 40, 40);

		// -- SAMPLING FIGURES LAYER TO COLOR POINTS LAYER
		//figuresLayer.loadPixels();
		for (Point actualPoint : points) {
			//actualPoint.update();

			PVector pointViewCoords = AppManager.canvasToViewTransform(actualPoint.position);

			// ONLY SAMPLE/REFRESH THE POINTS INSIDE THE VIEWPORT
			if (pointViewCoords.x < p5.width && pointViewCoords.x > 0 && pointViewCoords.y < p5.height && pointViewCoords.y > 0) {
				actualPoint.setColor(getColorAtPoint(figuresLayer.pixels, (int) actualPoint.position.x, (int) actualPoint.position.y));
				actualPoint.render();
			}
		}

		//figuresLayer.updatePixels();

		pointsLayer.popMatrix();
		
		pointsLayer.endDraw();

		//figuresLayer.updatePixels(); // INFO CANNOT DRAW FIGURES (AT THIS CLASS' render() METHOD) IF updatingPixels IS ON

		//------- DRAW POINTS LAYER - END

		// REWIND FIGURES WHEN DONE ANIMATING
		/*
		for (int i = 0; i < figures.size(); i++) {
			if (figures.get(i).isFinished()) {
				//figures.remove(i);
				figures.get(i).rewind();
			}
		}
		*/
		//p5.println("Figure Count: " + figures.size());

	}

	public void update() {
		if (isPlaying) {
			//if (p5.frameCount % timeStep == 0) {
			step();
			p5.println("Canvas Steps: " + stepCount);
			stepCount++;
			//}
		}
	}

	public void render() {

		//p5.image(figuresLayer, 0, 0);
		p5.image(pointsLayer, 0, 0);

		//showDirections();

	}

	public void rewind() {
		
		clearLayer = true;
		//step(); // TODO ERROR (CHECK AT update() )
		stepCount = 0;
		
		for (int i = 0; i < figures.size(); i++) {
			figures.get(i).rewind();
		}

	}

	public void pause() {
		isPlaying = false;
	}

	public void play() {
		isPlaying = true;
	}

	public boolean figuresAllFinished() {
		boolean allFinished = true;
		for (int i = 0; i < figures.size(); i++) {
			if (!figures.get(i).isFinished()) {
				allFinished = false;
				break;
			}
		}
		return allFinished;
	}

	/*
	public void refreshBackgrounds() {
	
		// PJOGL throws exception when attempting to access PGraphics here.... WTF..!!!
		 * 
		 * 
		// FIRST LOAD figuresLayer PIXELS, BEFORE BEING INSIDE pointsLayer. OTHERWISE, IT FUCKS pointsLayer INNER TRANSFORMS
		figuresLayer.loadPixels();

		pointsLayer.beginDraw();
		pointsLayer.pushMatrix();

		pointsLayer.background(0);
		pointsLayer.noStroke();

		//pointsLayer.image(backGrid, 0,0);
		//pointsLayer.fill(255, 255, 0);
		//pointsLayer.rect(0, 0, 40, 40);

		// -- SAMPLING FIGURES LAYER TO COLOR POINTS LAYER
		//figuresLayer.loadPixels();
		for (Point actualPoint : points) {
			//actualPoint.update();

			int pointX = (int) actualPoint.position.x;
			int pointY = (int) actualPoint.position.y;

			actualPoint.setColor(getColorAtPoint(figuresLayer.pixels, pointX, pointY));
			pointsLayer.stroke(100);

			actualPoint.render();
		}

		//figuresLayer.updatePixels();

		pointsLayer.popMatrix();
		pointsLayer.endDraw();

		//------- DRAW POINTS LAYER - END

	}
	*/

	public void addFigure(Figure _newFigure) {
		figures.add(_newFigure);
	}

	private int getColorAtPoint(int pix[], int x, int y) {
		// ACCESING PIXEL ARRAY TO SAMPLE (PGraphics.get() WAS FUCKING SLOW !!!) 
		return pix[(y * figuresLayer.width) + x];
	}

	public void drawFigureSkeleton(ArrayList<PVector> newFigureVertices, ArrayList<PVector> directions) {
		pointsLayer.beginDraw();

		pointsLayer.noFill();
		pointsLayer.stroke(255, 255, 0);

		pointsLayer.beginShape();
		for (int i = 0; i < newFigureVertices.size(); i++) {
			pointsLayer.vertex(newFigureVertices.get(i).x, newFigureVertices.get(i).y);
			//if (directions.size() != 0) {
			//	pointsLayer.line(newFigureVertices.get(i).x, newFigureVertices.get(i).y, newFigureVertices.get(i).x + directions.get(i).x, newFigureVertices.get(i).y + directions.get(i).y);
			//}
		}
		pointsLayer.endShape();

		pointsLayer.endDraw();
	}

	@Deprecated
	public void showDirections() {
		// render() IS ALREADY TRANSFORMED TO AppManager.canvasTranslation
		p5.stroke(0, 255, 255);
		for (int i = 0; i < figures.size(); i++) {
			Figure actualFigure = figures.get(i);

			for (int j = 0; j < actualFigure.points.size(); j++) {
				PVector pos = actualFigure.points.get(j).position;

				p5.line(pos.x, pos.y, pos.x + actualFigure.directions.get(j).x, pos.y + actualFigure.directions.get(j).y);

			}
		}
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

	@Deprecated
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

	public void setBackgroundImage(PImage _image) {
		backImage = _image;
	}

	/*
	 * private void expandPoint(int pointIndex) {
	 * 
	 * ColorPalette pointPalette = points.get(pointIndex).getPalette(); // LEFT
	 * points.get(pointIndex - 1).init(0, pointPalette);
	 * 
	 * }
	 */

	public void keyPressed(char key) {
		if (key == ' ') {
			step();
			//saveFrame();
		}

		if (key == 'j') {
			for (int i = 0; i < figures.size(); i++) {
				figures.get(i).rewind();
			}
		}
	}

	public void mousePressed(PVector transformedCoords) {

		// MAKE VISIBLE NEIGHBOUR POINTS
		/*
		 * for (int i = 0; i < points.size(); i++) { if
		 * (points.get(i).isInside(p5.mouseX, p5.mouseY)) {
		 * 
		 * int[] neighboursIndex = getNeighboursIndex(i);
		 * 
		 * p5.fill(0, 255, 0); for (int j = 0; j < neighboursIndex.length; j++)
		 * { p5.ellipse(points.get(neighboursIndex[j]).position.x,
		 * points.get(neighboursIndex[j]).position.y, 15, 15); } } }
		 */

		// ---  INSERT A NEW FIGURE, BASED ON A COLOR PALETTE
		/*
		 * ColorPalette newPalette = new ColorPalette("PALETA " +
		 * colorPalettes.size()); colorPalettes.add(newPalette);
		 * 
		 * // DETECT POINT CLICKED int pointClicked = -1; for (int i = 0; i <
		 * points.size(); i++) { if (points.get(i).isInside(transformedCoords.x,
		 * transformedCoords.y)) { pointClicked = i;
		 * 
		 * // MOMENTARILY DRAW A CIRCLE OVER CLICKED POINT. p5.fill(255,255,0);
		 * p5.ellipse(points.get(pointClicked).position.x,
		 * points.get(pointClicked).position.y, pointSize, pointSize);
		 * 
		 * break; }
		 * 
		 * }
		 * 
		 * 
		 * // CREATE POSITION AND DIRECTION VECTORS (JUST AN HEXAGON FOR NOW) if
		 * (pointClicked != -1) { PVector[] directions =
		 * Arrays.copyOf(directionVectors, directionVectors.length); PVector[]
		 * positions = new PVector[6]; for (int i = 0; i < positions.length;
		 * i++) { positions[i] = new
		 * PVector(points.get(pointClicked).position.x,
		 * points.get(pointClicked).position.y); } int figureCycles = 3; Figure
		 * newFigure = new Figure(figuresLayer); newFigure.initialize(positions,
		 * directions, colorPalettes.get(colorPalettes.size() - 1),
		 * figureCycles); figures.add(newFigure); }
		 */

	}

	public void mouseReleased() {

	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
