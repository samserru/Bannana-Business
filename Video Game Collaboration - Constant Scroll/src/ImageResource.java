import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageResource {

	private ImageIcon image;			// The ImageIcon will be used to hold the Character's png.
	private ImageIcon health;	
	private int imageCount;
	private int jumpCount;
	private int imageMax;
	private int imageOffset;
	public int counter;
	private int slideCount;
	public static int lives;
	public int healthCount;
	private Item dog;

	// These two variables are used so that the image doesn't refresh every time the the panel is redrawn.
	// Without these variables the images would change much too quickly.
	private int imageRefreshCounter = 0;
	private static final int IMAGE_REFRESH_MAX = 3;

	private static final int SCALE = 4;

	private ImageIcon[] runningImages;
	private ImageIcon[] jumpingImages;
	private ImageIcon[] slidingImages;
	public ImageIcon[] heartImages;


	public ImageResource(String imagePath, int imageMax, int imageOffset) {
		runningImages = new ImageIcon[imageMax];
		jumpingImages = new ImageIcon[imageMax];
		slidingImages = new ImageIcon[imageMax];
		heartImages = new ImageIcon[2];
		
		
		imageCount = 0;
		healthCount = 0;
		jumpCount = 0;
		counter = 0;

		loadImages((imagePath + "MonkeyJog ("), runningImages);
		loadImages((imagePath + "jump ("), jumpingImages);
		loadImages((imagePath + "slide ("), slidingImages);
		loadHealth((imagePath + "heart ("), heartImages);
		
		image = runningImages[imageCount];
		health = heartImages[healthCount];
		
		this.imageMax = imageMax;
		this.imageOffset = imageOffset;
	}

	public void loadImages(String imagePath, ImageIcon[] images) {

		ClassLoader cldr = this.getClass().getClassLoader();
		String newImagePath; 

		for(int i = 0; i < images.length; i++) {
			newImagePath = imagePath + (i + 1) + ").png";

			URL imageURL = cldr.getResource(newImagePath);				
			image = new ImageIcon(imageURL);	
			image.getImage();
			Image scaled = image.getImage().getScaledInstance(image.getIconWidth() / SCALE, 
					image.getIconHeight() / SCALE, image.getImage().SCALE_SMOOTH);
			images[i] = new ImageIcon(scaled);
		}
	}
	
	public void loadHealth(String imagePath, ImageIcon[] images) {
		
		ClassLoader cldr = this.getClass().getClassLoader();
		String newImagePath; 

		for(int i = 0; i < images.length; i++) {
			newImagePath = imagePath + (i + 1) + ").png";

			URL imageURL = cldr.getResource(newImagePath);				
			health = new ImageIcon(imageURL);	
			health.getImage();
			Image scaled = health.getImage().getScaledInstance(health.getIconWidth() *3, 
					health.getIconHeight()*3, health.getImage().SCALE_SMOOTH);
			heartImages[i] = new ImageIcon(scaled);
		}
	}

	public void updateImage(int x_direction, boolean jumping, boolean isDead, boolean sliding) {
		counter++;
		if(counter>9) {
			imageRefreshCounter++;

			if(imageRefreshCounter >= IMAGE_REFRESH_MAX && imageCount < imageMax - 1) {
				imageCount++;
				imageRefreshCounter = 0;
			}	
			else if(imageCount >= imageMax - 1 && !isDead) {
				imageCount = 0;
			}

			if(isDead) {
				image = runningImages[0];
			}
			else if(jumping) {
				jumpCount = (jumpCount < (imageMax * 6)-1) ? jumpCount+1 : 0;
				image = jumpingImages[jumpCount/6];
			}
			else if(sliding) {
				slideCount = (slideCount < (imageMax * 6)-1) ? slideCount+1 : 0;
				image = slidingImages[slideCount/6];
			}
			
			// running or walking
			else {
				image = runningImages[imageCount];
			}
			
			counter = 0;
		}
	}

	public ImageIcon getImage() {
		return image;
	}
	
	public ImageIcon getHeart() {
		return health;
	}
	
	public ImageIcon[] getHealth() {
		return heartImages;
	}

	public int getImageOffset() {
		return imageOffset;
	}

}
