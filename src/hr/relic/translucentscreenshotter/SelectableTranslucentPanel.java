package hr.relic.translucentscreenshotter;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * Represents swing component which represents translucent panel and provides
 * actions that provide selection of part of screen. When part of screen is
 * selected, observers are notified with selected part of screen as new image in
 * source.
 * 
 * @author Ivan
 *
 */
public class SelectableTranslucentPanel extends JComponent implements
		ImageSource {

	private static final Color RECTANGLE_SELECTION_COLOR = new Color(0, 0, 0, 0);
	private static final Color BACKGROUND_SCREEN_COLOR = new Color(0.5f, 0.5f,
			0.5f, 0.4f);
	private static final long serialVersionUID = 1L;
	private List<ImageSourceObserver> observers;
	private Point firstPoint;
	private Point secondPoint;
	private Robot screenshotterRobot;
	private Rectangle screenRectangle;
	private int[] selectionPoints;

	/**
	 * Creates selectable image component.
	 * 
	 * @param image
	 *            image to be drawn and selectable
	 * @throws AWTException
	 *             in case that screnshotting robot cannot be instantiated
	 */
	public SelectableTranslucentPanel() throws AWTException {
		super();
		this.observers = new ArrayList<>();
		this.selectionPoints = new int[6];
		this.screenshotterRobot = new Robot();
		this.screenRectangle = new Rectangle();
		createSelectionListeners();

	}

	/**
	 * Creates selection mouse listeners.
	 */
	private void createSelectionListeners() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				SelectableTranslucentPanel.this.setCursor(Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				SelectableTranslucentPanel.this.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				firstPoint = e.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (firstPoint != null) {
					secondPoint = e.getPoint();
					calculatePoints();
					if (selectionPoints[4] == 0 || selectionPoints[5] == 0) {
						firstPoint = null;
						secondPoint = null;
						repaint();
						return;
					}
					screenRectangle.x = selectionPoints[0];
					screenRectangle.y = selectionPoints[2];
					screenRectangle.width = selectionPoints[4];
					screenRectangle.height = selectionPoints[5];
					BufferedImage selectedPart = screenshotterRobot
							.createScreenCapture(screenRectangle);
					notifyImageSourceObservers(selectedPart);
					firstPoint = null;
					secondPoint = null;
					repaint();
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (firstPoint != null) {
					secondPoint = e.getPoint();
					repaint();
				}
			}
		});
	}

	/**
	 * Calculates x and y coordinates, width and height for image selection from
	 * rectangle points.
	 */
	private void calculatePoints() {
		if (firstPoint.x < secondPoint.x) {
			selectionPoints[0] = firstPoint.x;
			selectionPoints[1] = secondPoint.x;
		} else {
			selectionPoints[0] = secondPoint.x;
			selectionPoints[1] = firstPoint.x;
		}
		if (firstPoint.y < secondPoint.y) {
			selectionPoints[2] = firstPoint.y;
			selectionPoints[3] = secondPoint.y;
		} else {
			selectionPoints[2] = secondPoint.y;
			selectionPoints[3] = firstPoint.y;
		}
		selectionPoints[4] = selectionPoints[1] - selectionPoints[0];
		selectionPoints[5] = selectionPoints[3] - selectionPoints[2];
	}

	@Override
	public void addImageSourceObserver(ImageSourceObserver observer) {
		observers = new ArrayList<>(observers);
		observers.add(observer);
	}

	@Override
	public void removeImageSourceObserver(ImageSourceObserver observer) {
		observers = new ArrayList<>(observers);
		observers.remove(observer);
	}

	@Override
	public void notifyImageSourceObservers(BufferedImage image) {
		for (ImageSourceObserver observer : observers) {
			observer.newImageInSource(image);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		Dimension componentSize = this.getSize();
		if (firstPoint != null) {
			g.setColor(RECTANGLE_SELECTION_COLOR);
			int startingX = Math.min(firstPoint.x, secondPoint.x);
			int startingY = Math.min(firstPoint.y, secondPoint.y);
			int width = Math.abs(firstPoint.x - secondPoint.x);
			int height = Math.abs(firstPoint.y - secondPoint.y);
			g.fillRect(startingX, startingY, width, height);
			g.setColor(Color.BLACK);
			g.drawRect(startingX - 1, startingY - 1, width + 1, height + 1);
			fillAreasAroundSelection(g, startingX, startingY, width, height,
					componentSize);
		} else {
			g.setColor(BACKGROUND_SCREEN_COLOR);
			g.fillRect(0, 0, componentSize.width, componentSize.height);
		}
	}

	/**
	 * Fills other areas of screen around selection rectangle.
	 * 
	 * @param g
	 *            graphics used for drawing
	 * @param startingX
	 *            starting x point of selection rectangle
	 * @param startingY
	 *            starting y point of selection rectangle
	 * @param width
	 *            width of selection rectangle
	 * @param height
	 *            height of selection rectangle
	 * @param componentSize
	 *            size of component
	 */
	private void fillAreasAroundSelection(Graphics g, int startingX,
			int startingY, int width, int height, Dimension componentSize) {
		g.setColor(BACKGROUND_SCREEN_COLOR);
		int upperAreaHeight = startingY;
		if (upperAreaHeight > 0) {
			g.fillRect(0, 0, componentSize.width, upperAreaHeight);
		}
		int leftAreaWidth = startingX;
		if (startingX > 0) {
			g.fillRect(0, startingY, leftAreaWidth, height);
		}
		int heigthOffset = upperAreaHeight + height;
		int downAreaHeight = componentSize.height - heigthOffset;
		if (downAreaHeight > 0) {
			g.fillRect(0, heigthOffset, componentSize.width, downAreaHeight);
		}
		int widthOffset = leftAreaWidth + width;
		int rightAreaWidth = componentSize.width - widthOffset;
		if (rightAreaWidth > 0) {
			g.fillRect(widthOffset, startingY, rightAreaWidth, height);
		}
	}
}
