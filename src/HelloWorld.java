import processing.core.PApplet;
import processing.core.PFont;

public class HelloWorld {
	PApplet app;

	PFont font;
	float textX, textY;
	float cx, cy;
	float rad = .5f;
	float speed;
	int color;

	String s1 = "ACESS DENIED";
	String s2 = "HELLO WORLD";

	float scaleX, scaleY;

	public HelloWorld(PApplet app, float x, float y, float scaleX, float scaleY) {
		this.app = app;
		this.scaleX = scaleX;
		this.scaleY = scaleY;

		textX = x;
		textY = app.height / (scaleY * -2);

		speed = (float) (app.random(1, app.height) / (scaleY * -2));

		this.cx = x;
		this.cy = y;

		// rad = .2f;
		color = app.color(32, 194, 14);
		font = app.createFont("Monospace", 20);
	}

	void update() {
		this.textY -= speed;
	}

	// Check if it hits the bottom
	boolean comeAcross() {
		// If we go a little beyond the bottom
		if (textY < -2) {
			return true;
		} else {
			return false;
		}
	}

	void draw() {

		app.fill(color);
		app.textFont(font);
		app.textAlign(PApplet.CENTER);

		app.pushMatrix();
		app.translate(textX, textY);
		app.scale(1 / scaleX, 1 / scaleY); // scale back

		float tx = textX;
		float ty = 0;
		for (int i = 0; i < s2.length(); i++) {
			app.text(s2.charAt(i), tx, ty);
			System.out.println("c: " + s2.charAt(i) + " _width: " + app.textWidth(s2.charAt(i)));
			System.out.println("txt" + app.textAscent());
			ty += app.textAscent();
			System.out.println("ty/" + ty / (scaleY));
			System.out.println("endy" + (cy - ty / scaleY));
			// if (ty / (scaleY) < (cy - ty / scaleY) - rad * 0.01)
			if (ty + 25 > (cy -textY) * scaleY )
				break;
		}
		app.popMatrix();

		app.pushMatrix();
		app.translate(textX, cy);
		ty = cy + 50f;
		app.scale(1 / scaleX, 1 / scaleY); // scale back
		for (int i = 0; i < s1.length(); i++) {
			app.text(s1.charAt(i), tx, ty);
			ty += app.textAscent();
		}
		app.popMatrix();
	}

}
