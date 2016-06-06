package globals;

import canvas.CanvasManager;

import processing.core.*;
import controlP5.*;

public class Main extends PApplet {

	//CanvasManager canvas;
	AppManager appManager;

	public void setup() {
		size(1300, 800, P2D);
		frameRate(30);
		//noSmooth();
		
		setPAppletSingleton();
		
		imageMode(CORNER);

		//canvas = new CanvasManager();
		appManager = new AppManager();
	}

	public void draw() {
		background(0);
		drawBackLines();

		appManager.update();
		appManager.render();
		
		drawMouseCoordinates();

	}

	private void drawBackLines() {
		stroke(200);
		float offset = frameCount % 40;
		for (int i = 0; i < width; i += 40) {
			line(i + offset, 0, i + offset, height);
		}

	}

	private void drawMouseCoordinates() {
		// MOUSE POSITION
		fill(255, 0, 0);
		text("FR: " + frameRate, 20, 20);
		text("X: " + mouseX + " / Y: " + mouseY, mouseX, mouseY);
	}

	public void keyPressed() {

		appManager.keyPressed(key);

	}

	public void mousePressed() {
		appManager.mousePressed(mouseButton);
	}

	public void mouseReleased() {
		appManager.mouseReleased(mouseButton);
	}

	public void mouseClicked() {
	}

	public void mouseDragged() {
		appManager.mouseDragged(mouseButton);

	}

	public void mouseMoved() {

	}

	public static void main(String args[]) {
		/*
		 * if (args.length > 0) { String memorySize = args[0]; }
		 */

		PApplet.main(new String[] { Main.class.getName() });
		// PApplet.main(new String[] {
		// "--present","--hide-stop",Main.class.getName() }); //
		// PRESENT MODE
	}

	private void setPAppletSingleton() {
		PAppletSingleton.getInstance().setP5Applet(this);
	}

}
