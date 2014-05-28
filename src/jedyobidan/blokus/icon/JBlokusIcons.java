package jedyobidan.blokus.icon;

import java.awt.Image;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import jedyobidan.io.JImageIO;

public class JBlokusIcons extends ArrayList<Image>{
	private static final long serialVersionUID = 350553161630452095L;
	public JBlokusIcons(){
		try {
			this.add(JImageIO.readImage(JBlokusIcons.class, "favicon-16x16.png"));
			this.add(JImageIO.readImage(JBlokusIcons.class, "favicon-32x32.png"));
			this.add(JImageIO.readImage(JBlokusIcons.class, "favicon-96x96.png"));
			this.add(JImageIO.readImage(JBlokusIcons.class, "favicon-160x160.png"));
			this.add(JImageIO.readImage(JBlokusIcons.class, "favicon-196x196.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
