
public class DirectedLight {

	public final Vector direction;
	public final Vector intensity;

	public DirectedLight(Vector direction, Vector �intensity) {
		this.direction = direction.norm();
		this.intensity = �intensity;
	}

}
