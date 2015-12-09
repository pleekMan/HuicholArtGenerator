package canvas;

import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class Point {
	Main p5;
	
	PVector position;
	PVector direction;
	
	int atStage;
	
	public Point(){
		p5 = getP5();
		
		position = new PVector();
		direction = new PVector();
		
		atStage = 0;
		
	}
	
	public void update(){
		position.add(direction);
		//p5.println(direction);
		
		atStage++;
	}
	
	public void render(){
		
		p5.fill(255);
		p5.ellipse(position.x, position.y, CanvasManager.pointSize,CanvasManager.pointSize);
		
		p5.fill(127);
		p5.text(atStage, position.x - 3, position.y + 3);
		
	}
	
	public void setPosition(PVector _pos){
		position.set(_pos);
	}
	
	public void setDirection(PVector _dir){
		direction.set(_dir);
	}
	
	public boolean isAtStage(int stage){
		return stage == atStage;
	}
	
	public int atStage(){
		return atStage;
	}
	
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
