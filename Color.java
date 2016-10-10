
public class Color extends Vector {

	public Color(String[] args) {
		super(args);
	}

	public Color(double r, double g, double b) {
		super(r, g, b);
	}

	public int getRed() {
		return (int) (255 * x);
	}

	public int getGreen() {
		return (int) (255 * y);
	}

	public int getBlue() {
		return (int) (255 * z);
	}

	@Override
	public String toString() {
		return String.format("(%d, %d, %d)", x * 255, y * 255, z * 255);
	}

}
