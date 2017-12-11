import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class ECG {

	PApplet app;
	int x = 0;
	float py = 0;
	float fx;
	ArrayList<PVector> ecgTrails;
	PVector pos;

	public ECG(PApplet app) {
		this.app = app;
		ecgTrails = new ArrayList<PVector>();
	}

	void update() {

	}

	void draw() {
//		
//		int trailLen = 0;
//		x += 5;
//		x %= app.width;
//		float fx = py + app.random(-80, 80);
//		pos = new PVector();
		
		app.pushMatrix();
		// app.frameRate(1);
		app.translate(-1f / 0.5f, StrangerLandApp.PROJECTOR_RATIO / 0.5f);
		app.scale(1 / StrangerLandApp.scaleX, 1 / StrangerLandApp.scaleY); // scale back

		app.noStroke();
		// app.fill(0, 0, 0, 15);
		app.fill(255, 25);
		app.rect(0, 0, app.width, app.height);
		app.translate(0, app.height / 2);
		x += 5;
		x %= app.width;
		float fx = py + app.random(-80, 80);
		// app.strokeWeight(app.random(1, 2));
		app.stroke(255, 10, 10);
		// app.noFill();
		app.line(x - 5, py, x, fx);
		if (py > app.height / 2)
			py = app.height / 2;
		if (py < -app.height / 2)
			py = -app.height / 2;
		// System.out.println(app.frameRate);
		app.popMatrix();

		// app.translate(0, -app.height / 2); // translate back
	}

}
