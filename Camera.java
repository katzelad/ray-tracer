
public class Camera {

	public final Vector eye, direction, upDirection, leftDirection, screenCenter;
	public final Double screenDist, screenWidth;

	public Camera(Vector eye, Vector direction, Double screenDist, Vector upDirection, Double screenWidth) {
		this.eye = eye;
		this.direction = direction.norm();
		this.screenDist = screenDist;
		upDirection = upDirection.norm();
		this.screenWidth = screenWidth;
		this.leftDirection = upDirection.cross(this.direction);
		this.upDirection = this.direction.cross(this.leftDirection);
		this.screenCenter = this.eye.plus(this.direction.mul(this.screenDist));
	}

}
