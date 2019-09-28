
public abstract class Light {

	public final Vector intensity;

	public Light(Vector intensity) {
		this.intensity = intensity;
	}

	public abstract Vector direction(Vector intersection);

	public abstract Vector intensity(Vector intersection);

}
