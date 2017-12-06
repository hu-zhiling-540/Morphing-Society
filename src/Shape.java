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

	long timeBorn = System.currentTimeMillis();

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

	public static final Color BABY_PINK = new Color(255, 182, 193);
	public static final Color HOT_PINK = new Color(255, 105, 180);
	public static final Color BLUE = new Color(168, 111, 186);
	public static final Color RED = new Color(232, 64, 170);
	public static final Color pureRed = new Color(255, 0, 0);
	public static final Color pureGreen = new Color(0, 255, 0);
	public static final Color pureBlue = new Color(0, 0, 255);

	public Shape(PApplet app) {
		this.app = app;
		// initialization for vertices set
		initCircle();
		// initSquare();
	}

	public void draw(boolean b) {
		app.fill(BLUE.getRGB());
		// drawShape();
		if (b)
			morph(regPolygon(4, 10));
		else
			morph(regPolygon(5, 15));

		// morph(sqrSet);
	}
	//
	// public void draw(int state) {
	// switch (state) {
	// case -1: // married, left person
	// // double check
	// if (isMarried && !isDivorced) {
	// // should genetrate random color?
	// app.fill(BABY_PINK.getRGB());
	// // halfHeart(true); // left heart
	// } else
	// System.out.println("Left heart should be displayed");
	// break;
	// case 0: // married, right person
	// if (isMarried && !isDivorced) {
	// app.fill(HOT_PINK.getRGB());
	// // halfHeart(false); // left heart
	// } else
	// System.out.println("Left heart should be displayed");
	// break;
	// case 1: // is alone
	// app.fill(BLUE.getRGB());
	// morph(sqrSet); //
	// break;
	// case 2: // more than one person
	// if (!isMarried || isDivorced) { // single
	// app.fill(BABY_PINK.getRGB());
	// morph(crclSet);
	// }
	// break;
	// case 3:
	// if (isDivorced && !isMarried) {
	// app.fill(BLUE.getRGB());
	// morph(crclSet);
	// }
	// break;
	// }
	// }

	// https://processing.org/examples/morph.html
	public void morph(ArrayList<PVector> vertices) {
		// Look at each vertex
		for (int i = 0; i < morphSet.size(); i++) {
			PVector v1;
			v1 = vertices.get(i);
			// Get the vertex we will draw
			PVector v2 = morphSet.get(i);
			// Lerp to the target
			v2.lerp(v1, (float) 0.05);
		}
		app.noStroke();
		app.pushMatrix();
		// shape that represents the new enter
		PShape s = app.createShape();
		// draw relative to the center of this person
		app.translate(centerX, centerY);
		s.beginShape();
		s.scale(.01f, .01f);
		// shape drawing
		for (PVector v : morphSet)
			s.vertex(v.x, v.y);
		s.endShape(PApplet.CLOSE);
		// create this shape in its parent pApplet
		app.shape(s);
		app.popMatrix();
	}

	public void update(Body body) {
		this.body = body;
		head = body.getJoint(Body.HEAD);
		spineBase = body.getJoint(Body.SPINE_BASE);
		if (head != null && spineBase != null) {
			centerX = spineBase.x;
			centerY = spineBase.y;
		}

		// // if married stated
		// if (isMarried) {
		// text = "We fall in love!";
		// newTraces.add(new PVector(centerX, centerY));
		// if (newTraces.size() > 20) // have been together for a long time
		// text = "I love you so much.";
		// if (newTraces.size() > 80) // have been together for a long long time
		// text = "I love you so so much.";
		// // just got married
		// if (this.isMarried != isMarried) {
		// this.isDivorced = false;
		// // if never married to this person
		// if (!marriedTo.contains(partnerId)) {
		// marriedTo.add(partnerId);
		// newTraces = new ArrayList<PVector>();
		// } else // married to the same person again
		// text = "Can't believe we fall in love again!"; // override the displaying
		// text
		// }
		// } else if (isDivorced) {
		// if (newTraces.size() <= 10) // Just break up
		// text = "We should take a break.";
		// else // return to the game
		// text = "Single.";
		// // just got divorced
		// if (this.isDivorced != isDivorced) {
		// this.isDivorced = isDivorced;
		// this.isMarried = false;
		// // remember all the traces being together
		// exTraces = newTraces;
		// newTraces = new ArrayList<PVector>(); // record new traces until meeting the
		// next partner
		// }
		// newTraces.add(new PVector(centerX, centerY));
		// }
	}

	/**
	 * Creates a circle using vectors pointing from center
	 */
	public void initCircle() {
		for (int angle = 0; angle < 360; angle += 60) {
			// Note we are not starting from 0 in order to match the
			// path of a circle.
			PVector v = PVector.fromAngle(PApplet.radians(angle - 135));
			v.mult(15);
			crclSet.add(v);
			// fill out morph ArrayList with blank PVectors
			morphSet.add(new PVector());
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

	public static PVector getRotate(PVector vec, double theta) {
		float x = (float) (vec.x * Math.cos(theta) - vec.y * Math.sin(theta));
		float y = (float) (vec.x * Math.sin(theta) + vec.y * Math.cos(theta));
		return new PVector(x, y);
	}

	public ArrayList<PVector> regPolygon(int nPoints, float rad) {
		ArrayList<PVector> points = new ArrayList<PVector>();
		PVector vertex = new PVector(0, -rad);
		points.add(vertex);
		double rot = 2 * Math.PI / nPoints;
		for (int i = 1; i < 6; i += 1) {
			PVector next = getRotate(vertex, rot);
			points.add(next);
			vertex = next;
		}
		return points;
	}

	public ArrayList<PVector> regPolygon(int npoints) {
		float angle = PConstants.TWO_PI / npoints;
		ArrayList<PVector> vSet = new ArrayList<PVector>();
		float rad = 1f;
		for (float a = 0; a < PConstants.TWO_PI; a += angle) {
			float sx = centerX + PApplet.cos(a) * rad;
			float sy = centerY + PApplet.sin(a) * rad;
			// app.vertex(sx, sy);
			vSet.add(new PVector(sx, sy));
		}
		return vSet;
	}

}
