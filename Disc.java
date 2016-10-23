import javafx.geometry.Point2D;

public class Disc extends Surface {

	public final Vector center, normal;
	private final Vector up, left;
	public final double radius;

	public Disc(Material mtl, Vector center, Vector normal, double radius) {
		super(mtl);
		this.center = center;
		this.normal = normal.norm();
		this.radius = radius;
		this.up = normal.cross(new Vector(3, 2, 5)).norm();
		this.left = normal.cross(up).norm();
	}

	@Override
	public Ray intersect(Ray ray) {
		double t = center.minus(ray.origin).dot(normal) / ray.direction.dot(normal);
		if (t < 0)
			return null;
		Vector intersection = ray.origin.plus(ray.direction.mul(t));
		if (intersection.distance(center) > radius)
			return null;
		return new Ray(intersection, ray.direction.dot(normal) < 0 ? normal : normal.minus());
	}

	@Override
	public Point2D flatten(Vector point) {
		double angle = Math.acos(up.dot(point.minus(center).norm()));
		if (left.dot(point) < left.dot(center))
			angle *= -1;
		return new Point2D(point.distance(center) / radius, (angle / Math.PI + 1) / 2);
	}

}
