package editor;

import canvas.CanvasManager;
import globals.AppManager;
import globals.Main;
import globals.PAppletSingleton;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Controller;
import processing.core.PApplet;

//the ControlFrame class extends PApplet, so we 
//are creating a new processing applet inside a
//new frame with a controlP5 object loaded
class ControlWindow {

	public ControlP5 cp5;
	Main p5;

	EditorManager parent;

	public Controller<Button> buttonA;

	public ControlWindow(EditorManager _parent) {
		p5 = getP5();
		parent = _parent;
		init();
	}

	

	public void init() {

		cp5 = new ControlP5(p5);
		cp5.setColorBackground(p5.color(50));
		cp5.setColorForeground(p5.color(200, 127, 0));
		cp5.setColorActive(p5.color(200, 127, 0));
		cp5.setColorCaptionLabel(p5.color(255, 255, 0));

		cp5.addSlider("abc").setRange(0, 255).setPosition(10, 10).setValue(0);
		;
		cp5.addSlider("testColorControl").plugTo(parent, "testColorControl").setRange(0, 255).setPosition(10, 30);
		cp5.addButton("gui_newFigure").plugTo(parent, "gui_newFigure").setPosition(10, 50).setLabel("NEW FIGURE");
		cp5.addSlider("gui_viewPortScale").plugTo(parent, "gui_viewPortScale").setRange(0.1f, 1).setPosition(10, 80).setValue(1f).setLabel("VIEWPORT SCALE");

		cp5.addToggle("gui_showFigureGizmos").plugTo(parent, "gui_showFigureGizmos").setValue(true).setMode(cp5.SWITCH).setPosition(10, 100).setLabel("SHOW FIGURE GIZMOS");
		cp5.addToggle("gui_showGridLayer").plugTo(parent, "gui_showGridLayer").setValue(true).setMode(cp5.SWITCH).setPosition(10, 140).setLabel("SHOW GRID");
		cp5.addToggle("gui_showRoi").plugTo(parent, "gui_showRoi").setValue(false).setMode(cp5.SWITCH).setPosition(10, 180).setLabel("SHOW ROI");

		cp5.addButton("gui_rewind").plugTo(parent, "gui_rewind").setPosition(10, 240).setLabel("|<").setWidth(30);
		cp5.addButton("gui_pause").plugTo(parent, "gui_pause").setPosition(50, 240).setLabel("||").setWidth(30);
		cp5.addButton("gui_play").plugTo(parent, "gui_play").setPosition(90, 240).setLabel(">").setWidth(30);

		cp5.addButton("gui_delete").plugTo(parent, "gui_delete").setPosition(10, 300).setLabel("DELETE");
		cp5.addButton("gui_deleteAll").plugTo(parent, "gui_deleteAll").setPosition(100, 300).setLabel("DELETE ALL");

		cp5.addToggle("gui_showBackImage").plugTo(parent, "gui_showBackImage").setValue(false).setMode(cp5.SWITCH).setPosition(10, 350).setLabel("SHOW BACKGROUND IMAGE");
		cp5.addSlider("gui_backImageScale").plugTo(parent, "gui_backImageScale").setRange(0.1f, 2).setPosition(10, 390).setValue(1f).setLabel("BACK IMAGE SCALE");
		cp5.addSlider("gui_backImageOpacity").plugTo(parent, "gui_backImageOpacity").setRange(0, 1).setPosition(10, 410).setValue(1f).setLabel("BACK IMAGE opacity");
		cp5.addButton("gui_backImageSelect").plugTo(parent, "gui_backImageSelect").setPosition(10, 430).setLabel("BUSCAR IMAGEN");

		buttonA = cp5.addButton("BOTON A");
		buttonA.setPosition(300, 300);

	}

	public void controlEvent(ControlEvent theEvent) {
		if (theEvent.isController()) {

			System.out.println("Something is happening.");

			if (theEvent.getName().matches("BOTON A")) {
				p5.background(255, 0, 0);
			}
		}
	}

	public ControlP5 control() {
		return cp5;
	}

	public void gui_newFigure(int theValue) {
		//println("a button event from buttonA: " + theValue);
		parent.prepareNewFigure();
	}

	public void gui_viewPortScale(float value) {
		AppManager.canvasScale = value;
		p5.println("SCALING VIEWPORT");
	}

	public void gui_showFigureGizmos(boolean state) {
		parent.showFigureGizmos = state;
	}

	public void gui_showGridLayer(boolean state) {
		parent.showGridPoints = state;
	}

	public void gui_showRoi(boolean state) {
		parent.showRoi = state;
	}

	public void gui_rewind() {
		parent.rewind();
	}

	public void gui_pause() {
		parent.pause();
	}

	public void gui_play() {
		parent.play();
	}

	public void gui_delete() {
		parent.deleteFigure();
	}

	public void gui_deleteAll() {
		parent.deleteAllFigures();
	}

	public void gui_showBackImage(boolean state) {
		parent.showBackImage = state;
	}

	public void gui_backImageScale(float value) {
		parent.backImageScale = value;
	}

	public void gui_backImageOpacity(float value) {
		parent.backImageOpacity = value;
	}

	public void gui_backImageSelect() {
		parent.selectImageInput();
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}