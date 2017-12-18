import processing.core.PApplet;
import processing.core.PVector;

/**
 * Represents a single person.
 * 
 * @author Zhiling
 *
 */
public class Person {

	Body body;
	PApplet app;

	PVector head, spineBase;

	float centerX, centerY;
	float mass = 20; // radius
	long timeBorn;
	long layover;

	Shape myShape;

	WorldMsg[] inMsg;
	WorldMsg outMsg;
	int outIndex = 0;

	double avgX = 0.01;
	double avgY = 0.01;

	public Person(PApplet app) {
		this.app = app;
		timeBorn = System.currentTimeMillis();
		myShape = new Shape(app);
		inMsg = new WorldMsg[3];
	}

	/**
	 * Updates current data
	 * 
	 * @param body
	 * @param newEnter
	 */
	public void update(Body body, boolean newEnter, boolean newExit) {

		this.body = body;

		long currTime = System.currentTimeMillis();
		layover = Math.abs(currTime - timeBorn) / 1000;

		head = body.getJoint(Body.HEAD);
		spineBase = body.getJoint(Body.SPINE_BASE);

		if (head != null && spineBase != null) {
			centerX = spineBase.x;
			centerY = spineBase.y;
			float newRad = head.dist(spineBase) * 20;
			// update radius based on person's size
			if (Math.abs(newRad - mass) >= 0.5)
				mass = newRad;
		}

		// if one person has been on the screen for more than two seconds
		if (layover > 2) {
			avgX = (avgX * (layover - 1) + centerX) / layover;
			avgY = (avgY * (layover - 1) + centerY) / layover;
		}

		myShape.update(centerX, centerY, mass);

		if (newEnter)
			myShape.speedUp();
		if (newExit)
			myShape.speedDown();
	}

	/**
	 * Calls on two draw methods
	 * 
	 * @param population
	 */
	public void draw(int population) {

		outMsg = new WorldMsg(app, centerX, centerY, mass);
		inMsg[outIndex] = new WorldMsg(app, centerX, centerY, mass);

		outIndex++;
		// If we hit the end of the array
		if (outIndex >= inMsg.length)
			outIndex = 0; // Start over

		// update incoming message (only locations)
		for (int i = 0; i < outIndex; i++) {
			inMsg[i].update();
			inMsg[i].incoming();
		}
		// update outgoing message (will change after 15 seconds)
		if (layover > 15)
			outMsg.outgoing(true);
		else
			outMsg.outgoing(false);

		// draw shape based on the population
		myShape.draw(population);

	}

	/**
	 * Calculates motion increment for ECG curve
	 * 
	 * @return
	 */
	public float motion() {
		return (float) (centerY - avgY) * 100f;
		// return Math.max((float) (centerX - avgX), (float) (centerY - avgY)) * 100f;
	}

	// not implemented yet
	public void randomColor() {
		// Display the drop
		// app.fill(color);
		// use frameCount and noise to change the red color component
		float r = app.noise((float) (app.frameCount * 0.01)) * 255;
		// use frameCount and modulo to change the green color component
		float g = app.frameCount % 255;
		// use frameCount and noise to change the blue color component
		float b = 255 - app.noise((float) (1 + app.frameCount * 0.025)) * 255;
		int color = app.color(r, g, b);
	}

	// not implemented yet
	public void jiggle(float speed) {
		float x = centerX + app.random(-1, 1) * speed;
		float y = centerX + app.random(-1, 1) * speed;
		x = PApplet.constrain(x, 0, app.width);
		y = PApplet.constrain(y, 0, app.height);
	}
}
