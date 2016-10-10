
public abstract class Material {

	public final Color specular, emission, ambient;
	public final double shininess;

	public Material(Color specular, Color emission, Color ambient, double shininess) {
		this.specular = specular;
		this.emission = emission;
		this.ambient = ambient;
		this.shininess = shininess;
	}

}
