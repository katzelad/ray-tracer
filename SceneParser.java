import java.util.HashMap;
import java.util.Map;

public class SceneParser extends Parser {

	private Scene scene;
	private String obj;
	private Map<String, String[]> params;

	public SceneParser(Scene m_scene) {
		this.scene = m_scene;
	}

	@Override
	public boolean addObject(String name) throws ParseException {
		obj = name;
		params = new HashMap<>();
		return super.addObject(name);
	}

	@Override
	public boolean setParameter(String name, String[] args) throws ParseException {
		params.put(name, args);
		return super.setParameter(name, args);
	}

	private Material parseMaterial() {
		String type = params.containsKey("mtl-type") ? params.get("mtl-type")[0] : "flat";
		Vector specular = params.containsKey("mtl-specular") ? new Vector(params.get("mtl-specular"))
				: new Vector(1, 1, 1);
		Vector emission = params.containsKey("mtl-emission") ? new Vector(params.get("mtl-emission"))
				: new Vector(0, 0, 0);
		Vector ambient = params.containsKey("mtl-ambient") ? new Vector(params.get("mtl-ambient"))
				: new Vector(0.1, 0.1, 0.1);
		double shininess = params.containsKey("mtl-shininess") ? Double.parseDouble(params.get("mtl-shininess")[0])
				: 100;
		switch (type) {
		case "flat":
			Vector diffuse = params.containsKey("mtl-diffuse") ? new Vector(params.get("mtl-diffuse"))
					: new Vector(0.8, 0.8, 0.8);
			return new FlatMaterial(diffuse, specular, emission, ambient, shininess);
		}
		return null;
	}

	@Override
	public void commit() throws ParseException {
		switch (obj) {
		case "scene":
			Vector bgCol = params.containsKey("background-col") ? new Vector(params.get("background-col"))
					: new Vector(0, 0, 0);
			scene.setBgCol(bgCol);
			scene.setAmbientLight(params.containsKey("ambient-light") ? new Vector(params.get("ambient-light"))
					: new Vector(0, 0, 0));
			break;
		case "camera":
			double screenWidth = params.containsKey("screen-width") ? Double.parseDouble(params.get("screen-width")[0])
					: 2.0;
			double screenDist = Double.parseDouble(params.get("screen-dist")[0]);
			Vector eye = new Vector(params.get("eye"));
			Vector camDirection = params.containsKey("direction") ? new Vector(params.get("direction"))
					: new Vector(params.get("look-at")).minus(eye);
			Vector upDirection = new Vector(params.get("up-direction"));
			scene.setCam(new Camera(eye, camDirection, screenDist, upDirection, screenWidth));
			break;
		case "rectangle":
			scene.addSurface(new Rectangle(parseMaterial(), new Vector(params.get("p0")), new Vector(params.get("p1")),
					new Vector(params.get("p2"))));
			break;
		case "sphere":
			scene.addSurface(new Sphere(parseMaterial(), new Vector(params.get("center")),
					Double.parseDouble(params.get("radius")[0])));
			break;
		case "light-directed":
			Vector lightDirection = new Vector(params.get("direction"));
			Vector intensity = params.containsKey("color") ? new Vector(params.get("color")) : new Vector(1, 1, 1);
			scene.addLight(new DirectedLight(lightDirection, intensity));
			break;
		}
	}

}
