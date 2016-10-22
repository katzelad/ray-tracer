import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

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

	private Vector parseVector(String name, Vector defaultVec) {
		return params.containsKey(name) ? new Vector(params.get(name)) : defaultVec;
	}

	private Vector parseVector(String name) {
		return parseVector(name, new Vector(0, 0, 0));
	}

	private double parseDouble(String name, double defaultVal) {
		return params.containsKey(name) ? Double.parseDouble(params.get(name)[0]) : defaultVal;
	}

	private double parseDouble(String name) {
		return parseDouble(name, 0);
	}

	private int parseInt(String name, int defaultVal) {
		return params.containsKey(name) ? Integer.parseInt(params.get(name)[0]) : defaultVal;
	}

	private String parseString(String name, String defaultVal) {
		return params.containsKey(name) ? params.get(name)[0] : defaultVal;
	}

	private Material parseMaterial() {
		String type = parseString("mtl-type", "flat");
		Vector specular = parseVector("mtl-specular", new Vector(1, 1, 1));
		Vector emission = parseVector("mtl-emission");
		Vector ambient = parseVector("mtl-ambient", new Vector(0.1, 0.1, 0.1));
		double shininess = parseDouble("mtl-shininess", 100);
		double reflectance = parseDouble("reflectance");
		switch (type) {
		case "flat":
			Vector diffuse = parseVector("mtl-diffuse", new Vector(0.8, 0.8, 0.8));
			return new FlatMaterial(diffuse, specular, emission, ambient, shininess, reflectance);
		case "checkers":
			double size = parseDouble("checkers-size", 0.1);
			Vector diffuse1 = parseVector("checkers-diffuse1", new Vector(1, 1, 1));
			Vector diffuse2 = parseVector("checkers-diffuse2", new Vector(0.1, 0.1, 0.1));
			return new CheckersMaterial(specular, emission, ambient, shininess, reflectance, size, diffuse1, diffuse2);
		case "texture":
			BufferedImage texture = null;
			try {
				texture = ImageIO.read(new File("tex/" + parseString("texture", null)));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return new TextureMaterial(specular, emission, ambient, shininess, reflectance, texture);
		}
		return null;
	}

	@Override
	public void commit() throws ParseException {
		switch (obj) {
		case "scene":
			scene.setBgCol(parseVector("background-col"));
			scene.setAmbientLight(parseVector("ambient-light"));
			scene.setSsWidth(parseInt("super-samp-width", 1));
			break;
		case "camera":
			double screenWidth = parseDouble("screen-width", 2);
			double screenDist = parseDouble("screen-dist");
			Vector eye = parseVector("eye");
			Vector camDirection = parseVector("direction", parseVector("look-at").minus(eye));
			Vector upDirection = parseVector("up-direction");
			scene.setCam(new Camera(eye, camDirection, screenDist, upDirection, screenWidth));
			break;
		case "rectangle":
			scene.addSurface(new Rectangle(parseMaterial(), parseVector("p0"), parseVector("p1"), parseVector("p2")));
			break;
		case "sphere":
			scene.addSurface(new Sphere(parseMaterial(), parseVector("center"), parseDouble("radius")));
			break;
		case "box":
			Vector p0 = parseVector("p0"), p1 = parseVector("p1"), p2 = parseVector("p2"), p3 = parseVector("p3");
			Material mtl = parseMaterial();
			scene.addSurface(new Rectangle(mtl, p0, p1, p2));
			scene.addSurface(new Rectangle(mtl, p0, p1, p3));
			scene.addSurface(new Rectangle(mtl, p0, p2, p3));
			Vector p4 = p1.plus(p2).minus(p0), p5 = p1.plus(p3).minus(p0), p6 = p2.plus(p3).minus(p0),
					p7 = p4.plus(p3).minus(p0);
			scene.addSurface(new Rectangle(mtl, p7, p4, p5));
			scene.addSurface(new Rectangle(mtl, p7, p4, p6));
			scene.addSurface(new Rectangle(mtl, p7, p5, p6));
			break;
		case "cylinder":
			scene.addSurface(new Cylinder(parseMaterial(), parseVector("start"), parseVector("direction"),
					parseDouble("length"), parseDouble("radius")));
			break;
		case "disc":
			scene.addSurface(
					new Disc(parseMaterial(), parseVector("center"), parseVector("normal"), parseDouble("radius")));
			break;
		case "light-directed":
			scene.addLight(new DirectedLight(parseVector("direction"), parseVector("color", new Vector(1, 1, 1))));
			break;
		case "light-point":
			scene.addLight(new PointLight(parseVector("color", new Vector(1, 1, 1)), parseVector("pos"),
					parseVector("attenuation", new Vector(1, 0, 0))));
			break;
		}
	}

}
