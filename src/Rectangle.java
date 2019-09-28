import java.util.Arrays;

import javafx.geometry.Point2D;

public class Rectangle extends Surface {

	public final Vector p[], s[], n[], normal;
	public final double width, height;

	public Rectangle(Material mtl, Vector p0, Vector p1, Vector p2) {
		super(mtl);
		this.p = new Vector[] { p0, p1, p1.plus(p2).minus(p0), p2 };
		this.s = new Vector[] { p[1].minus(p[0]), p[2].minus(p[1]), p[3].minus(p[2]), p[0].minus(p[3]) };
		this.normal = s[0].cross(s[1]);
		this.n = new Vector[] { s[0].cross(normal), s[1].cross(normal), s[2].cross(normal), s[3].cross(normal) };
		this.width = p[0].distance(p[1]);
		this.height = p[0].distance(p[3]);
	}

	@Override
	public Ray intersect(Ray ray) {
		double t = p[0].minus(ray.origin).dot(normal) / ray.direction.dot(normal);
		if (t <= EPSILON)
			return null;
		Vector intersection = ray.origin.plus(ray.direction.mul(t));
		double dp[] = new double[4];
		for (int i = 0; i <= 3; i++) {
			dp[i] = intersection.minus(p[i]).dot(n[i]);
			for (int j = 0; j < i; j++)
				if (dp[i] * dp[j] < 0)
					return null;
		}
		return new Ray(intersection, ray.direction.dot(normal) < 0 ? normal : normal.minus());
	}

	@Override
	public Point2D flatten(Vector point) {
		double cos_p0 = s[0].norm().dot(s[3].norm());
		double ratio = point.distance(p[0]) / Math.sqrt(1 - cos_p0 * cos_p0);
		double cos_point_p0_p1 = point.minus(p[0]).norm().dot(s[0].norm());
		double cos_p0_point_p1 = p[0].minus(point).norm().dot(s[3].norm());
		double x = ratio * Math.sqrt(1 - cos_p0_point_p1 * cos_p0_point_p1) / width;
		double y = ratio * Math.sqrt(1 - cos_point_p0_p1 * cos_point_p0_p1) / height;
		return new Point2D(x, y);
	}

	@Override
	public String toString() {
		return Arrays.toString(p);
	}

}
