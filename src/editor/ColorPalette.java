package editor;

import globals.Main;
import globals.PAppletSingleton;

public class ColorPalette {

	Main p5;

	String name;

	int[] colors;

	public ColorPalette(String _name) {
		p5 = getP5();

		name = _name;

		generateColors(p5.floor(p5.random(3.99f)));

	}

	public int getColor(int index) {
		return colors[index];
	}

	public String getName() {
		return name;
	}

	public int getColorCount() {
		return colors.length;
	}

	public void eraseAllColors() {

		for (int i = 0; i < colors.length; i++) {
			colors[i] = p5.color(50);
		}
	}

	public void generateColors(int selector) {
		// TODO THIS IS TOTALLY TEMPORARY, UNTIL CODING SOMETHING SERIOUS..!!!
		p5.println("Chosen Palette: " + selector);
		switch (selector) {
		case 0:
			colors = new int[4];
			colors[0] = p5.color(255, 255, 0);
			colors[1] = p5.color(255, 127, 0);
			colors[2] = p5.color(255, 0, 0);
			colors[3] = p5.color(150, 0, 0);
			break;
		case 1:
			colors = new int[21];
			colors[0] = p5.color(120, 50, 30);
			colors[1] = p5.color(170, 50, 65);
			colors[2] = p5.color(200, 50, 75);
			colors[3] = p5.color(225, 35, 55);
			colors[4] = p5.color(255, 120, 105);
			colors[5] = p5.color(220, 210, 210);
			colors[6] = p5.color(160, 135, 140);
			colors[7] = p5.color(125, 100, 100);
			colors[8] = p5.color(130, 65, 60);
			colors[9] = p5.color(130, 60, 50);
			colors[10] = p5.color(160, 20, 35);
			colors[11] = p5.color(215, 30, 50);
			colors[12] = p5.color(250, 130, 15);
			colors[13] = p5.color(250, 170, 20);
			colors[14] = p5.color(210, 210, 210);
			colors[15] = p5.color(130, 160, 200);
			colors[16] = p5.color(60, 95, 185);
			colors[17] = p5.color(55, 65, 115);
			colors[18] = p5.color(40, 40, 65);
			colors[19] = p5.color(20, 20, 30);
			colors[20] = p5.color(15, 15, 20);
			break;
		case 2:
			colors = new int[17];
			colors[0] = p5.color(230, 220, 220);
			colors[1] = p5.color(230, 220, 220);
			colors[2] = p5.color(230, 220, 220);
			colors[3] = p5.color(60, 105, 170);
			colors[4] = p5.color(230, 220, 220);
			colors[5] = p5.color(230, 220, 220);
			colors[6] = p5.color(230, 220, 220);
			colors[7] = p5.color(230, 220, 220);
			colors[8] = p5.color(230, 220, 220);
			colors[9] = p5.color(250, 50, 75);
			colors[10] = p5.color(230, 220, 220);
			colors[11] = p5.color(255, 145, 50);
			colors[12] = p5.color(230, 220, 220);
			colors[13] = p5.color(200, 200, 200);
			colors[14] = p5.color(150, 150, 150);
			colors[15] = p5.color(100, 100, 100);
			colors[16] = p5.color(60, 60, 60);

			break;
		case 3:
			colors = new int[8];
			colors[0] = p5.color(70, 60, 50);
			colors[1] = p5.color(165, 125, 125);
			colors[2] = p5.color(70, 60, 50);
			colors[3] = p5.color(165, 125, 125);
			colors[4] = p5.color(70, 60, 50);
			colors[5] = p5.color(165, 125, 125);
			colors[6] = p5.color(70, 60, 50);
			colors[7] = p5.color(165, 125, 125);
			break;
		default:
			break;
		}

	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
