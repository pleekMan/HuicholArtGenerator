package editor;

import java.util.ArrayList;

import processing.core.PVector;
import processing.data.XML;
import globals.AppManager;
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

		loadColorPalettes();

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

		// DRAW PICKING COLORS
		if (pickerMode) {
			int pickerColor = p5.get(p5.mouseX, p5.mouseY);
			p5.fill(pickerColor);
			p5.noStroke();
			p5.ellipse(p5.mouseX + 25, p5.mouseY - 25, 50, 50);

			if (p5.brightness(pickerColor) > 127) {
				p5.stroke(0);
			} else {
				p5.stroke(255);
			}

			p5.noFill();
			p5.ellipse(p5.mouseX + 25, p5.mouseY - 25, 50, 50);
		}

	}

	public void createNewPalette(int[] colors, String name) {

		ColorPalette newPalette = new ColorPalette(name);
		newPalette.addColors(colors);
		palettes.add(newPalette);
		selectedPalette = palettes.size() - 1;

		palettes.get(selectedPalette).setPosition(pos.x, pos.y + ((paletteStripSize.y * (palettes.size() - 1)) + 20)); // TODO OFFSET IS WRONG
		palettes.get(selectedPalette).setSize(paletteStripSize.x, paletteStripSize.y);
	}

	public void createNewEmptyPalette(String name) {

		ColorPalette newPalette = new ColorPalette(name);
		palettes.add(newPalette);
		selectedPalette = palettes.size() - 1;

		palettes.get(selectedPalette).setPosition(pos.x, pos.y + ((paletteStripSize.y * (palettes.size() - 1)) + 20));
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
			if (isOverPaletteStrip(i)) {
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

	public void loadColorPalettes() {
		XML[] paletteRootTag = AppManager.settings.getChildren("colorPalettes/palette");

		for (int i = 0; i < paletteRootTag.length; i++) {

			String paletteName = paletteRootTag[i].getString("name");

			XML[] colorsInPaletteTag = paletteRootTag[i].getChildren("color");
			int[] colors = new int[colorsInPaletteTag.length];

			for (int j = 0; j < colorsInPaletteTag.length; j++) {
				colors[j] = p5.color(colorsInPaletteTag[j].getInt("r"), colorsInPaletteTag[j].getInt("g"), colorsInPaletteTag[j].getInt("b"));
			}

			createNewPalette(colors, paletteName);
		}

	}

	public void deletePalette() {

		// RESET ALL POSITIONS
		if (selectedPalette + 1 < palettes.size()) {
			for (int i = selectedPalette + 1; i < palettes.size(); i++) {
				palettes.get(i).setPosition(pos.x, pos.y + paletteStripSize.y * i);
			}
		}

		
		// DELETE PALETTE FROM XML
		XML[] allPalettes = AppManager.settings.getChildren("colorPalettes/palette");
		for (int i = 0; i < allPalettes.length; i++) {
			String paletteName = allPalettes[i].getString("name");
			if (paletteName.equals(palettes.get(selectedPalette).name)) {
				XML paletteToRemove = allPalettes[i];
				AppManager.settings.getChild("colorPalettes").removeChild(paletteToRemove);
				break;
			}
		}
		p5.saveXML(AppManager.settings, "data/settings.xml");

		
		palettes.remove(selectedPalette);
		selectedPalette-= 1;
		selectedPalette = (int)p5.constrain(selectedPalette, 0, palettes.size());
	}

	public void keyPressed(char key) {

		if (key == 'p') {
			int[] defaultColors = { p5.color(255), p5.color(200), p5.color(150), p5.color(100), p5.color(50), p5.color(0) };
			String name = "DefaultPalette" + Integer.toString(p5.frameCount);
			createNewPalette(defaultColors, name);
			createNewEmptyPalette("");
		}

		// PRESSING "ESC" FINISHES DE PICKING PROCEDURE
		if (pickerMode) {
			if (key == p5.ESC) {
				finishPickerMode();
			}
		}

	}

	private void finishPickerMode() {

		pickerMode = false;
		selectedPalette = palettes.size() - 1;
		selectedSwatchInPalette = 0;
		palettes.get(selectedPalette).selectedSwatch = selectedSwatchInPalette;

		// SAVE NEW COLOR PALETTE
		ColorPalette selectedPaletteToSave = palettes.get(selectedPalette);

		XML palettesRoot = AppManager.settings.getChild("colorPalettes");

		XML newChildPalette = palettesRoot.addChild("palette");
		newChildPalette.setString("name", "Palette " + p5.year() + "-" + p5.month() + "-" + p5.day() + "_" + p5.hour() + ":" + p5.minute() + ":" + p5.second());

		for (int i = 0; i < selectedPaletteToSave.getColorCount(); i++) {
			XML newChildColor = newChildPalette.addChild("color");
			newChildColor.setInt("r", (int) p5.red(selectedPaletteToSave.getColor(i)));
			newChildColor.setInt("g", (int) p5.green(selectedPaletteToSave.getColor(i)));
			newChildColor.setInt("b", (int) p5.blue(selectedPaletteToSave.getColor(i)));
		}

		p5.saveXML(AppManager.settings, "data/settings.xml");
	}

	public void mousePressed(int button) {

		if (p5.mouseX > EditorManager.menuBorderX) {
			// IF THE USER IS CLICKING OVER THE MENU COLUMN
			select();
		}

		if (pickerMode && button == p5.LEFT) {
			int pickedColor = p5.get(p5.mouseX, p5.mouseY);
			palettes.get(selectedPalette).addColor(pickedColor);
		}

	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
