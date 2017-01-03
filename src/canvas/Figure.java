package canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.jogamp.common.util.IntIntHashMap;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import editor.ColorPalette;
import globals.Main;
import globals.PAppletSingleton;

public class Figure {
	Main p5;

	PGraphics drawLayer;

	ArrayList<Shape> shapes;
	ColorPalette colorPalette;
	public ArrayList<Point> points;
	public ArrayList<PVector> directions;

	int atColorStage;
	int maxColorStages;

	int cycle;
	int maxCycles;
	int maxCyclesInit;
	
	String name;

	//PVector startPosition;

	public Figure(PGraphics _drawLayer) {
		p5 = getP5();

		drawLayer = _drawLayer;
		shapes = new ArrayList<Shape>();

		atColorStage = 0;
		maxColorStages = -1;
		
		name ="";

	}

	public void initialize(ArrayList<Point> _pointsLink, PVector[] verticesDirections, ColorPalette palette, int _maxCycles) {

		directions = new ArrayList(Arrays.asList(verticesDirections));
		colorPalette = palette;
		maxColorStages = colorPalette.getColorCount();
		atColorStage = -maxColorStages - 1; // START AT NEGATIVE SHAPE COUNT, TO SHOOT THE SHAPES INCREMENTALLY

		cycle = 1; // STARTS AT 1 CUZ IT NEEDS TO FINISH ALONG WITH atStage, WHICH STARTS AT NEGATIVE maxColorStage (FOR SHAPE SHOOTING REASONS)
		maxCycles = _maxCycles;
		maxCyclesInit = maxCycles;

		points = new ArrayList<Point>();
		//points = new ArrayList<Point>(_pointsLink); // COPY THE ARRAYLIST (NOT THE REFERENCES/POINTERS)
		//Collections.copy(points, _pointsLink);
		
		// MAKE A COPY OF THE POINTS GIVEN, SPECIALLY THE POSITION, SINCE POINTS CAN BE MOVED AROUND AFTERWARDS
		for (int i = 0; i < _pointsLink.size(); i++) {
			PVector copiedPosition = new PVector();
			copiedPosition.set(_pointsLink.get(i).position);
			
			Point newFigurePoint = new Point(copiedPosition, _pointsLink.get(i).color, _pointsLink.get(i).drawLayer);
			points.add(newFigurePoint);
		}
		
		PVector[] startingPositions = getPointsPosition();

		for (int i = 0; i < maxColorStages; i++) {

			Shape newShape = new Shape(drawLayer, startingPositions, verticesDirections);
			//newShape.setOrder(i);
			//newShape.setPosition(startPosition); // CENTER OF SOME GRID POINT
			newShape.setColor(colorPalette.getColor(i));

			shapes.add(newShape);
			//newShape.setVelocity(_velocity);

		}

	}

	private PVector[] getPointsPosition() {
		PVector[] pointsPos = new PVector[points.size()];
		for (int i = 0; i < pointsPos.length; i++) {
			pointsPos[i] = points.get(i).position;
		}
		return pointsPos;
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

		// IF CYCLES (AND EACH SHAPE IN IT'S LAST CYCLE) ARE FINISHED, DISABLE SHAPE DRAW
		if (shapes.size() > 0 && cycle >= maxCycles) {
			for (int i = 0; i < shapes.size(); i++) {
				if (shapes.get(i).isFinished(maxColorStages)) {
					shapes.get(i).setIsDrawn(false);
				}
			}

		}

		// UPDATE FIGURE COLOR STAGE AND CYCLES
		if (atColorStage < maxColorStages - 1) {
			atColorStage++;
		} else {
			if (cycle <= maxCycles) {
				atColorStage = 0;
				cycle++;
			}
		}

		//p5.println("Cycle : " + cycle + "\tColorStage: " + atColorStage + "\tMaxColorStages: " + maxColorStages);

		// MAKE THE SHAPE LOOP IF IT'S FINISHED
		for (int i = 0; i < shapes.size(); i++) {
			if (shapes.get(i).isFinished(maxColorStages)) {
				shapes.get(i).restart();
			}
		}

		//p5.println("||- Cycle: " + cycle + " | At Stage: " + atColorStage);

	}

