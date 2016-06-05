package editor;

import java.util.ArrayList;

import globals.Main;
import globals.PAppletSingleton;

public class ColorPaletteManager {

	Main p5;
	ArrayList<ColorPalette> palettes;
	
	public ColorPaletteManager(){
		p5 = getP5();
		
		palettes = new ArrayList<ColorPalette>();
	}
	
	public void update(){
		
	}
	
	public void render(){
		
	}
	
	public void createNewPalette(){
		
	}
	
	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
