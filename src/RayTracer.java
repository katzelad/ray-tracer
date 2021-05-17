import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class RayTracer {

	/**
	 * @param args
	 */
	static Display display;

	public static void main(String[] args) {
		display = new Display();
		RayTracer tracer = new RayTracer();
		tracer.runMain(display);

		display.dispose();
	}

	void renderTo(ImageData dat, Canvas canvas) throws Parser.ParseException {
		// TO-ADD: initialize your scene object
		Scene m_scene = new Scene();
		m_scene.setCanvasSize(dat.height, dat.width);
		// probably need to set its size before parsing.

		try {
			// TO-ADD: instantiate your Parser inherited class and invoke it.
			SceneParser f = new SceneParser(m_scene);
			f.parse(new StringReader(m_sceneText.getText()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// TO-ADD: possibly add post-parse scene initializations

		GC gc = new GC(canvas);
		gc.fillRectangle(m_rect);
		ImageData scanlined = new ImageData(m_rect.width, 1, 24, new PaletteData(0xFF0000, 0xFF00, 0xFF));

		for (int y = 0; y < dat.height; ++y) {
			for (int x = 0; x < dat.width; ++x) {
				// TO-ADD: get the color for this pixel (shoot rays etc')
				Vector mycol = m_scene.getColor(x, y);

				// TO-ADD: set the image color for this pixel.
				int r = mycol.getRed();
				int g = mycol.getGreen();
				int b = mycol.getBlue();

				// clamp values to [0,255]
				r &= 0xFF;
				g &= 0xFF;
				b &= 0xFF;
				int rgbcol = (r << 16) | (g << 8) | b;

				dat.setPixel(x, y, rgbcol);
				scanlined.setPixel(x, 0, rgbcol);
			}
			Image img = new Image(display, scanlined);
			gc.drawImage(img, 0, y);
			img.dispose();

		}
		Image img = new Image(display, dat);
		gc.drawImage(img, 0, 0);
		img.dispose();
	}

	public static String readTextFile(Reader in) throws IOException {
		StringBuilder sb = new StringBuilder(1024);
		BufferedReader reader = new BufferedReader(in);

		char[] chars = new char[1024];
		int numRead;
		while ((numRead = reader.read(chars)) > -1) {
			sb.append(String.valueOf(chars, 0, numRead));
		}
		return sb.toString();
	}

	void openFile(String filename) {
		try {

			Reader fr = new FileReader(filename);
			m_sceneText.setText(readTextFile(fr));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	org.eclipse.swt.graphics.Rectangle m_rect;
	ImageData m_imgdat;

	// GUI
	Text m_sceneText;

	void runMain(final Display display) {
		Shell editShell = new Shell(display);
		editShell.setText("Input");
		editShell.setSize(300, 550);
		GridLayout gridEdit = new GridLayout();
		editShell.setLayout(gridEdit);

		Composite editComp = new Composite(editShell, SWT.NONE);
		GridData ld = new GridData();
		ld.heightHint = 30;
		editComp.setLayoutData(ld);

		m_sceneText = new Text(editShell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		ld = new GridData(GridData.FILL_BOTH);
		m_sceneText.setLayoutData(ld);
		Font fixed = new Font(display, "Courier New", 10, 0);
		m_sceneText.setFont(fixed);

		final Shell shell = new Shell(display);
		shell.setText("Ray Tracer Ex");
		shell.setSize(600, 500);
		GridLayout gridLayout = new GridLayout();

		shell.setLayout(gridLayout);

		// the canvas we'll be drawing on.
		final Canvas canvas = new Canvas(shell, SWT.BORDER | SWT.NO_REDRAW_RESIZE);
		ld = new GridData(GridData.FILL_BOTH);
		canvas.setLayoutData(ld);

		Composite comp = new Composite(shell, SWT.NONE);
		ld = new GridData();
		ld.heightHint = 45;
		comp.setLayoutData(ld);

		// "Render Button"
		Button renderBot = new Button(comp, SWT.PUSH);
		renderBot.setText("Render");
		renderBot.setSize(150, 40);

		renderBot.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent ev) {
				try {
					m_imgdat = new ImageData(m_rect.width, m_rect.height, 24, new PaletteData(0xFF0000, 0xFF00, 0xFF));
					renderTo(m_imgdat, canvas);
				} catch (Parser.ParseException e) {
					System.out.println("Error Parsing text: " + e.getMessage());
				}
			}
		});

		Button savePngBot = new Button(comp, SWT.PUSH);
		savePngBot.setText("Save PNG");
		savePngBot.setBounds(250, 0, 70, 40);
		savePngBot.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent ev) {
				FileDialog dlg = new FileDialog(shell, SWT.SAVE);
				dlg.setText("Save PNG");
				dlg.setFilterExtensions(new String[] { "*.png", "*.*" });
				String selected = dlg.open();
				if (selected == null)
					return;

				ImageLoader loader = new ImageLoader();
				loader.data = new ImageData[] { m_imgdat };
				loader.save(selected, SWT.IMAGE_PNG);
			}
		});

		Button openBot = new Button(editComp, SWT.PUSH);
		openBot.setText("Open");
		openBot.setBounds(0, 0, 100, 30);

		openBot.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(shell, SWT.OPEN);
				dlg.setText("Open Model");
				dlg.setFilterExtensions(new String[] { "*.txt", "*.*" });
				String selected = dlg.open();
				if (selected != null)
					openFile(selected);

			}
		});

		canvas.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				m_rect = canvas.getClientArea();
			}
		});

		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				if (m_imgdat == null) {
					gc.drawLine(0, 0, e.width, e.height);
					return;
				}
				Image img = new Image(display, m_imgdat);
				if (img != null) {
					gc.drawImage(img, 0, 0);
				}
				img.dispose();
			}
		});

		shell.open();
		org.eclipse.swt.graphics.Point l = shell.getLocation();
		editShell.setLocation(new org.eclipse.swt.graphics.Point(l.x + 650, l.y));
		editShell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	}

}
