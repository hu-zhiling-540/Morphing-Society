import processing.core.PApplet;
import processing.core.PFont;

public class HelloWorld {
	PApplet app;

	PFont font;
	float x, y;
	float rad;
	float speed;
	int color;

	String s1 = "Access Denied";
	String s2 = "Hello World";

	public HelloWorld(PApplet app, float x, float y, float mass) {
		this.app = app;
		this.x = x;
		this.y = y;
		// app.width = 1440
		speed = (float) (app.random(1, app.width) * 0.001);
		rad = (float) (mass * 0.01);
		color = app.color(32, 194, 14);
		font = app.createFont("Monospace", 5);
		// y = app.random(app.height);
		// x = -rad * 2;// start off the screen
	}

	void move() {
		// Increment by speed
		x += speed;
	}

	// Check if it hits the bottom
	boolean reachedBottom() {
		// If we go a little beyond the bottom
		if (x > app.height) {
			return true;
		} else {
			return false;
		}
	}

	void display() {
		// Display the drop
		app.fill(color);
		// app.noStroke();
		app.stroke(1);
		app.pushMatrix();
		app.translate(x, y);
		app.scale(0.01f, -0.01f);
		app.text(s2, x, y);
		app.popMatrix();
	}

	// If the drop is caught
	void caught() {
		// Stop it from moving by setting speed equal to zero
		speed = 0;
		// Set the location to somewhere way off-screen
		y = -1000;
	}
}
