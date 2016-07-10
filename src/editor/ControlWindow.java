package editor;

import canvas.CanvasManager;
import globals.AppManager;
import controlP5.ControlP5;
import controlP5.Label;
import controlP5.Slider;
import controlP5.Toggle;
import processing.core.PApplet;
import processing.core.PImage;

//the ControlFrame class extends PApplet, so we 
//are creating a new processing applet inside a
//new frame with a controlP5 object loaded
public class ControlWindow extends PApplet {

	public ControlP5 cp5;

	EditorManager parent;
	
	PImage backImage;

	int w = 1;
	int h = 1;
	int abc = 100;
	

	public void setup() {
		size(w, h);
		frameRate(10);
		
		//backImage = loadImage("data/ControlPanel_backg.png");
		
		cp5 = new ControlP5(this);
		cp5.setColorBackground(parent.guiColors[parent.BLUEDARK]);
		cp5.setColorForeground(parent.guiColors[parent.BLUEGRAY]);
		cp5.setColorActive(parent.guiColors[parent.GREEN]);
		cp5.setColorCaptionLabel(color(255));
		
		//cp5.addSlider("testColorControl").plugTo(parent, "testColorControl").setRange(0, 255).setPosition(10, 30);
		cp5.addButton("gui_newFigure").plugTo(parent, "gui_newFigure").setPosition(20, 140).setLabel("NEW FIGURE");
		Slider sldr = cp5.addSlider("gui_newFigureCycles").plugTo(parent, "gui_newFigureCycles").setLabel("NEW FIGURE CYCLES").setRange(1, 10).setPosition(20, 180).setSize(100,15).setValue(3).showTickMarks(true).setNumberOfTickMarks(10);
			sldr.getCaptionLabel().alignX(0);
			sldr.getCaptionLabel().getStyle().marginTop = -15;
		
		Toggle tgl1 = cp5.addToggle("gui_shapePointInterpolation").plugTo(parent, "gui_shapePointInterpolation").setPosition(135, 180).setSize(40,23).setLabel("PUNTOS SOLOS");
			tgl1.getCaptionLabel().getStyle().marginTop = -40;
		cp5.addButton("gui_delete").plugTo(parent, "gui_delete").setPosition(20, 213).setLabel("DELETE");
		cp5.addButton("gui_deleteAll").plugTo(parent, "gui_deleteAll").setPosition(103, 213).setLabel("DELETE ALL");
		
		Slider sldr1 = cp5.addSlider("gui_viewPortScale").plugTo(parent, "gui_viewPortScale").setRange(0.1f, 2).setPosition(20, 305).setSize(100, 15).setValue(1f).setLabel("VIEWPORT SCALE");
			sldr1.getCaptionLabel().alignX(0);
			sldr1.getCaptionLabel().getStyle().marginTop = -15;
		cp5.addToggle("gui_showFigureGizmos").plugTo(parent, "gui_showFigureGizmos").setValue(true).setPosition(130,335).setLabel("SHOW FIGURE GIZMOS");
		cp5.addToggle("gui_showGridLayer").plugTo(parent, "gui_showGridLayer").setValue(true).setPosition(20, 335).setLabel("SHOW GRID");
		
		cp5.addButton("gui_rewind").plugTo(parent, "gui_rewind").setPosition(95, 70).setLabel("|<").setWidth(30);
		cp5.addButton("gui_pause").plugTo(parent, "gui_pause").setPosition(137, 70).setLabel("||").setWidth(30);
		cp5.addButton("gui_play").plugTo(parent, "gui_play").setPosition(180, 70).setLabel(">").setWidth(30);
		
		
		cp5.addButton("gui_backImageSelect").plugTo(parent, "gui_backImageSelect").setPosition(20, 400).setLabel("OPEN IMAGE");
		cp5.addToggle("gui_showBackImage").plugTo(parent, "gui_showBackImage").setValue(false).setPosition(20, 435).setLabel("SHOW BACKGROUND IMAGE");
		Slider sldr2 = cp5.addSlider("gui_backImageScale").plugTo(parent, "gui_backImageScale").setRange(0.1f, 2).setPosition(20, 495).setSize(100, 15).setValue(1f).setLabel("BACK IMAGE SCALE");
			sldr2.getCaptionLabel().alignX(0);
			sldr2.getCaptionLabel().getStyle().marginTop = -15;
		Slider sldr3 = cp5.addSlider("gui_backImageOpacity").plugTo(parent, "gui_backImageOpacity").setRange(0, 1).setPosition(135, 495).setSize(100, 15).setValue(1f).setLabel("BACK IMAGE OPACITY");
			sldr3.getCaptionLabel().alignX(0);
			sldr3.getCaptionLabel().getStyle().marginTop = -15;
			
		cp5.addButton("gui_newPalette").plugTo(parent, "gui_newPalette").setPosition(20, 565).setSize(80,20).setLabel("NEW PALETTE");
		cp5.addButton("gui_deletePalette").plugTo(parent, "gui_deletePalette").setPosition(120, 565).setSize(80,20).setLabel("DELETE PALETTE");
		cp5.addButton("gui_assignToFigure").plugTo(parent, "gui_assignToFigure").setPosition(20, 591).setSize(80,20).setLabel("ASSIGN TO FIGURE");


		cp5.addTextfield("gui_renderOutFolder").plugTo(parent,"gui_renderOutFolder").setPosition(20, 660).setSize(180,20).setLabel("RENDER NAME:");
		cp5.addToggle("gui_enableRenderToFile").plugTo(parent, "gui_enableRenderToFile").setPosition(20, 718).setSize(40,20).setLabel("GUARDAR ANIMACION");
		cp5.addToggle("gui_showRoi").plugTo(parent, "gui_showRoi").setValue(false).setPosition(20, 755).setLabel("SHOW ROI");
		
		
	}
	
	public void setBackgroundImage(PImage image){
		backImage = image;
	}

	public void draw() {
		

		background(0);
		image(backImage,0,0);
		
		fill(EditorManager.guiColors[EditorManager.RED]);
		text("SAVE TO: renders/" + EditorManager.renderName + "/",20,707);
		//fill(255, 255, 0);
		//text("FR: " + frameRate, 20, 20);
		//text("X: " + mouseX + " / Y: " + mouseY, mouseX, mouseY);
	}

	public ControlWindow() {
	}

	public ControlWindow(EditorManager theParent, int theWidth, int theHeight) {
		parent = theParent;
		w = theWidth;
		h = theHeight + 23; // 23 = IN THIS WINDOW, THE TITLE BAR EATS UP PIXELS.... ( ??? )

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
		parent.enableCanvasClear = true;
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