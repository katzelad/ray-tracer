import java.awt.Color;
import java.awt.image.BufferedImage;

import javafx.geometry.Point2D;

public class TextureMaterial extends Material {

	private final BufferedImage texture;
	private final int width, height;

	public TextureMaterial(Vector specular, Vector emission, Vector ambient, double shininess, double reflectance,
			BufferedImage texture) {
		super(specular, emission, ambient, shininess, reflectance);
		this.texture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
	}

	@Override
	public Vector diffuse(Point2D point) {
		Color pixel = new Color(texture.getRGB((int) (point.getX() * width), (int) (point.getY() * height)));
		return new Vector(pixel.getRed(), pixel.getGreen(), pixel.getBlue()).div(255);
	}

}
