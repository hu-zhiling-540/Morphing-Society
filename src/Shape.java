import java.awt.Color;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Shape {

	Body body;
	PApplet app;
	// int color;

	PVector head;
	PVector spineBase;

	float centerX; // location based on the center
	float centerY;

	long timeBorn;
	// long layover = 1; // in seconds
	float rotSpeed;
	float decel = 10; // deceleration
	// float minSpeed = (float) 0.1;
	float mass = 50; // radius
	// float angle = 0;
	float aVelocity = 0;
	float aAcceleration = 0;

	// store the vertices that been lerped to
	// ArrayList<PVector> morphSet = new ArrayList<PVector>();

	// to store all the locations past by until meeting that married person
	ArrayList<PVector> exTraces = new ArrayList<PVector>();
	ArrayList<PVector> newTraces = new ArrayList<PVector>();

	// potential shapes represented by fixed size of vertices
	PVector[] heptagon;
	PVector[] hextagon;
	PVector[] pentagon;
	PVector[] square;
	PVector[] triangle;
	PVector[] circle;
	PVector[] currShape;

	int maxSize = 600;

	public static final Color BABY_PINK = new Color(255, 182, 193);
	public static final Color HOT_PINK = new Color(255, 105, 180);
	public static final Color BLUE = new Color(168, 111, 186);
	public static final Color RED = new Color(232, 64, 170);
	public static final Color pureRed = new Color(255, 0, 0);
	public static final Color pureGreen = new Color(0, 255, 0);
	public static final Color pureBlue = new Color(0, 0, 255);

	public Shape(PApplet app) {
		this.app = app;
		timeBorn = System.currentTimeMillis();

		heptagon = setupPolygon(7, maxSize, mass);
		// hextagon = setupPolygon(6, maxSize, mass);
		// circle = new PVector[maxSize];
		// circle = setupPolygon(0, maxSize, mass);
		// pentagon = setupPolygon(5, maxSize, mass);
		// square = setupPolygon(4, maxSize, mass);
		// triangle = setupPolygon(3, maxSize, mass);
		// initialization for vertices set
		// initCircle();
		currShape = new PVector[maxSize];
		currShape = heptagon;
	}

	public void update(Body body) {
		this.body = body;

		// float ac = (float) (Math.abs(PApplet.sin(app.frameCount / 100f) * 0.2));
		// rotSpeed += ac;

		// ease out rotSpeed speed
		rotSpeed = app.frameCount / -200; // constant rotation speed
		if (decel > 0) {
			decel -= 1;
			rotSpeed += decel;
		}
		System.out.println(rotSpeed);

		head = body.getJoint(Body.HEAD);
		spineBase = body.getJoint(Body.SPINE_BASE);
		if (head != null && spineBase != null) {
			// System.out.println(mass);
			centerX = spineBase.x;
			centerY = spineBase.y;
			int newRad = (int) (head.dist(spineBase) * 50); // update radius
			if (Math.abs(newRad - mass) >= 2) {
				mass = newRad;
				heptagon = setupPolygon(7, maxSize, mass);
				hextagon = setupPolygon(6, maxSize, mass);
				pentagon = setupPolygon(5, maxSize, mass);
				square = setupPolygon(4, maxSize, mass);
				triangle = setupPolygon(3, maxSize, mass);
			}
		}
	}

	// ref: https://processing.org/examples/morph.html
	public void morph(PVector[] nextShape) {
		// app.fill(BLUE.getRGB());
		// app.noFill();
		app.noStroke();
		for (int i = 0; i < maxSize; i++) {
			PVector v1 = nextShape[i];
			// Get the next vertex we will draw to
			PVector v2 = currShape[i];
			// Lerp to the target
			v2.lerp(v1, (float) 0.05);
		}
		app.pushMatrix();
		PShape s = app.createShape();
		s.rotate(rotSpeed);
		s.beginShape();
		s.scale(.01f, .01f);

		// draw relative to the center of this person
		app.translate(centerX, centerY);
		for (PVector v : currShape) // drawing shape
			s.vertex(v.x, v.y);
		s.endShape(PApplet.CLOSE);
		// create this shape in its parent pApplet
		app.shape(s);
		app.popMatrix();
	}

	public void draw(int state) {

		// use frameCount and noise to change the red color component
		float r = app.noise((float) (app.frameCount * 0.01)) * 255;
		// use frameCount and modulo to change the green color component
		float g = app.frameCount % 255;
		// use frameCount and noise to change the blue color component
		float b = 255 - app.noise((float) (1 + app.frameCount * 0.025)) * 255;
		int color = app.color(r, g, b);
		app.fill(color);

		switch (state) {
		case 1: // is alone
			morph(heptagon); //
			break;
		case 2: // two people
			morph(hextagon); //
			break;
		case 3: // three people
			morph(pentagon);
			break;
		case 4: // four people
			morph(square);
			break;
		case 5: // five people
			morph(triangle);
			break;
		case 6: // six people
			morph(circle);
			break;
		}
	}

	public void drawShape(ArrayList<PVector> vSet) {
		app.noStroke();
		app.pushMatrix();
		PShape s = app.createShape();
		// draw relative to the center of this person
		app.translate(centerX, centerY);
		s.beginShape();
		s.scale(.05f, .05f);
		// shape drawing
		for (PVector v : vSet)
			s.vertex(v.x, v.y);
		s.endShape(PConstants.CLOSE);
		// create this shape in its parent pApplet
		app.shape(s);
		app.popMatrix();
	}

	/**
	 * Creates a circle using vectors pointing from center
	 */
	public void initCircle() {
		int i = 0;
		for (int angle = 0; angle < 3600; angle += 6) {
			// Note we are not starting from 0 in order to match the
			// path of a circle.
			PVector v = PVector.fromAngle(PApplet.radians(angle - 135));
			v.mult(40);
			// crclSet.add(v);
			circle[i] = v;
			i++;
			// fill out morph ArrayList with blank PVectors
			// morphSet.add(new PVector());
		}
	}

	public PVector[] setupPolygon(int sides, int spokes, float rad) {
		PVector[] v; // all spokes from origin
		PVector[] spoke; // spokes to each corner ie used spokes
		PVector origin = new PVector(0, 0);
		float theta = PApplet.TWO_PI / sides; // angle between sides
		float thetaSpokes = PApplet.TWO_PI / spokes; // angle between spokes
		int currentSpoke = 1;
		PVector overlap; // working vector to tell if spokes too long

		spoke = new PVector[sides + 1];
		v = new PVector[spokes];

		for (int i = 0; i < sides; i++) { // set up n used spokes
			spoke[i] = PVector.fromAngle(theta * i);
			spoke[i].setMag(rad);
		}
		spoke[sides] = spoke[0]; // to loop around

		for (int i = 0; i < spokes; i++) { // set up spokes non used spokes
			v[i] = PVector.fromAngle(thetaSpokes * i);
			v[i].setMag(rad); // ful length so overlaps side
			// // if current angle past current used spoke, move on
			// to next spoke
			if ((i * thetaSpokes) > (currentSpoke * theta))
				currentSpoke++;
			overlap = lineIntersection(spoke[currentSpoke], spoke[currentSpoke - 1], origin, v[i]);
			if (overlap != null) {
				float distance = PApplet.dist(overlap.x, overlap.y, 0, 0);
				v[i].setMag(distance);
			}
		}
		return v;
	}

	// ref:
	// https://forum.processing.org/two/discussion/17094/how-to-make-a-circle-to-square-pattern-or-shape-to-another-shape-pattern
	public PVector lineIntersection(PVector p1, PVector p2, PVector p3, PVector p4) {
		PVector b = PVector.sub(p2, p1);
		PVector d = PVector.sub(p4, p3);

		float b_dot_d_perp = b.x * d.y - b.y * d.x;
		if (b_dot_d_perp == 0)
			return null;
		PVector c = PVector.sub(p3, p1);
		float t = (c.x * d.y - c.y * d.x) / b_dot_d_perp;
		if (t < 0 || t > 1)
			return null;
		float u = (c.x * b.y - c.y * b.x) / b_dot_d_perp;
		if (u < 0 || u > 1)
			return null;
		return new PVector(p1.x + t * b.x, p1.y + t * b.y);
	}

}
