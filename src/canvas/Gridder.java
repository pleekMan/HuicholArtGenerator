package canvas;
import globals.Main;
import globals.PAppletSingleton;

import java.awt.Rectangle;

public class Gridder {

	Main p5;

	boolean enableSnapping;
	boolean enableViewGrid;
	boolean snapAtModuleCenter;
	int moduleSize;

	float pointLoc;

	float pointX;
	float pointY;

	Rectangle boundingBox;

	public Gridder(int modSize) {

		//p5.registerDraw(this);
		
		p5 = getP5();

		enableSnapping = true;
		enableViewGrid = true;
		snapAtModuleCenter = false;

		pointX = 0;
		pointY = 0;

		moduleSize = modSize;

		boundingBox = new Rectangle(0, 0, p5.width, p5.height); // DEFAULT BOUNDING BOX SET TO WINDOW SIZE

	}

	// ESTE DRAW, COMO ES UN registerDraw, TIENE Q SER PUBLIC SI O SI
	// ES LLAMADO AL FINAL DEL DRAW DEL MAIN PAPPLET
	/*
	public void draw() {

		if (enableViewGrid) {
			drawGrid();
		}
	}
	*/

	public float snapX(float valueX) {
		if (enableSnapping) {
			if (isInsideBounds()) {
				pointX = constrainValue(valueX);
				return pointX;
			} else {
				return pointX;
			}
		} else {
			pointX = valueX;
			return pointX;
		}
	}

	public float snapY(float valueY) {
		if (enableSnapping) {
			if (isInsideBounds()) {
				pointY = constrainValue(valueY);
				return pointY;
			} else {
				return pointY;
			}
		} else {
			pointY = valueY;
			return pointY;
		}

	}

	public float snap(float value) {

		if (isInsideBounds()) {
			value = value + (moduleSize / 2); // ADD boxSize HALF TO DIVIDE
												// CONSTRAIN AT HALF OF THE BOX
			pointLoc = p5.floor(value / moduleSize) * moduleSize;
			return pointLoc;
		} else {
			return pointLoc;
		}

	}

	private float constrainValue(float value) {
		if (snapAtModuleCenter) {
			// ADD boxSize HALF TO DIVIDE CONSTRAIN AT HALF OF THE BOX
			value = value + (moduleSize / 2);
		}
		value = p5.floor(value / moduleSize) * moduleSize;
		return value;
	}

	// SET GRID BOUNDING BOX ON CORNERS COORDINATES
	public void setBoundingBox(float x1, float y1, float x2, float y2) {
		boundingBox.setLocation((int) x1, (int) y1);
		boundingBox.setSize((int) x2 - (int) x1, (int) y2 - (int) y1);
	}

	// SET GRID BOUNDING BOX ON CORNER COORDINATE AND SIZE
	public void setBoundingBox(float x1, float y1, int width, int height) {
		boundingBox.setLocation((int) x1, (int) y1);
		boundingBox.setSize(width, height);
	}

	public boolean isInsideBounds() {
		if (boundingBox.contains(p5.mouseX, p5.mouseY)) {
			return true;
		} else {
			return false;
		}
	}

	public void toggleGrid() {
		enableViewGrid = !enableViewGrid;
	}

	public void setSnapAtCenter(boolean value) {
		snapAtModuleCenter = value;
	}

	public void toggleSnapAtCenter() {
		snapAtModuleCenter = !snapAtModuleCenter;
	}

	public void drawGrid() {

		p5.noFill();
		p5.stroke(50);
		p5.strokeWeight(1);

		float originX = boundingBox.x;
		float originY = boundingBox.y;

		int lineCountX = ((boundingBox.width) / moduleSize);
		int lineCountY = ((boundingBox.height) / moduleSize);

		for (int i = 0; i <= lineCountX; i++) {

			p5.line(originX + (moduleSize * i), originY, originX + (moduleSize * i), originY + boundingBox.height);

			for (int j = 0; j <= lineCountY; j++) {

				p5.line(originX, originY + (moduleSize * j), originX + boundingBox.width, originY + (moduleSize * j));
			}
		}

		p5.stroke(250);
		p5.rect(originX, originY, boundingBox.width, boundingBox.height);
	}

	public void toggleSnapping() {
		enableSnapping = !enableSnapping;
	}
	
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
