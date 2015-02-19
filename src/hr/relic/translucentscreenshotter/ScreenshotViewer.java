package hr.relic.translucentscreenshotter;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

/**
 * Represents screenshot viewer in which user can do several operations with
 * screenshot.
 * 
 * @author Ivan
 *
 */
public class ScreenshotViewer extends JFrame {

	private static final long serialVersionUID = 1L;

	private JMenuBar menuBar;
	private BufferedImage image;
	private JLabel imageLabel;
	private JFrame parent;
	private static final String FILE_MENU_STRING = "File";
	private static final String SAVE_MENU_ITEM_STRING = "Save";
	private Action saveAction;

	/**
	 * Creates graphics user interface and program actions.
	 * 
	 * @param parent
	 *            parent frame which has to be locked when this frame is shown;
	 *            if parent is null, nothing is locked
	 * @param image
	 *            image that is shown in viewer
	 */
	public ScreenshotViewer(JFrame parent, BufferedImage image) {
		super();
		this.parent = parent;
		this.image = image;
		lockParent();
		createActions();
		createGUI();
	}

	/**
	 * Locks parent frame (if parent exists).
	 */
	private void lockParent() {
		if (parent != null) {
			parent.setEnabled(false);
		}
	}

	@Override
	public void dispose() {
		if (parent != null) {
			parent.setEnabled(true);
		}
		super.dispose();
	}

	/**
	 * Creates actions.
	 */
	private void createActions() {
		saveAction = new AbstractAction(SAVE_MENU_ITEM_STRING) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				int response = jfc.showSaveDialog(ScreenshotViewer.this);
				if (response == JFileChooser.APPROVE_OPTION) {
					File saveFile = jfc.getSelectedFile();
					if (saveFile != null) {
						try {
							ImageIO.write(image, "png", saveFile);
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(
									ScreenshotViewer.this,
									"Error while saving image!", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		};
	}

	/**
	 * Initializes and creates graphics user interface.
	 */
	private void createGUI() {
		menuBar = new JMenuBar();
		fillMenuBar();
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		imageLabel = new JLabel(new ImageIcon(image));
		container.add(new JScrollPane(imageLabel), BorderLayout.CENTER);
		container.add(menuBar, BorderLayout.NORTH);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * Creates menu bar and adds menu actions.
	 */
	private void fillMenuBar() {
		JMenu fileMenu = new JMenu(FILE_MENU_STRING);
		JMenuItem saveMenuItem = new JMenuItem(SAVE_MENU_ITEM_STRING);
		saveMenuItem.setAction(saveAction);
		fileMenu.add(saveMenuItem);
		menuBar.add(fileMenu);
	}

}
