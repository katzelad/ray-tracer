
public class CheckersMaterial extends Material {

	public final double size;
	public final Vector diffuse1, diffuse2;

	public CheckersMaterial(Vector specular, Vector emission, Vector ambient, double shininess, double size,
			double reflectance, Vector diffuse1, Vector diffuse2) {
		super(specular, emission, ambient, shininess, reflectance);
		this.size = size;
		this.diffuse1 = diffuse1;
		this.diffuse2 = diffuse2;
	}

	@Override
	public Vector diffuse(Point point) {
		return ((int) (point.x / size) + (int) (point.y / size) & 1) == 0 ? diffuse2 : diffuse1;
	}

}
