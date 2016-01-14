package editor;

import globals.AppManager;
import globals.Main;
import globals.PAppletSingleton;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;

import canvas.CanvasManager;
import canvas.Figure;
import canvas.Point;
import processing.core.PVector;
import controlP5.*;

/*
 *  OJO..!! IF THE APP IS RUN WHEN THE ACTIVE EDITOR WINDOW IS ControlFrame.java
 * 	IT THROWS AN EXCEPTION, SINCE THAT WINDOW IS A PApplet AND ECLIPSE CREATES A NEW RUN CONFIG.
 *  ALWAYS RUN THROUGH OTHER WINDOW.
 */

public class EditorManager {
	Main p5;

	CanvasManager canvas;
	//ControlP5 controlGui;
	ControlWindow controlFrame;

	public int testColorControl;
	boolean createFigureMode;
	boolean drawDirectionsTurn;
	int lastVertexGridIdDirection;

	ArrayList<ColorPalette> colorPalettes;

	ArrayList<PVector> newFigureVertices;
	ArrayList<PVector> newFigureDirectionVectors;
	ArrayList<Point> newFigurePointsLink;
	PVector[] gridDirectionVectors; // THE SIX POSIBLE DIRECTION VECTORS THAT A POINT CAN HAVE

	PVector lastMousePosition;

	int initialVertex;

	Figure newFigure;

	public EditorManager(CanvasManager _canvas) {
		p5 = getP5();

		canvas = _canvas;
		//controlGui = new ControlP5(p5);
		controlFrame = addControlFrame("Editor Options", 300, 500);

		createFigureMode = false;
		drawDirectionsTurn = false;

		colorPalettes = new ArrayList<ColorPalette>();

		newFigureVertices = new ArrayList<PVector>();
		newFigureDirectionVectors = new ArrayList<PVector>();
		newFigurePointsLink = new ArrayList<Point>();

		initialVertex = -1;
		lastVertexGridIdDirection = 0;

		newFigure = null;

		lastMousePosition = new PVector();

		createDefaultPalette();
		createGridDirectionVectors();

	}

	public void update() {

	}

	public void render() {

		if (createFigureMode) {
			if (newFigureVertices.size() > 0) {

				// DRAW OVER SELECTED POINTS by BACKTRANSFORMING THE LINKED POINTS
				p5.fill(0, 255, 255);
				p5.stroke(0, 255, 255);
				for (int i = 0; i < newFigurePointsLink.size(); i++) {
					PVector canvasCoords = AppManager.canvasToViewTransform(newFigurePointsLink.get(i).position, AppManager.canvasTranslation, AppManager.canvasScale);

					if (i > 0) {
						PVector prevCanvasCoords = AppManager.canvasToViewTransform(newFigurePointsLink.get(i -1).position, AppManager.canvasTranslation, AppManager.canvasScale);
						p5.line(prevCanvasCoords.x, prevCanvasCoords.y, canvasCoords.x, canvasCoords.y);
					}
					p5.ellipse(canvasCoords.x, canvasCoords.y, CanvasManager.pointSize * AppManager.canvasScale * 0.25f, CanvasManager.pointSize * AppManager.canvasScale * 0.25f);

				}

			}

			/*
			p5.stroke(0,255,255);
			if (drawDirectionsTurn) {
				PVector lastVertex = newFigureVertices.get(newFigureVertices.size() - 1);
				PVector mouseVector = new PVector(p5.mouseX - lastVertex.x, p5.mouseY - lastVertex.y);
				
				float angle = PVector.angleBetween(mouseVector, gridDirectionVectors[0]);
				
				// RETURNED ANGLE GOES FROM 0 TO PI, TWICE AROUND THE CIRCLE
				if (mouseVector.y > 0) {
					lastVertexGridIdDirection = p5.floor(p5.map(angle, 0, p5.PI, 0, 3));
				} else {
					lastVertexGridIdDirection = p5.floor(p5.map(angle, p5.PI, 0, 3, 6));
				}
				
				// DRAW THIS DIRECTION LINE (KINDA CHOTO: DRAWING FROM MOUSE AND NOT FROM POINT CENTER)
				p5.pushMatrix();
				p5.translate(lastMousePosition.x, lastMousePosition.y);
				p5.rotate((p5.TWO_PI / 6) * lastVertexGridIdDirection);
				p5.line(0,0,40,0);
				
				p5.popMatrix();
				p5.text(angle + " : " + lastVertexGridIdDirection, p5.mouseX, p5.mouseY - 20);
			}
			*/

			// KEEP DRAWING THE DIRECTION LINES
			// TODO NOT QUITE WORKING
			/*
			p5.stroke(0,255,255);
			for (int i = 0; i < newFigureDirectionVectors.size(); i++) {
				p5.pushMatrix();
				p5.translate(newFigureVertices.get(i).x, newFigureVertices.get(i).y);
				p5.rotate((p5.TWO_PI / 6) * lastVertexGridIdDirection);
				p5.line(0,0,40,0);
				
				p5.popMatrix();
				//p5.line(newFigureVertices.get(i).x, newFigureVertices.get(i).y, newFigureVertices.get(i).x + newFigureDirectionVectors.get(i).x, newFigureVertices.get(i).y + newFigureDirectionVectors.get(i).y);
			}
			*/

		}

	}

