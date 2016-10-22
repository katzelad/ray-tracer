
public class DirectedLight extends Light {

	public final Vector direction;

	public DirectedLight(Vector direction, Vector intensity) {
		super(intensity);
		this.direction = direction.norm();
	}

	@Override
	public Vector direction(Vector intersection) {
		return direction;
	}

	@Override
	public Vector intensity(Vector intersection) {
		return intensity;
	}

}
