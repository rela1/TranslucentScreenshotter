package hr.relic.translucentscreenshotter;

import java.awt.image.BufferedImage;

/**
 * Represents image source for observing.
 * 
 * @author irelic
 *
 */
public interface ImageSource {

	/**
	 * Adds new observer to this image source.
	 * 
	 * @param observer
	 *            image source observer
	 */
	public void addImageSourceObserver(ImageSourceObserver observer);

	/**
	 * Removes observer from this image source.
	 * 
	 * @param observer
	 *            image source observer to be removed
	 */
	public void removeImageSourceObserver(ImageSourceObserver observer);

	/**
	 * Notifies observers that are observing this image source.
	 * 
	 * @param image
	 *            new image in source
	 */
	public void notifyImageSourceObservers(BufferedImage image);
}
