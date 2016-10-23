
public class PointLight extends Light {

	public final Vector position, attenuation;

	public PointLight(Vector intensity, Vector position, Vector attenuation) {
		super(intensity);
		this.position = position;
		this.attenuation = attenuation;
	}

	@Override
	public Vector direction(Vector intersection) {
		return intersection.minus(position).norm();
	}

	@Override
	public Vector intensity(Vector intersection) {
		double distance = position.distance(intersection);
		return intensity.div(attenuation.x + attenuation.y * distance + attenuation.z * distance * distance);
	}

}