	public void render() {
		//p5.println("Figure Render");
		drawLayer.strokeWeight(2);
		for (int i = 0; i < shapes.size(); i++) {
			//if (atColorStage + (maxColorStages - i) >= 0) {
			shapes.get(i).render();

			// DRAW SHAPE INDEX
			//drawLayer.fill(255, 255, 0);
			//drawLayer.text(i, shapes.get(i).verticesPos[0].x, shapes.get(i).verticesPos[0].y);

			//}

		}
		//drawLayer.fill(colorPalette.getColor(atColorStage));
		//drawLayer.ellipse(p5.mouseX, p5.mouseY, 20, 20);
	}

	public void rewind() {

		atColorStage = -maxColorStages - 1; // START AT NEGATIVE SHAPE COUNT, TO SHOOT THE SHAPES INCREMENTALLY
		cycle = 1; // STARTS AT 2 CUZ IT NEEDS TO FINISH ALONG WITH atStage, WHICH STARTS AT NEGATIVE maxColorStage (FOR SHAPE SHOOTING REASONS)
		maxCycles = maxCyclesInit;

		for (int i = 0; i < shapes.size(); i++) {
			shapes.get(i).setIsDrawn(true);
			shapes.get(i).restart();
		}

	}
	
	public void setName(String _name){
		name = _name;
	}

	public void setColorPalette(ColorPalette palette) {
		colorPalette = palette;
		maxColorStages = colorPalette.getColorCount();

		rewind();

		// NEED TO RE-SIZE THE SHAPE LIST TO FIT NEW COLOR COUNT
		// CLEAR SHAPE ARRAYLIST AND RECREATE
		PVector[] startingPositions = shapes.get(0).getVerticesPos();
		PVector[] verticesDirections = shapes.get(0).getVerticesVel();
		shapes.clear();
		for (int i = 0; i < maxColorStages; i++) {
			Shape newShape = new Shape(drawLayer, startingPositions, verticesDirections);
			newShape.setColor(colorPalette.getColor(i));
			shapes.add(newShape);
		}

	}

	public boolean hasPoint(Point _point) {
		boolean hasPoint = false;
		for (int i = 0; i < points.size(); i++) {
			// CHECK OBJECT POINTER EQUALITY
			if (_point == points.get(i)) {
				hasPoint = true;
				break;
			} else {
				hasPoint = false;
			}
		}
		return hasPoint;
	}

	public void reAssignPoint(Point originalPoint, Point targetPoint) {

		for (int i = 0; i < points.size(); i++) {
			// CHECK OBJECT POINTER EQUALITY
			if (originalPoint == points.get(i)) {
				points.set(i, targetPoint);

				PVector[] startingPositions = getPointsPosition();

				for (int j = 0; j < shapes.size(); j++) {
					shapes.get(i).setPositions(startingPositions);
				}

				break;
			}
		}
	}

	/*
	public void setDrawAsPoints(boolean state){
		for (int i = 0; i < shapes.size(); i++) {
			shapes.get(i).setDrawAsPoints(state);
		}
	}
	*/

	public boolean isFinished() {
		//p5.println("At Cycle : " + cycle + " / Max Cycles: " + maxCycles);
		return cycle > maxCycles;
	}

	public PVector getPointDirectionVector(Point _point) {
		PVector pointDirection = null;
		for (int i = 0; i < points.size(); i++) {
			// CHECK OBJECT POINTER EQUALITY
			if (_point == points.get(i)) {
				pointDirection = directions.get(i);
				break;
			}
		}
		return pointDirection;
	}
	
	public void updateShapePointDirection(int pointIndex, PVector newDirection){
		directions.get(pointIndex).set(newDirection);
	}
	
	public void updateShapePointPosition(int pointIndex, PVector newPos) {
		points.get(pointIndex).position.set(newPos);
		
		for (int i = 0; i < shapes.size(); i++) {
			shapes.get(i).setVertexInitialPosition(pointIndex, newPos);
		}
		
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}


}
