import processing.core.PApplet;
import processing.core.PFont;

public class WorldMsg {
	PApplet app;

	PFont font;
	float textX, textY;
	float cx, cy;
	float rad = .5f;
	float speed;
	int color;

	String s1 = "HELLO WORLD";
	String s2 = "ACESS DENIED";

	public WorldMsg(PApplet app, float x, float y) {
		this.app = app;

		textX = x;
		textY = app.height / (StrangerLandApp.scaleY * -2);

		speed = (float) (app.random(1, app.height) / (StrangerLandApp.scaleY * -2));

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
		app.scale(1 / StrangerLandApp.scaleX, 1 / StrangerLandApp.scaleY); // scale back

		float tx = textX;
		float ty = 0;
		for (int i = 0; i < s1.length(); i++) {
			app.text(s1.charAt(i), tx, ty);
			ty += app.textAscent();
			if (ty + 25 > (cy - textY) * StrangerLandApp.scaleY)
				break;
		}
		app.popMatrix();

		app.pushMatrix();
		app.translate(textX, cy);
		ty = cy + 50f;
		app.scale(1 / StrangerLandApp.scaleX, 1 / StrangerLandApp.scaleY); // scale back
		for (int i = 0; i < s2.length(); i++) {
			app.text(s2.charAt(i), tx, ty);
			ty += app.textAscent();
		}
		app.popMatrix();
	}

}
