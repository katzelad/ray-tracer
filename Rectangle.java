import java.util.Arrays;

public class Rectangle extends Surface {

	public final Vector p[], s[], n[], normal;

	public Rectangle(Vector p0, Vector p1, Vector p2) {
		this.p = new Vector[] { p0, p1, p1.plus(p2).minus(p0), p2 };
		this.s = new Vector[] { p[1].minus(p[0]), p[2].minus(p[1]), p[3].minus(p[2]), p[0].minus(p[3]) };
		this.normal = p1.minus(p0).cross(p2.minus(p0));
		this.n = new Vector[] { s[0].cross(normal), s[1].cross(normal), s[2].cross(normal), s[3].cross(normal) };
	}

	@Override
	public Vector intersect(Vector origin, Vector direction) {
		Vector intersection = origin.plus(direction.mul(p[0].minus(origin).dot(normal) / direction.dot(normal)));
		double dp[] = new double[4];
		for (int i = 0; i <= 3; i++) {
			dp[i] = intersection.minus(p[i]).dot(n[i]);
			for (int j = 0; j < i; j++)
				if (dp[i] * dp[j] < 0)
					return null;
		}
		return intersection;
	}

	@Override
	public String toString() {
		return Arrays.toString(p);
	}

}