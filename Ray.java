
public class Ray {

	public final Vector origin, direction;

	public Ray(Vector origin, Vector direction) {
		this.origin = origin;
		this.direction = direction.norm();
	}

	@Override
	public String toString() {
		return "origin: " + origin + ", direction: " + direction;
	}

	public static Ray fromPoints(Vector src, Vector dst) {
		return new Ray(src, dst.minus(src));
	}

}
