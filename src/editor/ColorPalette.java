package editor;

import java.util.ArrayList;

import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class ColorPalette {

	Main p5;

	String name;
	PVector pos;
	PVector size;
	int selectedSwatch;

	ArrayList<Integer> colors;

	public ColorPalette(String _name) {
		p5 = getP5();

		name = _name;
		
		colors = new ArrayList<Integer>();
		
		pos = new PVector();
		size = new PVector();
		selectedSwatch = 0;
		//generateColors(p5.floor(p5.random(3.99f)));
		//generateColors(2);
	}
	
	public void render(){
		p5.pushStyle();
		
		float swatchWidth = ColorPaletteManager.paletteStripSize.x / getColorCount();

		p5.stroke(0);;
		p5.strokeWeight(3);
		p5.line(pos.x, pos.y, p5.width, pos.y);
		
		for (int i = 0; i < colors.size(); i++) {

			float posX = pos.x + (swatchWidth * i);
			
			p5.noStroke();
			p5.fill(getColor(i));
			p5.rect(posX, pos.y, swatchWidth, size.y);
			//p5.ellipse(posX, pos.y, 20, 20);
			
			if(i == selectedSwatch){
				p5.fill(0);
				p5.noStroke();
				p5.ellipse(posX + swatchWidth * 0.5f, pos.y + (size.y * 0.5f), 10,10);
			}
		}
		
		p5.popStyle();
	}
	
	public void setPosition(float x, float y){
		pos.set(x,y);
	}
	
	public void setSize(float w, float h){
		size.set(w, h);
	}
	
	public void addColors(int[] newColors){
		for (int i = 0; i < newColors.length; i++) {
			colors.add(newColors[i]);
		}
	}
	
	public void addColor(int newColor){
			colors.add(newColor);
	}
	
	public int selectSwatch() {
		int selectedColor = -1;
		
		float swatchWidth = ColorPaletteManager.paletteStripSize.x / getColorCount();

		for (int i = 0; i < colors.size(); i++) {
			float posX = pos.x + (swatchWidth * i);
			
			if(p5.mouseX > posX && p5.mouseX < (posX + swatchWidth)){
				selectedColor =  selectedSwatch = i;
				
			}				
		}
		
		return selectedColor;
	}

	public int getColor(int index) {
		return colors.get(index);
	}

	public String getName() {
		return name;
	}
	
	public ArrayList<Integer> getColors() {
		return colors;
	}

	public int getColorCount() {
		return colors.size();
	}

	public void eraseAllColors() {
		colors.clear();
	}
	
	public PVector getPosition(){
		return pos;
	}
	

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}



}
