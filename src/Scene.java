import java.util.ArrayList;
import java.util.List;

public class Scene {

	private Vector bgCol;
	private Vector ambient;
	private Camera cam;
	private final List<Surface> surfaces = new ArrayList<>();
	private final List<Light> lights = new ArrayList<>();
	private int ssWidth;

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

	public void setSsWidth(int ssWidth) {
		this.ssWidth = ssWidth;
	}

	public void setCam(Camera cam) {
		this.cam = cam;
	}

	public void addSurface(Surface rec) {
		surfaces.add(rec);
	}

	public void addLight(Light light) {
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
		for (Light light : lights) {
			Vector toLightDirection = light.direction(ri.normal.origin).minus();
			if (RayIntersection.test(this, new Ray(ri.normal.origin, toLightDirection)) == null) {
				Vector lightIntensity = light.intensity(ri.normal.origin);
				Vector lightReflection = ri.normal.direction.mul(2 * toLightDirection.dot(ri.normal.direction))
						.minus(toLightDirection);
				Vector diffuse = mtl.diffuse(ri.surface.flatten(ri.normal.origin))
						.mul(ri.normal.direction.dot(toLightDirection)).mul(lightIntensity);
				Vector specular = mtl.specular.mul(Math.pow(ray.direction.minus().dot(lightReflection), mtl.shininess))
						.mul(lightIntensity);
				intensity = intensity.plus(diffuse).plus(specular);
			}
		}
		if (mtl.reflectance > 0) {
			Vector reflection = ri.normal.direction.mul(2 * ri.normal.direction.dot(ray.direction.minus()))
					.plus(ray.direction);
			intensity = intensity.plus(castRay(new Ray(ri.normal.origin, reflection)).mul(mtl.reflectance));
		}
		return intensity;
	}

	public Vector getColor(int x, int y) {
		Vector colorSum = new Vector(0, 0, 0);
		for (double i = 0; i < ssWidth; i++)
			for (double j = 0; j < ssWidth; j++) {
				Vector screenPixel = cam.screenCenter
						.plus(cam.upDirection.mul(((height - 1) * 0.5 - y - j / ssWidth) / width * cam.screenWidth))
						.plus(cam.leftDirection.mul(((width - 1) * 0.5 - x - i / ssWidth) / width * cam.screenWidth));
				colorSum = colorSum.plus(castRay(Ray.fromPoints(cam.eye, screenPixel)));
			}
		return colorSum.div(ssWidth * ssWidth);
	}

}
