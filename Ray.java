
public class Ray {

	public final Vector origin, direction;

	public Ray(Vector origin, Vector direction) {
		this.origin = origin;
		this.direction = direction.norm();
	}

	public static Ray fromPoints(Vector src, Vector dst) {
		return new Ray(src, dst.minus(src));
	}

}
