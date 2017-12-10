import processing.core.PApplet;
import processing.core.PVector;

public class Person {

	Body body;
	PApplet app;

	PVector head, spineBase;

	float centerX, centerY;
	float mass = 20; // radius
	long timeBorn;

	Shape myShape;

	WorldMsg[] inMsg;
	WorldMsg outMsg;
	int outIndex = 0;

	public Person(PApplet app) {
		this.app = app;
		timeBorn = System.currentTimeMillis();
		myShape = new Shape(app);
		inMsg = new WorldMsg[3];
	}

	public void update(Body body) {

		this.body = body;

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
		
		myShape.update(centerX, centerY, mass);
	}

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
		// update outgoing message (will change after 10 seconds)
		long currTime = System.currentTimeMillis();
		if (Math.abs(currTime - timeBorn) > 10000)
			outMsg.outgoing(true);
		else
			outMsg.outgoing(false);

		// draw shape based on the population
		myShape.draw(population);

	}

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

	public void jiggle(float speed) {
		float x = centerX + app.random(-1, 1) * speed;
		float y = centerX + app.random(-1, 1) * speed;
		x = PApplet.constrain(x, 0, app.width);
		y = PApplet.constrain(y, 0, app.height);
	}
}
