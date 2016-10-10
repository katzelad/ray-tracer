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

	private Color castRay(Ray ray) {
		Surface closestSurface = null;
		Ray closestIntersection = null;
		double minDistance = Double.POSITIVE_INFINITY;
		for (Surface surface : surfaces) {
			Ray intersection = surface.intersect(ray);
			if (intersection != null) {
				double distance = ray.origin.distance(intersection.origin);
				if (distance < minDistance) {
					minDistance = distance;
					closestSurface = surface;
					closestIntersection = intersection;
				}
			}
		}
		if (closestSurface == null)
			return this.bgCol;
		Color 
		return new Color(0, 0, 0);
	}

	public Color getColor(int x, int y) {
		Vector screenPixel = cam.screenCenter
				.plus(cam.upDirection.mul(((height - 1) * 0.5 - y) / width * cam.screenWidth))
				.plus(cam.leftDirection.mul(((width - 1) * 0.5 - x) / width * cam.screenWidth));
		return castRay(Ray.fromPoints(cam.eye, screenPixel));
	}

}
