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
	static float pointSize;

	ArrayList<ColorPalette> colorPalettes;

	public CanvasManager() {

		p5 = getP5();
		drawLayer = p5.createGraphics(p5.width, p5.height, processing.core.PGraphics.P2D);

		// grid = new Gridder(40);
		// grid.setSnapAtCenter(true);

		points = new ArrayList<Node>();

		pointSize = 30;

		colorPalettes = new ArrayList<ColorPalette>();

		createGrid();
		createDefaultPalette();
	}

	private void createGrid() {

		float startX = pointSize;
		float posX = startX;
		float posY = startX;
		float separation = pointSize * 1.2f;
		boolean offset = false;

		// START CREATING THE POINTS/NODES
		while (posY < drawLayer.height) {

			Node newPoint = new Node(new PVector(posX, posY), false);
			newPoint.setDrawLayer(drawLayer);
			newPoint.setColor(p5.color(50));
			points.add(newPoint);

			posX += separation;

			if (posX > drawLayer.width) {
				offset = !offset;
				posX = offset ? startX + (separation * 0.5f) : startX;
				posY += separation;
			}
		}

	}

	public void createDefaultPalette() {
		ColorPalette defaultPalette = new ColorPalette("DEFAULT");
		defaultPalette.eraseAllColors();
		colorPalettes.add(defaultPalette);

		for (Node point : points) {
			point.setColorPalette(defaultPalette);
		}

	}

	public void update() {

		for (int i=0; i < points.size(); i++) {
			Node actualPoint = points.get(i);
			
			if(!actualPoint.isEmpty){
				expandPoint(i);
			}

			actualPoint.step();
		}

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

		ColorPalette newPalette = new ColorPalette("PALETA " + colorPalettes.size());
		colorPalettes.add(newPalette);

		// ASSIGN TO POINT
		for (Node point : points) {
			if (point.isInside(p5.mouseX, p5.mouseY)) {
				point.init(0,getColorPaletteByName("PALETA " + (colorPalettes.size() - 1)));
				//point.setColorStep(0);
				//point.setColorPalette(getColorPaletteByName("PALETA " + (colorPalettes.size() - 1)));
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
