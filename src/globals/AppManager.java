package globals;

import processing.core.PVector;
import canvas.CanvasManager;
import editor.EditorManager;

public class AppManager {
	Main p5;

	CanvasManager canvas;
	EditorManager editor;

	PVector canvasSize;
	//PVector viewSize; // NOT USED ??

	PVector canvasTranslation;
	public static float canvasScale;
	PVector translationMouseOrigin;
	PVector translationMouseOffset;

	PVector transformedCoords;

	public AppManager() {
		p5 = getP5();

		canvasSize = new PVector(2048, 2048);
		//viewSize = new PVector(1024, 1024);
		canvasTranslation = new PVector(0, 0);
		canvasScale = 0.5f;

		translationMouseOrigin = new PVector();
		translationMouseOffset = new PVector();

		canvas = new CanvasManager((int) canvasSize.x, (int) canvasSize.y);
		editor = new EditorManager(canvas);

		transformedCoords = new PVector();

	}

	public void update() {
		//canvas.update();

	}

	public void render() {

		// CALCULATING TRANSFORMED COORDINATES FROM VIEW TO CANVAS
		PVector mouseCoords = new PVector(p5.mouseX, p5.mouseY);
		transformedCoords = getTransformedCoords(mouseCoords);

		// TRANSFORMING CANVAS RENDER
		p5.pushMatrix();
		p5.translate(canvasTranslation.x, canvasTranslation.y);
		p5.scale(canvasScale);

		canvas.render();

		p5.popMatrix();

		editor.render();
	}

	private PVector getTransformedCoords(PVector coords) {
		PVector invertedTranslation = PVector.mult(canvasTranslation, -1);
		PVector newCoords = PVector.add(coords, invertedTranslation);
		newCoords.mult(1.0f / canvasScale);

		return newCoords;
	}

	public void keyPressed(char key) {

		canvas.keyPressed(key);

	}

	public void mousePressed(int button) {
		canvas.mousePressed(transformedCoords);
		editor.mousePressed(0, transformedCoords);
		
		// SET CANVAS DRAGGING FIRST COORD
		if (button == p5.RIGHT) {
			translationMouseOrigin.set(p5.mouseX, p5.mouseY);
		}
	}

	public void mouseReleased() {
		canvas.mouseReleased();
	}

	public void mouseDragged(int button) {
		
		// CANVAS DRAGGING CALCULATIONS
		if (button == p5.RIGHT) {
			translationMouseOffset.set(p5.mouseX - translationMouseOrigin.x, p5.mouseY - translationMouseOrigin.y);
			canvasTranslation.add(translationMouseOffset);
			translationMouseOrigin.set(p5.mouseX, p5.mouseY);
		}
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
