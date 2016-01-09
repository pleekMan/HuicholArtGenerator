package editor;

import globals.AppManager;
import globals.Main;
import globals.PAppletSingleton;

import java.awt.Frame;
import java.awt.BorderLayout;

import controlP5.*;

/*
 *  OJO..!! IF THE APP IS RUN WHEN THE ACTIVE EDITOR WINDOW IS ControlFrame.java
 * 	IT THROWS AN EXCEPTION. ALWAYS RUN THROUGH OTHER WINDOW
 */

public class EditorManager {
	Main p5;

	//ControlP5 controlGui;
	ControlWindow controlFrame;

	public int testColorControl;
	boolean newFigureMode;

	public EditorManager() {
		p5 = getP5();

		//controlGui = new ControlP5(p5);
		controlFrame = addControlFrame("Editor Options", 300, 500);

		newFigureMode = false;
	}

	public void update() {
		
	}

	public void render() {
		//p5.fill(testColorControl);
		//p5.rect(100, 100, 400, 400);

		//if (p5.frameCount % 20 == 0)
			//p5.println(newFigureMode);
		//controlFrame.cp5.getController("gui_newFigure").setValue(0f);
	}

	public ControlWindow addControlFrame(String theName, int theWidth, int theHeight) {
		Frame f = new Frame(theName);
		ControlWindow p = new ControlWindow(this, theWidth, theHeight);
		f.add(p);
		p.init();
		f.setTitle(theName);
		f.setSize(p.w, p.h);
		f.setLocation(100, 100);
		f.setResizable(false);
		f.setVisible(true);
		return p;
	}

	protected Main getP5() {
		return PAppletSingleton.getInstance().getP5Applet();
	}

}
