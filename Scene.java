import java.util.ArrayList;
import java.util.List;

public class Scene {

	private Vector bgCol;
	private Vector ambient;
	private Camera cam;
	private final List<Surface> surfaces = new ArrayList<>();
	private final List<DirectedLight> lights = new ArrayList<>();

	private int width, height;

	public void setCanvasSize(int height, int width) {
		this.width = width;
		this.height = height;
	}

	public void setBgCol(Vector bgCol) {
		this.bgCol = bgCol;
	}

	public void setAmbientLight(Vector ambientLight) {
		this.ambient = ambientLight;
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

	public Iterable<Surface> getSurfaces() {
		return surfaces;
	}

	private Vector castRay(Ray ray) {
		RayIntersection ri = RayIntersection.test(this, ray);
		if (ri == null)
			return this.bgCol;
		Material mtl = ri.surface.mtl;
		Vector intensity = mtl.emission.plus(mtl.ambient.mul(ambient));
		for (DirectedLight light : lights)
			if (RayIntersection.test(this, new Ray(ri.normal.origin, light.direction)) == null) {
				Vector lightIntensity = light.intensity;
				Vector lightDirection = light.direction.minus();
				Vector lightReflection = ri.normal.direction.mul(2 * lightDirection.dot(ri.normal.direction))
						.minus(lightDirection);
				Vector diffuse = mtl.diffuse(ri.normal.origin).mul(ri.normal.direction.dot(lightDirection))
						.mul(lightIntensity);
				Vector specular = mtl.specular.mul(Math.pow(ray.direction.minus().dot(lightReflection), mtl.shininess))
						.mul(lightIntensity);
				intensity = intensity.plus(diffuse).plus(specular);
			}
		return intensity;
	}

	public Vector getVector(int x, int y) {
		Vector screenPixel = cam.screenCenter
				.plus(cam.upDirection.mul(((height - 1) * 0.5 - y) / width * cam.screenWidth))
				.plus(cam.leftDirection.mul(((width - 1) * 0.5 - x) / width * cam.screenWidth));
		return castRay(Ray.fromPoints(cam.eye, screenPixel));
	}

}
