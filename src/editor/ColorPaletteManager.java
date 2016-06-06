package editor;

import java.util.ArrayList;

import processing.core.PVector;
import globals.Main;
import globals.PAppletSingleton;

public class ColorPaletteManager {

	Main p5;
	ArrayList<ColorPalette> palettes;
	int selectedPalette;
	int selectedSwatchInPalette;

	PVector pos;
	PVector size;
	public static PVector paletteStripSize; // SIZE OF THE COLOR GRADIENT DISPLAY

	public boolean pickerMode;

	public ColorPaletteManager() {
		p5 = getP5();

		palettes = new ArrayList<ColorPalette>();
		selectedPalette = selectedSwatchInPalette = 0;

		pos = new PVector(EditorManager.menuBorderX, p5.height / 2);
		size = new PVector(300, 400);
		paletteStripSize = new PVector(300, 20);

		pickerMode = false;
	}

	public void update() {

	}

	public void render() {

		p5.text("PALETAS DE COLOR", pos.x + 10, pos.y);

		p5.pushStyle();
		p5.noStroke();
		for (int i = 0; i < palettes.size(); i++) {
			ColorPalette thisPalette = palettes.get(i);

			//float posY = pos.y + (paletteStripSize.y * i) + 2; // + 2 = A LITTLE SEPARATION
			//float swatchWidth = paletteStripSize.x / thisPalette.getColorCount();

			for (int j = 0; j < thisPalette.getColorCount(); j++) {
				thisPalette.render();
				/*
				float posX = pos.x + (swatchWidth * j);
				p5.fill(thisPalette.getColor(j));
				p5.rect(posX, posY, swatchWidth, paletteStripSize.y);
				*/
			}

			if (i == selectedPalette) {
				p5.noFill();
				p5.stroke(255, 255, 0);
				p5.rect(thisPalette.pos.x, thisPalette.pos.y, paletteStripSize.x, paletteStripSize.y);
			}
		}

		p5.popStyle();

	}

	public void createNewPalette(int[] colors, String name) {

		ColorPalette newPalette = new ColorPalette(name);
		newPalette.addColors(colors);
		palettes.add(newPalette);
		selectedPalette = palettes.size() - 1;
		
		palettes.get(selectedPalette).setPosition(pos.x, pos.y + ((paletteStripSize.y * (palettes.size() - 1)) + 20 )); // TODO OFFSET IS WRONG
		palettes.get(selectedPalette).setSize(paletteStripSize.x, paletteStripSize.y);
	}

	public void createNewEmptyPalette(String name) {

		ColorPalette newPalette = new ColorPalette(name);
		palettes.add(newPalette);
		selectedPalette = palettes.size() - 1;
		
		palettes.get(selectedPalette).setPosition(pos.x, pos.y + ((paletteStripSize.y * (palettes.size() - 1)) + 20) );
		palettes.get(selectedPalette).setSize(paletteStripSize.x, paletteStripSize.y);

		pickerMode = true;
	}

	public ColorPalette getSelectedPalette() {
		return palettes.get(selectedPalette);
	}

	public ColorPalette getPalette(int index) {
		return palettes.get(index);
	}

	public boolean isAtPickerMode() {
		return pickerMode;
	}

	public int[] select() {
		int[] paletteAndColor = new int[2];

		for (int i = 0; i < palettes.size(); i++) {
			if(isOverPaletteStrip(i)){
				paletteAndColor[0] = i;
				selectedPalette = i;
				
				paletteAndColor[1] = palettes.get(i).selectSwatch();
				
			}
		}

		return paletteAndColor;
	}

	private boolean isOverPaletteStrip(int paletteNumber) {
		PVector stripPos = palettes.get(paletteNumber).pos;

		if (p5.mouseX > stripPos.x && p5.mouseX < (stripPos.x + paletteStripSize.x) && p5.mouseY > stripPos.y && p5.mouseY < (stripPos.y + paletteStripSize.y)) {
			return true;
		} else {
			return false;
		}
	}

	public void keyPressed(char key) {

		if (key == 'p') {
			int[] defaultColors = { p5.color(255), p5.color(200), p5.color(150), p5.color(100), p5.color(50), p5.color(0) };
			String name = "DefaultPalette" + Integer.toString(p5.frameCount);
			createNewPalette(defaultColors, name);
			createNewEmptyPalette("");
		}

	}

	public void mousePressed(int button) {

		// if IS INSIDE THE PALETTE BOUNDING BOX
		select();

	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
