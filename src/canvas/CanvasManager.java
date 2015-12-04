package canvas;

import java.util.ArrayList;

import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class CanvasManager {

	Main p5;
	// Gridder grid;

	ArrayList<Spawner> spawners;
	static float pointSize;

	public CanvasManager() {

		p5 = getP5();

		// grid = new Gridder(40);
		// grid.setSnapAtCenter(true);

		spawners = new ArrayList<Spawner>();
		
		pointSize = 20;
	}

	public void update() {
		/*
		for (Spawner actualSpawner : spawners) {
			actualSpawner.update();
		}
		*/
	}

	public void render() {

		p5.fill(230);
		p5.noStroke();

		for (Spawner actualSpawner : spawners) {
			actualSpawner.render();
		}

		// grid.drawGrid();

		// p5.ellipse(grid.snapX(p5.mouseX), grid.snapY(p5.mouseY), 10, 10);

	}

	public void keyPressed() {
		for (Spawner actualSpawner : spawners) {
			actualSpawner.step();
		}
	}

	public void mousePressed() {

		Spawner newSpawner = new Spawner(p5.frameCount);
		newSpawner.setPosition(new PVector(p5.mouseX, p5.mouseY));
		newSpawner.spawn();
		spawners.add(newSpawner);

	}

	public void mouseReleased() {

	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
