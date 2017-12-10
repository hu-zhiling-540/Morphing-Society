import processing.core.PApplet;
import processing.core.PFont;

public class HelloWorld {
	PApplet app;

	PFont font;
	float x, y;
	float rad = 5;
	float speed;
	int color;

	String s1 = "Access Denied";
	String s2 = "HELLO WORLD";

	public HelloWorld(PApplet app) {
		this.app = app;
		this.x = app.random(-2, 2);
		this.y = 2;
		// app.width = 1440
		speed = (float) (0.05);
		// rad = (float) (mass * 0.01);
		color = app.color(32, 194, 14);
		font = app.createFont("Monospace", 10);
		// y = app.random(app.height);
		// x = -rad * 2;// start off the screen
	}

	void move() {
		// Increment by speed
		y -= speed;
	}

	// Check if it hits the bottom
	boolean reachedBottom() {
		// If we go a little beyond the bottom
		if (y < -10) {
			return true;
		} else {
			return false;
		}
	}

	void display() {
		// Display the drop
		app.fill(color);
		// app.noStroke();
		// app.stroke(10);
		app.pushMatrix();
		app.translate(x, y);
		app.scale(0.01f, -0.01f);
		app.textFont(font);
		app.textAlign(PApplet.CENTER);
		float tx = x;
		float ty = y;
		for (int i = 0; i < s2.length(); i++) {
			app.text(s2.charAt(i), tx, ty);
			System.out.println("w:" + app.textWidth(s2.charAt(i)));
			ty += app.textAscent();
		}
		app.popMatrix();
	}

	// If the drop is caught
	void caught() {
		// Stop it from moving by setting speed equal to zero
		speed = 0;
		// Set the location to somewhere way off-screen
		y = 100;
	}
}
