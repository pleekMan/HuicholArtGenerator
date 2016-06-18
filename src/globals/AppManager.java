package globals;

import processing.core.PImage;
import processing.core.PVector;
import processing.data.XML;
import canvas.CanvasManager;
import editor.ColorPaletteManager;
import editor.EditorManager;

public class AppManager {
	Main p5;

	static public XML settings;

	CanvasManager canvas;
	EditorManager editor;
	

	PVector canvasSize;
	//PVector viewSize; // NOT USED ??

	public static PVector canvasTranslation;
	public static float canvasScale;
	PVector translationMouseOrigin;
	PVector translationMouseOffset;

	PVector transformedCoords;
	
	PImage renderBuffer;
	int frameSaveCount;


	public AppManager() {
		p5 = getP5();

		loadSettings();
		
		canvasSize = new PVector(4096, 4096);
		//viewSize = new PVector(1024, 1024);
		canvasTranslation = new PVector(0, 0);
		canvasScale = 0.5f;

		translationMouseOrigin = new PVector();
		translationMouseOffset = new PVector();

		canvas = new CanvasManager((int) canvasSize.x, (int) canvasSize.y);
		editor = new EditorManager(canvas);

		transformedCoords = new PVector();

		renderBuffer = p5.createImage(100, 100, p5.RGB);
		frameSaveCount = 0;

	}

	private void loadSettings() {
		settings = p5.loadXML("data/settings.xml");		
		
	}

	public void update() {
		canvas.update();

	}

	public void render() {

		// CALCULATING TRANSFORMED COORDINATES FROM VIEW TO CANVAS
		PVector mouseCoords = new PVector(p5.mouseX, p5.mouseY);
		transformedCoords = viewToCanvasTransform(mouseCoords, canvasTranslation, canvasScale);

		// TRANSFORMING CANVAS RENDER
		p5.pushMatrix();
		p5.translate(canvasTranslation.x, canvasTranslation.y);
		p5.scale(canvasScale);

		canvas.render();

		p5.popMatrix();

		editor.render();
	}
	
	public void setCanvasTransforms(PVector _canvasTranslation, float _canvasScale){
		canvasTranslation.set(_canvasTranslation);
		AppManager.canvasScale = _canvasScale;
	}

	public static PVector viewToCanvasTransform(PVector viewCoords, PVector canvasTranslation, float canvasScale) {
		PVector invertedTranslation = PVector.mult(canvasTranslation, -1);
		PVector newCoords = PVector.add(viewCoords, invertedTranslation);
		newCoords.mult(1.0f / canvasScale);
		return newCoords;
	}
	
	public static PVector viewToCanvasTransform(PVector viewCoords) {
		PVector invertedTranslation = PVector.mult(canvasTranslation, -1);
		PVector newCoords = PVector.add(viewCoords, invertedTranslation);
		newCoords.mult(1.0f / canvasScale);
		return newCoords;
	}
	
	public static PVector canvasToViewTransform(PVector canvasCoords, PVector canvasTranslation, float canvasScale) {
		PVector scaledCoord = PVector.mult(canvasCoords, canvasScale);
		return PVector.add(scaledCoord, canvasTranslation);
	}
	public static PVector canvasToViewTransform(PVector canvasCoords) {
		PVector scaledCoord = PVector.mult(canvasCoords, canvasScale);
		return PVector.add(scaledCoord, canvasTranslation);
	}

	public void keyPressed(char key) {

		canvas.keyPressed(key);
		editor.keyPressed(key);

		if (key == '1') {
			PVector canvasCoords = canvas.points.get(0).position;
			PVector canvasToView = canvasToViewTransform(canvasCoords, canvasTranslation, canvasScale);
			
			p5.println("CanvasCoord: " + canvasCoords + " || CanvasToView: " + canvasToView);
			
		}
		
		if(key == 'r'){
			renderFrame(editor.getRoi());
		}
		
	}
	
	private void renderFrame(PVector[] roi){
		String frameNumber = p5.nf(frameSaveCount, 3);
		
		renderBuffer.resize((int)(roi[1].x - roi[0].x), (int)(roi[3].y - roi[0].y));
		renderBuffer = canvas.pointsLayer.get((int)roi[0].x, (int)roi[0].y, (int)(roi[1].x - roi[0].x), (int)(roi[3].y - roi[0].y));
		renderBuffer.save("renders/tests/test_" + frameNumber + ".png");
		
		frameSaveCount++;
	}

	public void mousePressed(int button) {
		canvas.mousePressed(transformedCoords);
		editor.mousePressed(button);
		
		// SET CANVAS DRAGGING FIRST COORD
		if (button == p5.RIGHT) {
			translationMouseOrigin.set(p5.mouseX, p5.mouseY);
		}
	}

	public void mouseReleased(int button) {
		//canvas.mouseReleased();
		//editor.mouseReleased(button);
	}

	public void mouseDragged(int button) {
		
		// CANVAS DRAGGING CALCULATIONS
		if (button == p5.RIGHT) {
			translationMouseOffset.set(p5.mouseX - translationMouseOrigin.x, p5.mouseY - translationMouseOrigin.y);
			canvasTranslation.add(translationMouseOffset);
			translationMouseOrigin.set(p5.mouseX, p5.mouseY);
		}
		
		editor.mouseDragged(button);
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
