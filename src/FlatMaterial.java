import javafx.geometry.Point2D;

public class FlatMaterial extends Material {

	public final Vector diffuse;

	public FlatMaterial(Vector diffuse, Vector specular, Vector emission, Vector ambient, double shininess,
			double reflectance) {
		super(specular, emission, ambient, shininess, reflectance);
		this.diffuse = diffuse;
	}

	@Override
	public Vector diffuse(Point2D point) {
		return diffuse;
	}

}
