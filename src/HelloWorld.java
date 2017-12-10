import processing.core.PApplet;
import processing.core.PFont;

public class HelloWorld {
	PApplet app;

	PFont font;
	float x, y;
	float cx, cy;
	float rad = 5;
	float speed;
	int color;

	String s1 = "ACESS DENIED";
	String s2 = "HELLO WORLD";

	float scaleX, scaleY;

	public HelloWorld(PApplet app, float x, float y, float sx, float sy) {
		this.app = app;
		this.x = x;
		this.y = 2f;
		this.scaleX = sx;
		this.scaleY = sy;
		this.cx = x;
		this.cy = y;

		// app.width = 1440
		speed = (float) (app.random(1, 10) * 0.1);
		// rad = (float) (mass * 0.01);
		color = app.color(32, 194, 14);
		font = app.createFont("Monospace", 20);
		// y = app.random(app.height);
		// x = -rad * 2;// start off the screen
	}

	void update(float x, float y) {
		cx = x;
		cy = y;
		this.y -= speed;
		System.out.println("afte update " + this.y);
	}

	// Check if it hits the bottom
	boolean reachedBottom() {
		// If we go a little beyond the bottom
		if (y < -2) {
			return true;
		} else {
			return false;
		}
	}

	void draw() {

		app.fill(color);
		app.pushMatrix();
		app.translate(x, y);
		app.scale(1 / scaleX, 1 / scaleY); // scale back
		System.out.println("after" + x + "y" + y);
		app.textFont(font);
		app.textAlign(PApplet.CENTER);

		float tx = x;
		float ty = y;
		System.out.println("ty" + ty);
		for (int i = 0; i < s2.length(); i++) {
			app.text(s2.charAt(i), tx, ty);
			System.out.println("w:" + app.textWidth(s2.charAt(i)));
			System.out.println(s2.charAt(i));
			ty += app.textAscent();
			System.out.println("ty/" + ty / scaleY);
			System.out.println("endy" + cy);

			if (ty / scaleY < cy)
				break;
		}

		// for (int i = 0; i < s1.length(); i++) {
		// app.text(s1.charAt(i), tx, ty);
		// System.out.println("w:" + app.textWidth(s1.charAt(i)));
		// ty += app.textAscent();
		// System.out.println("ty" + ty);
		// System.out.println("endy" + cy);
		// //
		// // if (ty * -0.01 < cy)
		// // break;
		// }
		app.popMatrix();
	}

}
