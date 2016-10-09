
public class Vector {

	public final Double x, y, z;

	public Vector(String[] args) {
		x = Double.parseDouble(args[0]);
		y = Double.parseDouble(args[1]);
		z = Double.parseDouble(args[2]);
	}

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector norm() {
		return this.div(this.length());
	}

	public Vector mul(Double factor) {
		return new Vector(factor * x, factor * y, factor * z);
	}

	public Vector div(double factor) {
		return this.mul(1 / factor);
	}

	public Vector plus(Vector other) {
		return new Vector(x + other.x, y + other.y, z + other.z);
	}

	public Vector minus(Vector other) {
		return new Vector(x - other.x, y - other.y, z - other.z);
	}

	public double dot(Vector other) {
		return x * other.x + y * other.y + z * other.z;
	}

	public Vector cross(Vector other) {
		return new Vector(y * other.z - other.y * z, z * other.x - other.z * x, x * other.y - other.x * y);
	}

	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double distance(Vector other) {
		return this.minus(other).length();
	}

	@Override
	public String toString() {
		return String.format("(%.2f, %.2f, %.2f)", x, y, z);
	}

}
