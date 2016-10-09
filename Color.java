
public class Color {

	public final double r, g, b;

	public Color(String[] args) {
		r = Double.parseDouble(args[0]);
		g = Double.parseDouble(args[1]);
		b = Double.parseDouble(args[2]);
	}

	public Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d, %d)", r * 255, g * 255, b * 255);
	}

}
