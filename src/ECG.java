import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class ECG {

	PApplet app;

	int currX = 0;
	float lastY = 0;
	float motionY = 0;
	float motionX = 2;

	ArrayList<PVector> ecgTrails;
	int maxSize = 20;

	public ECG(PApplet app) {
		this.app = app;
		ecgTrails = new ArrayList<PVector>();
	}

	void update(float motion) {
		this.motionY = motion;
	}

	void draw() {
		app.pushMatrix();
		app.translate(-1f / 0.5f, StrangerLandApp.PROJECTOR_RATIO / 0.5f);
		app.scale(1 / StrangerLandApp.scaleX, 1 / StrangerLandApp.scaleY);

		app.translate(0, app.height / 2);
		int trailLen = 0;
		currX += motionX;
		currX %= app.width;
		float currY = lastY + motionY;
		ecgTrails.add(new PVector(currX, currY));

		trailLen = ecgTrails.size() - 2;
		if (trailLen >= 1) {
			for (int i = 0; i < trailLen; i++) {
				PVector currentTrail = ecgTrails.get(i);
				PVector previousTrail = ecgTrails.get(i + 1);
				app.strokeWeight(app.random(1, 3));
				app.stroke(255 * i / trailLen, 0, 0);
				if (currentTrail.x >= app.width - motionX)
					break;
				app.line(currentTrail.x, currentTrail.y, previousTrail.x, previousTrail.y);
			}
			if (trailLen >= maxSize)
				ecgTrails.remove(0);
		}
		app.popMatrix();

		// app.translate(0, -app.height / 2); // translate back
	}

}
