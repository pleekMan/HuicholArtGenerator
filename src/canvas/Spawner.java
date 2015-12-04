package canvas;

import java.util.ArrayList;

import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class Spawner {
	
	Main p5;
	
	int id;
	boolean[] directions;
	
	PVector position;
	
	ArrayList<Point> points;
	
	public Spawner(int _id){
		
		p5 = getP5();
		id = _id;
	
		directions = new boolean[6];
		for (int i = 0; i < directions.length; i++) {
			directions[i] = true;
		}
		//directions[0] = true;
		
		position = new PVector();
		
		points = new ArrayList<Point>();
	}
	
	public void spawn(){
				
		for (int i = 0; i < 6; i++) { // FIX SO THAT IT ONLY CYCLES THROUGH ACTIVE DIRECTIONS
			
			if (directions[i]) {
				Point newPoint = new Point();
				newPoint.setPosition(position);
				newPoint.setDirection(setDirectionVector(i));
				
				points.add(newPoint);
			}
		}
		
	}
	
	// TODO update() IS ONLY USED WHEN UPDATING PER FRAME 
	public void update(){
		for (Point actualPoint : points) {
			actualPoint.update();
		}
		spawn();
	}
	
	// TODO step() IS ONLY USED WHEN UPDATING PER TIME WINDOW 
	public void step(){
		update();
	}
	
	public void render(){
		for (Point actualPoint : points) {
			actualPoint.render();
		}
	}
	
	private PVector setDirectionVector(int i) {
		float radiusMultiplier = CanvasManager.pointSize;
		float angleUnit = p5.TWO_PI / directions.length;
		float x = radiusMultiplier * p5.cos(angleUnit * i);
		float y = radiusMultiplier * p5.sin(angleUnit * i);
		return new PVector(x,y,0);
	}

	public void setPosition(PVector _pos){
		position.set(_pos);
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
