import javafx.geometry.Point2D;

public abstract class Material {

	public final Vector specular, emission, ambient;
	public final double shininess, reflectance;

	public Material(Vector specular, Vector emission, Vector ambient, double shininess, double reflectance) {
		this.specular = specular;
		this.emission = emission;
		this.ambient = ambient;
		this.shininess = shininess;
		this.reflectance = reflectance;
	}

	public abstract Vector diffuse(Point2D point);

}