	private void createGridDirectionVectors() {
		// DEFAULT HEXAGONAL GRID VECTORS
		// GET DISTANCE VECTORS TO NEIGHBOUR POINTS BY CALCULATING DISTANCE OVER THE GRID (taking the first point as reference to calculate)

		gridDirectionVectors = new PVector[6];

		Point pointZero = canvas.points.get(0);

		gridDirectionVectors[0] = new PVector(canvas.points.get(1).position.x - pointZero.position.x, 0);
		gridDirectionVectors[1] = new PVector(canvas.points.get(canvas.gridWidth).position.x - pointZero.position.x, canvas.points.get(canvas.gridWidth).position.y - pointZero.position.y);
		gridDirectionVectors[2] = new PVector(gridDirectionVectors[1].x * -1, gridDirectionVectors[1].y);
		gridDirectionVectors[3] = PVector.mult(gridDirectionVectors[0], -1);
		gridDirectionVectors[4] = PVector.mult(gridDirectionVectors[1], -1);
		gridDirectionVectors[5] = PVector.mult(gridDirectionVectors[2], -1);

		p5.println("||- GRID DIRECTION VECTORS:");
		p5.println(gridDirectionVectors);
	}

	private void createDefaultPalette() {
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

	public ControlWindow addControlFrame(String theName, int theWidth, int theHeight) {
		Frame f = new Frame(theName);
		ControlWindow p = new ControlWindow(this, theWidth, theHeight);
		f.add(p);
		p.init();
		f.setTitle(theName);
		f.setSize(p.w, p.h);
		f.setLocation(100, 100);
		f.setResizable(false);
		f.setVisible(true);
		return p;
	}

	public void keyPressed(int key) {

	}

	public void mousePressed(int button, PVector transformedCoords) {

		if (createFigureMode) {

			// SWITCH BETWEEN DRAWING THE LINES AND DRAWING THE DIRECTIONS VERCTORS
			if (drawDirectionsTurn) {

				newFigureDirectionVectors.add(gridDirectionVectors[lastVertexGridIdDirection]);
				canvas.step();

				drawDirectionsTurn = false;
			} else {
				// DETECT POINT CLICKED
				int pointClicked = -1;
				for (int i = 0; i < canvas.points.size(); i++) {
					Point actualPoint = canvas.points.get(i);
					if (actualPoint.isInside(transformedCoords.x, transformedCoords.y)) {
						pointClicked = i;

						PVector pointSelected = actualPoint.position.get();
						newFigurePointsLink.add(actualPoint);

						// CHECKING FOR INITIAL, MIDDLE AND CLOSING VERTICES
						if (newFigureVertices.size() != 0) {
							if (detectClosingVertex(pointClicked)) {
								p5.println("CLOSING FIGURE");
								//spawnNewFigure();
							} else {
								newFigureVertices.add(pointSelected);
								newFigureDirectionVectors.add(gridDirectionVectors[0]); // DEFAULT TO DIRECTION 0
							}
						} else {
							initialVertex = pointClicked;
							newFigureVertices.add(pointSelected);
							newFigureDirectionVectors.add(gridDirectionVectors[0]); // DEFAULT TO DIRECTION 0
						}

						lastMousePosition.set(p5.mouseX, p5.mouseY);
						//drawDirectionsTurn = true;

						// MOMENTARILY DRAW A CIRCLE OVER CLICKED POINT.
						p5.fill(255, 255, 0);
						p5.ellipse(p5.mouseX, p5.mouseY, CanvasManager.pointSize * AppManager.canvasScale, CanvasManager.pointSize * AppManager.canvasScale);
						p5.println("Point " + i + " Clicked");

						break;
					}

				}
			}
		}

		/*
		 * // --- INSERT A NEW FIGURE, BASED ON A COLOR PALETTE ColorPalette
		 * newPalette = new ColorPalette("PALETA " + colorPalettes.size());
		 * colorPalettes.add(newPalette);
		 * 
		 * // DETECT POINT CLICKED int pointClicked = -1; for (int i = 0; i <
		 * CanvasManager.points.size(); i++) { if
		 * (CanvasManager.points.get(i).isInside(transformedCoords.x,
		 * transformedCoords.y)) { pointClicked = i;
		 * 
		 * // MOMENTARILY DRAW A CIRCLE OVER CLICKED POINT. p5.fill(255, 255,
		 * 0); p5.ellipse(CanvasManager.points.get(pointClicked).position.x,
		 * CanvasManager.points.get(pointClicked).position.y,
		 * CanvasManager.pointSize, CanvasManager.pointSize);
		 * 
		 * break; }
		 * 
		 * }
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

	private void spawnNewFigure() {

		// ---  INSERT A NEW FIGURE, BASED ON A COLOR PALETTE

		//ColorPalette newPalette = new ColorPalette("PALETA " + colorPalettes.size());
		//colorPalettes.add(newPalette);

		// CREATE POSITION AND DIRECTION VECTORS (JUST AN HEXAGON FOR NOW)
		if (pointClicked != -1) {
			PVector[] directions = Arrays.copyOf(newFigureDirectionVectors, newFigureDirectionVectors.length);
			PVector[] positions = new PVector[6];
			for (int i = 0; i < positions.length; i++) {
				positions[i] = new PVector(points.get(pointClicked).position.x, points.get(pointClicked).position.y);
			}
			int figureCycles = 3;
			Figure newFigure = new Figure(figuresLayer);
			newFigure.initialize(positions, directions, colorPalettes.get(colorPalettes.size() - 1), figureCycles);
			figures.add(newFigure);
		}

	}

	private boolean detectClosingVertex(int pointClicked) {
		return pointClicked == initialVertex;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
