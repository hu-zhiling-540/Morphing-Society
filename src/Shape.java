import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

/**
 * @author Zhiling
 *
 */
public class Shape {

	PApplet app;

	PVector head, spineBase;

	float centerX, centerY;

	long timeBorn;

	float rotSpeed = (float) 0;
	float decel = 0.2f; // deceleration
	float minSpeed = 0.01f;
	float rad = 20; // radius

	// potential shapes represented by fixed size of vertices
	PVector[] heptagon;
	PVector[] hextagon;
	PVector[] pentagon;
	PVector[] square;
	PVector[] triangle;
	PVector[] circle;
	PVector[] currShape;

	private final int maxSize = 600; // fixed count of vertices

	public Shape(PApplet app) {
		this.app = app;

		heptagon = setupPolygon(7, maxSize, rad);
		initCircle();
		currShape = new PVector[maxSize];
		currShape = heptagon;
	}

	/**
	 * Updates current location
	 * 
	 * @param centerX
	 * @param centerY
	 * @param rad
	 */
	public void update(float centerX, float centerY, float rad) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.rad = rad;
		// ease out rotSpeed speed
		if (decel > minSpeed)
			decel -= 0.001;
		rotSpeed += decel;

		heptagon = setupPolygon(7, maxSize, rad);
		hextagon = setupPolygon(6, maxSize, rad);
		pentagon = setupPolygon(5, maxSize, rad);
		square = setupPolygon(4, maxSize, rad);
		triangle = setupPolygon(3, maxSize, rad);
	}

	/**
	 * Draws based on current population
	 * 
	 * @param state
	 */
	public void draw(int state) {
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

	/**
	 * Morph from current shape to next state. Reference:
	 * https://processing.org/examples/morph.html
	 * 
	 * @param nextShape
	 * 
	 */
	public void morph(PVector[] nextShape) {
		for (int i = 0; i < maxSize; i++) {
			PVector v1 = nextShape[i];
			// Get the next vertex we will draw to
			PVector v2 = currShape[i];
			// Lerp to the target
			v2.lerp(v1, (float) 0.5);
		}
		app.pushMatrix();
		PShape s = app.createShape();
		s.rotate(rotSpeed);
		s.beginShape();
		s.scale(.01f, -.01f);
		s.stroke(255); // white outline
		s.strokeWeight(1);
		s.fill(1, 100); // transparent
		// draw relative to the center of this person
		app.translate(centerX, centerY);
		for (PVector v : currShape) // drawing shape
			s.vertex(v.x, v.y);
		s.endShape(PApplet.CLOSE);
		// create this shape in its parent pApplet
		app.shape(s);
		app.popMatrix();
	}

	/**
	 * Creates a circle using vectors pointing from center
	 */
	public void initCircle() {
		circle = new PVector[maxSize];

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

	/**
	 * Sets up polygon based on vertices count and rad. reference:
	 * https://forum.processing.org/two/discussion/17094/how-to-make-a-circle-to-square-pattern-or-shape-to-another-shape-pattern
	 * 
	 * @param sides
	 * @param spokes
	 * @param rad
	 * @return
	 * 
	 */
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

	public void speedUp() {
		decel += 0.002;
	}

	public void speedDown() {
		decel -= 0.001;
	}

}
