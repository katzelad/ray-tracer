
public abstract class Surface {
	
	public static final double EPSILON = 0.0001;

	public final Material mtl;

	public Surface(Material mtl) {
		this.mtl = mtl;
	}

	public abstract Ray intersect(Ray ray);

	public abstract Point flatten(Vector point);

}
