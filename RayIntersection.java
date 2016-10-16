
public class RayIntersection {

	public final Surface surface;
	public final Ray normal;
	public final double distance;

	public RayIntersection(Scene scene, Ray ray) {
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
		this.surface = closestSurface;
		this.normal = closestIntersection;
		this.distance = minDistance;
	}

}
