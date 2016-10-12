
public abstract class Material {

	public final Vector specular, emission, ambient;
	public final double shininess;

	public Material(Vector specular, Vector emission, Vector ambient, double shininess) {
		this.specular = specular;
		this.emission = emission;
		this.ambient = ambient;
		this.shininess = shininess;
	}
	
	public abstract Vector diffuse(Vector point);

}
