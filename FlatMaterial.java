
public class FlatMaterial extends Material {

	public final Vector diffuse;

	public FlatMaterial(Vector diffuse, Vector specular, Vector emission, Vector ambient, double shininess) {
		super(specular, emission, ambient, shininess);
		this.diffuse = diffuse;
	}

	@Override
	public Vector diffuse(Vector point) {
		return diffuse;
	}

}
