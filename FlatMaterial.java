
public class FlatMaterial extends Material {

	public final Color diffuse;

	public FlatMaterial(Color diffuse, Color specular, Color emission, Color ambient, double shininess) {
		super(specular, emission, ambient, shininess);
		this.diffuse = diffuse;
	}

}
