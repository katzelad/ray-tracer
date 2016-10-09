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
			scene.addSurface(new Rectangle(new Vector(params.get("p0")), new Vector(params.get("p1")),
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
