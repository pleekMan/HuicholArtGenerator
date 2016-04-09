package editor;

import canvas.CanvasManager;
import globals.AppManager;
import controlP5.ControlP5;
import processing.core.PApplet;

//the ControlFrame class extends PApplet, so we 
//are creating a new processing applet inside a
//new frame with a controlP5 object loaded
public class ControlWindow extends PApplet {

	ControlP5 cp5;

	EditorManager parent;

	int w = 1;
	int h = 1;
	int abc = 100;

	public void setup() {
		size(w, h);
		frameRate(25);
		cp5 = new ControlP5(this);
		cp5.setColorBackground(color(50));
		cp5.setColorForeground(color(200,127,0));
		cp5.setColorActive(color(200,127,0));
		cp5.setColorCaptionLabel(color(255,255,0));
		
		cp5.addSlider("abc").setRange(0, 255).setPosition(10, 10).setValue(0);;
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





	}

	public void draw() {
		background(abc);
	}

	public ControlWindow() {
	}

	public ControlWindow(EditorManager theParent, int theWidth, int theHeight) {
		parent = theParent;
		w = theWidth;
		h = theHeight;
	}

	public ControlP5 control() {
		return cp5;
	}

	public void gui_newFigure(int theValue) {
		//println("a button event from buttonA: " + theValue);
		parent.prepareNewFigure();
	}
	
	public void gui_viewPortScale(float value){
		AppManager.canvasScale = value;
	}
	
	public void gui_showFigureGizmos(boolean state){
		parent.showFigureGizmos = state;
	}
	
	public void gui_showGridLayer(boolean state){
		parent.showGridPoints = state;
	}
	
	public void gui_showRoi(boolean state){
		parent.showRoi = state;
	}
	
	public void gui_rewind(){
		parent.rewind();
	}
	public void gui_pause(){
		parent.pause();
	}
	public void gui_play(){
		parent.play();
	}
	
	public void gui_delete(){
		parent.deleteFigure();
	}
	
	public void gui_deleteAll(){
		parent.deleteAllFigures();
	}

}