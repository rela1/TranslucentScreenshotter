package hr.relic.translucentscreenshotter;

import java.awt.*;

import javax.swing.*;

/**
 * Translucent frame for adding components.
 * 
 * @author Ivan
 * 
 */
public class TranslucentFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates translucent frame with no title.
	 */
	public TranslucentFrame() {
		this("");
	}

	/**
	 * Creates translucent frame with title.
	 * 
	 * @param title
	 *            frame title
	 */
	public TranslucentFrame(String title) {
		super(title);
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
	}
}