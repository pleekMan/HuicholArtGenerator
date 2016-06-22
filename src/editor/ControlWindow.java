package editor;

import canvas.CanvasManager;
import globals.AppManager;
import controlP5.ControlP5;
import processing.core.PApplet;

//the ControlFrame class extends PApplet, so we 
//are creating a new processing applet inside a
//new frame with a controlP5 object loaded
public class ControlWindow extends PApplet {

	public ControlP5 cp5;

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
		
		cp5.addSlider("testColorControl").plugTo(parent, "testColorControl").setRange(0, 255).setPosition(10, 30);
		cp5.addButton("gui_newFigure").plugTo(parent, "gui_newFigure").setPosition(10, 50).setLabel("NEW FIGURE");
		cp5.addSlider("gui_newFigureCycles").plugTo(parent, "gui_newFigureCycles").setLabel("NEW FIGURE CYCLES").setRange(1, 10).setPosition(10, 10).setValue(3).showTickMarks(true).setNumberOfTickMarks(10);

		
		cp5.addSlider("gui_viewPortScale").plugTo(parent, "gui_viewPortScale").setRange(0.1f, 1).setPosition(10, 80).setValue(1f).setLabel("VIEWPORT SCALE");
		cp5.addToggle("gui_showFigureGizmos").plugTo(parent, "gui_showFigureGizmos").setValue(true).setMode(cp5.SWITCH).setPosition(10, 100).setLabel("SHOW FIGURE GIZMOS");
		cp5.addToggle("gui_showGridLayer").plugTo(parent, "gui_showGridLayer").setValue(true).setMode(cp5.SWITCH).setPosition(10, 140).setLabel("SHOW GRID");
		
		cp5.addButton("gui_rewind").plugTo(parent, "gui_rewind").setPosition(10, 265).setLabel("|<").setWidth(30);
		cp5.addButton("gui_pause").plugTo(parent, "gui_pause").setPosition(50, 265).setLabel("||").setWidth(30);
		cp5.addButton("gui_play").plugTo(parent, "gui_play").setPosition(90, 265).setLabel(">").setWidth(30);
		
		cp5.addButton("gui_delete").plugTo(parent, "gui_delete").setPosition(10, 300).setLabel("DELETE");
		cp5.addButton("gui_deleteAll").plugTo(parent, "gui_deleteAll").setPosition(100, 300).setLabel("DELETE ALL");
		
		cp5.addToggle("gui_showBackImage").plugTo(parent, "gui_showBackImage").setValue(false).setMode(cp5.SWITCH).setPosition(10, 350).setLabel("SHOW BACKGROUND IMAGE");
		cp5.addSlider("gui_backImageScale").plugTo(parent, "gui_backImageScale").setRange(0.1f, 2).setPosition(10, 390).setValue(1f).setLabel("BACK IMAGE SCALE");
		cp5.addSlider("gui_backImageOpacity").plugTo(parent, "gui_backImageOpacity").setRange(0, 1).setPosition(10, 410).setValue(1f).setLabel("BACK IMAGE opacity");
		cp5.addButton("gui_backImageSelect").plugTo(parent, "gui_backImageSelect").setPosition(10, 430).setLabel("BUSCAR IMAGEN");

		cp5.addButton("gui_newPalette").plugTo(parent, "gui_newPalette").setPosition(10, 500).setSize(100,20).setLabel("NEW COLOR PALETTE");
		cp5.addButton("gui_deletePalette").plugTo(parent, "gui_deletePalette").setPosition(10, 530).setSize(100,20).setLabel("DELETE COLOR PALETTE");
		cp5.addButton("gui_assignToFigure").plugTo(parent, "gui_assignToFigure").setPosition(10, 560).setSize(100,20).setLabel("ASIGNAR A FIGURA");

		cp5.addToggle("gui_shapePointInterpolation").plugTo(parent, "gui_shapePointInterpolation").setMode(cp5.SWITCH).setPosition(10, 590).setSize(100,20).setLabel("PUNTOS SOLOS");

		cp5.addToggle("gui_showRoi").plugTo(parent, "gui_showRoi").setValue(false).setMode(cp5.SWITCH).setPosition(10, 180).setLabel("SHOW ROI");
		cp5.addTextfield("gui_renderOutFolder").plugTo(parent,"gui_renderOutFolder").setPosition(10, 225).setSize(180,20).setLabel("Nombre de la Carpeta de Render:");
		cp5.addToggle("gui_enableRenderToFile").plugTo(parent, "gui_enableRenderToFile").setMode(cp5.SWITCH).setPosition(200, 225).setSize(80,20).setLabel("GUARDAR ANIMACION");

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
	
	public void gui_newFigureCycles(float value){
		parent.newFigureCycles = (int)value;
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
	
	// BACKGROUND / REFERENCE IMAGE -------------
	
	public void gui_showBackImage(boolean state){
		parent.showBackImage = state;
	}
	public void gui_backImageScale(float value){
		parent.backImageScale = value;
	}
	public void gui_backImageOpacity(float value){
		parent.backImageOpacity = value;
	}
	
	public void gui_backImageSelect(){
		parent.selectImageInput();
	}
	
	// COLOR PALETTE COMMANDS -------------
	
	public void gui_newPalette(){
		parent.createPalette();
	}

	public void gui_deletePalette(){
		parent.deletePalette();
	}
	
	public void gui_assignToFigure(){
		parent.assignPaletteToFigure();
	}
	
	public void gui_shapePointInterpolation(boolean state){
		parent.setShapePointInterpolation(state);
	}
	
	public void gui_renderOutFolder(String text){
		parent.checkRenderFolder(text);
	}
	
	public void gui_enableRenderToFile(boolean state){
		parent.prepareRender(state);
	}

}