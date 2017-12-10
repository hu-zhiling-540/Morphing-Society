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

	public WorldMsg(PApplet app, float x, float y, float rad) {
		this.app = app;

		textX = x;
		textY = app.height / (StrangerLandApp.scaleY * -2);

		speed = (float) (app.random(1, app.height) / (StrangerLandApp.scaleY * -2));

		this.cx = x;
		this.cy = y;

		this.rad = rad;
		color = app.color(32, 194, 14);
		font = app.createFont("Monospace", 15);
	}

	void update() {
		this.textY -= speed;
	}

	void incoming() {

		app.fill(color);
		app.textFont(font);
		app.textAlign(PApplet.CENTER);

		app.pushMatrix();
		app.translate(textX, textY);
		app.scale(1 / StrangerLandApp.scaleX, 1 / StrangerLandApp.scaleY); // scale back

		float tx = textX;
		float ty = 0;
		for (int i = 0; i < s1.length(); i++) {
			ty += app.textAscent();
			if (ty + rad * 2 > (cy - textY) * StrangerLandApp.scaleY)
				break;
			app.text(s1.charAt(i), tx, ty);

		}
		app.popMatrix();

	}

	void outgoing(boolean isFamiliar) {
		String msg = s2;
		if (isFamiliar)
			msg = s1;
		app.pushMatrix();
		app.translate(textX, cy);
		float tx = textX;
		float ty = cy + rad * 5;
		app.scale(1 / StrangerLandApp.scaleX, 1 / StrangerLandApp.scaleY); // scale back
		for (int i = 0; i < msg.length(); i++) {
			app.text(msg.charAt(i), tx, ty);
			ty += app.textAscent();
		}
		app.popMatrix();
	}

}
