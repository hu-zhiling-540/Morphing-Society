import java.awt.Color;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Shape {

	Body body;
	PApplet app;
	int color;

	float centerX;
	float centerY;

	PVector head;
	PVector spineBase;

	long timeBorn;

	float angle = 0;
	float aVelocity = 0;
	float aAcceleration = 0;

	// store the vertices for shapes
	ArrayList<PVector> crclSet = new ArrayList<PVector>();
	ArrayList<PVector> sqrSet = new ArrayList<PVector>();
	// store the vertices that been lerped to
	ArrayList<PVector> morphSet = new ArrayList<PVector>();

	// to store all the locations past by until meeting that married person
	ArrayList<PVector> exTraces = new ArrayList<PVector>();
	ArrayList<PVector> newTraces = new ArrayList<PVector>();

	// potential shapes
	PVector[] heptagon;
	PVector[] hextagon;
	PVector[] pentagon;
	PVector[] square;
	PVector[] triangle;
	PVector[] circle;
	PVector[] currShape;

	int maxSize = 600;
	int rad = 50;
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

		// initSquare();
		heptagon = setupPolygon(7, maxSize, rad);
		// hextagon = setupPolygon(6, maxSize, rad);
		// circle = new PVector[maxSize];
		// circle = setupPolygon(0, maxSize, rad);
		// pentagon = setupPolygon(5, maxSize, rad);
		// square = setupPolygon(4, maxSize, rad);
		// triangle = setupPolygon(3, maxSize, rad);
		// initialization for vertices set
		// initCircle();
		currShape = new PVector[maxSize];
		currShape = heptagon;
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
		switch (state) {
		case 1: // is alone
			app.fill(BLUE.getRGB());
			morph(heptagon); //
			break;
		case 2: // two people
			app.fill(BLUE.getRGB());
			morph(hextagon); //
			break;
		case 3: // three people
			app.fill(BABY_PINK.getRGB());
			morph(pentagon);
			break;
		case 4: // four people
			app.fill(BLUE.getRGB());
			morph(square);
			break;
		case 5: // five people
			app.fill(BLUE.getRGB());
			morph(triangle);
			break;
		case 6: // six people
			app.fill(BLUE.getRGB());
			morph(circle);
			break;
		}
	}

	public void update(Body body) {
		this.body = body;

		head = body.getJoint(Body.HEAD);
		spineBase = body.getJoint(Body.SPINE_BASE);
		if (head != null && spineBase != null) {
			// System.out.println(rad);
			centerX = spineBase.x;
			centerY = spineBase.y;
			int newRad = (int) (head.dist(spineBase) * 50); // update radius
			if (Math.abs(newRad - rad) >= 2) {
				rad = newRad;
				heptagon = setupPolygon(7, maxSize, rad);
				hextagon = setupPolygon(6, maxSize, rad);
				pentagon = setupPolygon(5, maxSize, rad);
				square = setupPolygon(4, maxSize, rad);
				triangle = setupPolygon(3, maxSize, rad);
			}
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

	public PVector[] setupPolygon(int sides, int spokes, int radius) {
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
			spoke[i].setMag(radius);
		}
		spoke[sides] = spoke[0]; // to loop around

		for (int i = 0; i < spokes; i++) { // set up spokes non used spokes
			v[i] = PVector.fromAngle(thetaSpokes * i);
			v[i].setMag(radius); // ful length so overlaps side
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
