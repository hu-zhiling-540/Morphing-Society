import processing.core.PApplet;

/**
 * 
 */

/**
 * @author Zhiling
 *
 */
public class MorphingSocietyApplication extends PApplet {

	KinectBodyDataProvider kinectReader;
	public static float PROJECTOR_RATIO = 1080f / 1920.0f;

	public static void main(String[] args) {
		PApplet.main(MorphingSocietyApplication.class.getName());
	}

	public void setup() {

		/*
		 * use this code to run your PApplet from data recorded by UPDRecorder
		 */
		// try {
		// kinectReader = new KinectBodyDataProvider("exitTest.kinect", 2);
		// } catch (IOException e) {
		// System.out.println("Unable to create kinect producer");
		// }

		kinectReader = new KinectBodyDataProvider(8008);
		kinectReader.start();

	}

	public void settings() {
		createWindow(true, true, .25f);
	}

	public void createWindow(boolean useP2D, boolean isFullscreen, float windowsScale) {
		if (useP2D) {
			if (isFullscreen) {
				fullScreen(P2D);
			} else {
				size((int) (1920 * windowsScale), (int) (1080 * windowsScale), P2D);
			}
		} else {
			if (isFullscreen) {
				fullScreen();
			} else {
				size((int) (1920 * windowsScale), (int) (1080 * windowsScale));
			}
		}
	}

	// use lower numbers to zoom out (show more of the world)
	// zoom of 1 means that the window is 2 meters wide and appox 1 meter tall.
	public void setScale(float zoom) {
		scale(zoom * width / 2.0f, zoom * -width / 2.0f);
		translate(1f / zoom, -PROJECTOR_RATIO / zoom);
	}

}
