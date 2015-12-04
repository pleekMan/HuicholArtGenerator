package globals;
import canvas.CanvasManager;

import processing.core.*;
import controlP5.*;

public class Main extends PApplet {

	CanvasManager canvas;
	
	public void setup() {
		size(1280,720,P2D);
		
		setPAppletSingleton();
		
		canvas = new CanvasManager();
	}

	public void draw() {
		background(0);
		//drawBackLines();
		
		canvas.update();
		canvas.render();
		
		
	}

	private void drawBackLines() {
		stroke(200);
		float offset = frameCount % 40;
		for (int i = 0; i < width; i += 40) {
			line(i + offset, 0, i + offset, height);
		}

		// MOUSE POSITION
		fill(255, 0, 0);
		text("FR: " + frameRate, 20, 20);
		text("X: " + mouseX + " / Y: " + mouseY, mouseX, mouseY);

	}

	public void keyPressed() {

		canvas.keyPressed();

	}

	public void mousePressed() {
		canvas.mousePressed();
	}

	public void mouseReleased() {
		canvas.mouseReleased();
	}

	public void mouseClicked() {
	}

	public void mouseDragged() {
	}

	public void mouseMoved() {
		
	}



	

	public static void main(String args[]) {
		/*
		 * if (args.length > 0) { String memorySize = args[0]; }
		 */

		PApplet.main(new String[] { Main.class.getName() });
		//PApplet.main(new String[] { "--present","--hide-stop",Main.class.getName() }); //
		// PRESENT MODE
	}

	private void setPAppletSingleton() {
		PAppletSingleton.getInstance().setP5Applet(this);
	}

}
