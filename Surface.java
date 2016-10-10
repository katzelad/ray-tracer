
public abstract class Surface {
	
	public final Material mtl;
	
	public Surface(Material mtl) {
		this.mtl = mtl;
	}

	public abstract Ray intersect(Ray ray);

}
