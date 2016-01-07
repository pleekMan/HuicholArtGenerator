package globals;

import canvas.CanvasManager;
import editor.EditorManager;

public class AppManager {
	Main p5;

	CanvasManager canvas;
	EditorManager editor;

	public AppManager() {
		p5 = getP5();
		
		canvas = new CanvasManager();
		editor = new EditorManager();

	}

	public void update() {
		//canvas.update();

	}

	public void render() {

		canvas.render();
		editor.render();
	}
	
	public void keyPressed(char key) {

		canvas.keyPressed(key);

	}

	public void mousePressed() {
		canvas.mousePressed();
	}

	public void mouseReleased() {
		canvas.mouseReleased();
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}
}
