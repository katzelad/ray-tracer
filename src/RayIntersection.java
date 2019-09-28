
public class RayIntersection {

	public final Surface surface;
	public final Ray normal;
	public final double distance;

	private RayIntersection(Surface surface, Ray normal, double distance) {
		this.surface = surface;
		this.normal = normal;
		this.distance = distance;
	}

	public static RayIntersection test(Scene scene, Ray ray) {
		Surface closestSurface = null;
		Ray closestIntersection = null;
		double minDistance = Double.POSITIVE_INFINITY;
		for (Surface surface : scene.getSurfaces()) {
			Ray intersection = surface.intersect(ray);
			if (intersection != null) {
				double distance = ray.origin.distance(intersection.origin);
				if (distance < minDistance) {
					minDistance = distance;
					closestSurface = surface;
					closestIntersection = intersection;
				}
			}
		}
		return closestSurface == null ? null
				: new RayIntersection(closestSurface,
						new Ray(closestIntersection.origin.plus(closestIntersection.direction.mul(Surface.EPSILON)),
								closestIntersection.direction),
						minDistance);
	}

}
