package hr.relic.translucentscreenshotter;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Represents translucent screenshotter graphics interface.
 * 
 * @author irelic
 *
 */
public class TranslucentScreenshotterMainWindow extends TranslucentFrame {

	private static final long serialVersionUID = 1L;
	private SelectableTranslucentPanel selectableTranslucentPanel;
	private Action exitAction;

	/**
	 * Creates graphics user interface and program actions.
	 * 
	 * @throws AWTException
	 *             in case of error initializing selectable translucent panel
	 */
	public TranslucentScreenshotterMainWindow() throws AWTException {
		super();
		createActions();
		createGUI();
		createListenersAndObservers();
	}

	/**
	 * Creates listeners and observers.
	 */
	private void createListenersAndObservers() {
		InputMap inputMap = selectableTranslucentPanel
				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = selectableTranslucentPanel.getActionMap();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				InputEvent.CTRL_DOWN_MASK), "exit");
		actionMap.put("exit", exitAction);
		selectableTranslucentPanel
				.addImageSourceObserver(new ImageSourceObserver() {
					@Override
					public void newImageInSource(BufferedImage image) {
						JFrame viewer = new ScreenshotViewer(
								TranslucentScreenshotterMainWindow.this, image);
						viewer.setVisible(true);
					}
				});
	}

	/**
	 * Creates actions.
	 */
	private void createActions() {
		exitAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				TranslucentScreenshotterMainWindow.this.dispose();
			}
		};
	}

	/**
	 * Initializes and creates graphics user interface.
	 * 
	 * @throws AWTException
	 *             in case of error initializing selectable translucent panel
	 */
	private void createGUI() throws AWTException {
		selectableTranslucentPanel = new SelectableTranslucentPanel();
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(selectableTranslucentPanel, BorderLayout.CENTER);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setLocationRelativeTo(null);
	}

	/**
	 * Creates and shows main application window.
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new TranslucentScreenshotterMainWindow().setVisible(true);
				} catch (AWTException e) {
					JOptionPane
							.showMessageDialog(
									null,
									"Error while initializating application, will terminate now!",
									"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
