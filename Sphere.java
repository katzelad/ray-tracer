
public class Sphere extends Surface {

	private final Vector center;
	private final double radius;

	public Sphere(Material mtl, Vector center, double radius) {
		super(mtl);
		this.center = center;
		this.radius = radius;
	}

	@Override
	public Ray intersect(Ray ray) {
		double b = 2 * ray.direction.dot(ray.origin.minus(center));
		double center2origin = ray.origin.minus(center).length();
		double c = center2origin * center2origin - radius * radius;
		if (b * b < 4 * c)
			return null;
		double delta = Math.sqrt(b * b - 4 * c), t1 = (delta - b) / 2, t2 = (-delta - b) / 2;
		if (t1 <= 0 && t2 <= 0)
			return null;
		double t = t1 <= 0 ? t2 : t2 <= 0 ? t1 : Math.min(t1, t2);
		Vector intersection = ray.origin.plus(ray.direction.mul(t));
		return new Ray(intersection, intersection.minus(center));
	}

}
