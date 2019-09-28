import javafx.geometry.Point2D;

public class Cylinder extends Surface {

	public final Vector start, direction;
	private final Vector normal, halfNormal;
	public final double length, radius;

	public Cylinder(Material mtl, Vector start, Vector direction, double length, double radius) {
		super(mtl);
		this.start = start;
		this.direction = direction.norm();
		this.length = length;
		this.radius = radius;
		this.normal = direction.cross(new Vector(3, 2, 5)).norm();
		this.halfNormal = direction.cross(normal).norm();
	}

	@Override
	public Ray intersect(Ray ray) {
		double u_dot_d = ray.direction.dot(direction);
		double a = 1 - u_dot_d * u_dot_d;
		Vector p_minus_s = ray.origin.minus(start);
		double p_minus_s_dot_d = p_minus_s.dot(direction);
		double b = 2 * (ray.direction.dot(p_minus_s) - p_minus_s_dot_d * u_dot_d);
		double c = p_minus_s.dot(p_minus_s) - p_minus_s_dot_d * p_minus_s_dot_d - radius * radius;
		if (b * b < 4 * a * c)
			return null;
		double delta = Math.sqrt(b * b - 4 * a * c), t1 = (delta - b) / 2 / a, t2 = (-delta - b) / 2 / a;
		if (t1 <= EPSILON && t2 <= EPSILON)
			return null;
		double t = t1 <= EPSILON ? t2 : t2 <= EPSILON ? t1 : Math.min(t1, t2);
		double s = u_dot_d * t + p_minus_s_dot_d;
		if (s < 0 || s > length) {
			if (t1 <= EPSILON || t2 <= EPSILON)
				return null;
			t = Math.max(t1, t2);
			s = u_dot_d * t + p_minus_s_dot_d;
			if (s < 0 || s > length)
				return null;
		}
		Vector intersection = ray.origin.plus(ray.direction.mul(t));
		return new Ray(intersection, intersection.minus(start.plus(direction.mul(s))));
	}

	@Override
	public Point2D flatten(Vector point) {
		double t = point.minus(start).dot(direction);
		double angle = Math.acos(normal.dot(point.minus(start.plus(direction.mul(t))).norm()));
		if (halfNormal.dot(point) < halfNormal.dot(start))
			angle *= -1;
		return new Point2D(t / length, (angle / Math.PI + 1) / 2);
	}

}
