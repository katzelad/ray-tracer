import java.util.ArrayList;
import java.util.List;

public class Scene {

	private Color bgCol;
	private Camera cam;
	private final List<Surface> surfaces = new ArrayList<>();
	private final List<DirectedLight> lights = new ArrayList<>();

	private int width, height;

	public void setCanvasSize(int height, int width) {
		this.width = width;
		this.height = height;
	}

	public void setBgCol(Color bgCol) {
		this.bgCol = bgCol;
	}

	public void setCam(Camera cam) {
		this.cam = cam;
	}

	public void addSurface(Surface rec) {
		surfaces.add(rec);
	}

	public void addLight(DirectedLight light) {
		lights.add(light);
	}

	private Color castRay(Vector origin, Vector direction) {
		Surface closest = null;
		double minDistance = Double.POSITIVE_INFINITY;
		for (Surface surface : surfaces) {
			Vector intersection = surface.intersect(origin, direction);
			if (intersection != null) {
				double distance = origin.distance(intersection);
				if (distance < minDistance) {
					minDistance = distance;
					closest = surface;
				}
			}
		}
		if (closest == null)
			return this.bgCol;
		return new Color(0, 0, 0);
	}

	public Color getColor(int x, int y) {
		Vector screenPixel = cam.screenCenter
				.plus(cam.upDirection.mul(((height - 1) * 0.5 - y) / width * cam.screenWidth))
				.plus(cam.leftDirection.mul(((width - 1) * 0.5 - x) / width * cam.screenWidth));
		return castRay(cam.eye, screenPixel.minus(cam.eye));
	}

}
