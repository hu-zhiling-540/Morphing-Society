import java.io.IOException;
import java.util.HashMap;

import processing.core.PApplet;

/**
 * The main application initiates the recorded demo or renders real-time data.
 * 
 * @author Zhiling
 *
 */
public class StrangerLandApp extends PApplet {

	KinectBodyDataProvider kinectReader;

	public static float PROJECTOR_RATIO = 1080f / 1920.0f;
	static float scaleX, scaleY;

	HashMap<Long, Person> society = new HashMap<Long, Person>();
	PersonTracker pTracker = new PersonTracker();
	ECG ecg = new ECG(this);

	public void draw() {

		background(0); // black

		setScale(.5f);

		ecg.draw();

		KinectBodyData bodyData = kinectReader.getData();
		// KinectBodyData bodyData = kinectReader.getMostRecentData();
		boolean newEnter = false;
		pTracker.update(bodyData);
		for (Long id : pTracker.getEnters()) {
			newEnter = true; // will speed up person
			society.put(id, new Person(this));
		}
		boolean newExit = false;
		for (Long id : pTracker.getExits()) {
			newExit = true;
			society.remove(id);
		}
		int population = society.size();
		float motion = 0;
		for (Body b : pTracker.getPeople().values()) {
			Person p = society.get(b.getId());
			if (p != null) {
				p.update(b, newEnter, newExit);
				p.draw(population);

				// overall motion
				if (motion + p.motion() < width / 2 && motion + p.motion() > -width / 2)
					motion += p.motion();
			}
		}
		ecg.update(motion);
	}

	public void setup() {
		/*
		 * use this code to run your PApplet from data recorded by UPDRecorder
		 */
		try {
			// kinectReader = new KinectBodyDataProvider("exitTest.kinect", 2);
			kinectReader = new KinectBodyDataProvider("fivePeople.kinect", 1);
		} catch (IOException e) {
			System.out.println("Unable to create kinect producer");
		}

		// kinectReader = new KinectBodyDataProvider(8008);
		kinectReader.start();
	}

	public void settings() {
		createWindow(true, false, .5f);

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
		scaleX = zoom * width / 2.0f;
		scaleY = zoom * -width / 2.0f;
		scale(zoom * width / 2.0f, zoom * -width / 2.0f);
		translate(1f / zoom, -PROJECTOR_RATIO / zoom);
	}

	public static void main(String[] args) {
		PApplet.main(StrangerLandApp.class.getName());
	}

}
