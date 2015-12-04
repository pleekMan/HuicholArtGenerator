package globals;

/**
 * The goal of this class is to have the PApplet available from any place of the application.
 * There is no need to pass the PApplet as a parameter in every object.
 *
 * This is a ONE INSTANCE class. As a singleton, this class is instanced just once.
 *
 * @author alejandro
 */
public class PAppletSingleton {
	// "static" IS THE KEY WORD HERE
    private static PAppletSingleton P5INSTANCE = new PAppletSingleton();

    private PAppletSingleton() {}

    public static PAppletSingleton getInstance() {
        return P5INSTANCE;
    }

    //--------
    
    private Main appMain;

    public void setP5Applet(Main _appMain){
    	this.appMain = _appMain;
	}

    public Main getP5Applet() {
		return appMain;
	}
}