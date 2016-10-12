
public class DirectedLight {

	public final Vector direction;
	public final Vector intensity;

	public DirectedLight(Vector direction, Vector ïintensity) {
		this.direction = direction.norm();
		this.intensity = ïintensity;
	}

}
