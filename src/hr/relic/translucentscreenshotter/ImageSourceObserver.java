package hr.relic.translucentscreenshotter;

import java.awt.image.BufferedImage;

/**
 * Observer for image source.
 * 
 * @author Ivan
 *
 */
public interface ImageSourceObserver {

	/**
	 * Called when new image is available in source.
	 * 
	 * @param image
	 *            new image in source
	 */
	public void newImageInSource(BufferedImage image);
}
