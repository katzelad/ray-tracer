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
		Color specular = params.containsKey("mtl-specular") ? new Color(params.get("mtl-specular"))
				: new Color(1, 1, 1);
		Color emission = params.containsKey("mtl-emission") ? new Color(params.get("mtl-emission"))
				: new Color(0, 0, 0);
		Color ambient = params.containsKey("mtl-ambient") ? new Color(params.get("mtl-ambient"))
				: new Color(0.1, 0.1, 0.1);
		double shininess = params.containsKey("mtl-shininess") ? Double.parseDouble(params.get("mtl-shininess")[0])
				: 100;
		switch (type) {
		case "flat":
			Color diffuse = params.containsKey("mtl-diffuse") ? new Color(params.get("mtl-diffuse"))
					: new Color(0.8, 0.8, 0.8);
			return new FlatMaterial(diffuse, specular, emission, ambient, shininess);
		}
		return null;
	}

	@Override
	public void commit() throws ParseException {
		switch (obj) {
		case "scene":
			Color bgCol = params.containsKey("background-col") ? new Color(params.get("background-col"))
					: new Color(0, 0, 0);
			scene.setBgCol(bgCol);
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
		case "light-directed":
			Vector lightDirection = new Vector(params.get("direction"));
			Color intensity = params.containsKey("color") ? new Color(params.get("color")) : new Color(1, 1, 1);
			scene.addLight(new DirectedLight(lightDirection, intensity));
			break;
		}
	}

}
