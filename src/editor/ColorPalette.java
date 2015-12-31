package editor;

import globals.Main;
import globals.PAppletSingleton;

public class ColorPalette {
	
	Main p5;
	
	String name;
	
	int[] colors;
	
	public ColorPalette(String _name){
		p5 = getP5();
		
		name = _name;
		
		colors = new int[4];
		colors[0] = p5.color(255,255,0);
		colors[1] = p5.color(255,127,0);
		colors[2] = p5.color(255,0,0);
		colors[3] = p5.color(150,0,0);
		
	}
	
	public int getColor(int index){
		return colors[index];
	}
	
	public String getName(){
		return name;
	}

	public int getColorCount() {
		return colors.length;
	}
	
	public void eraseAllColors(){
		
		for (int i = 0; i < colors.length; i++) {
			colors[i] = p5.color(50);
		}
	}
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
