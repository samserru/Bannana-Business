// Class: GraphicsPanel
// Written by: Mr. Swope
// Date: 1/27/2020
// Description: This class is the main class for this project.  It extends the Jpanel class and will be drawn on
// on the JPanel in the GraphicsMain class.  
//
// Since you will modify this class you should add comments that describe when and how you modified the class.  

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Rectangle;

public class GraphicsPanel extends JPanel implements KeyListener{
	private Timer timer; // The timer is used to move objects at a consistent time interval.
	private Boolean mover;
	private Background background1; // The background object will display a picture in the background.
	private Background background2; // There has to be two background objects for scrolling.
	private ArrayList<Item> cans;
	private ArrayList<Item> fireHydrants;
	private Sprite sprite; // create a Sprite object
	private Item item;
	private int gameState;
	private int counter;
	private int score;
	private double scoreMultiplier;
	private int levelUp;
	private boolean dead;

	// This declares an Item object. You can make a Item display
	// pretty much any image that you would like by passing it
	// the path for the image.


	public GraphicsPanel() throws IOException{
		LeaderBoard leaderBoard1 = new LeaderBoard();

		gameState = 0;
		score = 1;
		background1 = new Background(); // You can set the background variable equal to an instance of any of  
		background2 = new Background(-background1.getImage().getIconWidth());
		dead = false;
		levelUp=600;
		item = new Item(500, 200, "images/objects/box.png", 4);  
		// The Item constructor has 4 parameters - the x coordinate, y coordinate
		// the path for the image, and the scale. The scale is used to make the
		// image smaller, so the bigger the scale, the smaller the image will be.
		counter = 0;
		cans = new ArrayList<Item>();
		fireHydrants = new ArrayList<Item>();
		sprite = new Sprite(background1.getWidth()/2-190, (int)(Math.random()*100)+100);
		// The Sprite constuctor has two parameter - - the x coordinate and y coordinate

		setPreferredSize(new Dimension(background1.getImage().getIconWidth(),
				background2.getImage().getIconHeight()));  
		// This line of code sets the dimension of the panel equal to the dimensions
		// of the background image.

		timer = new Timer(5, new ClockListener(this));   // This object will call the ClockListener's
		// action performed method every 5 milliseconds once the
		// timer is started. You can change how frequently this
		// method is called by changing the first parameter.

		//LEADERBOARD
		LeaderBoard.addScore("Bennett", "2");
		ArrayList<String> Top10 = LeaderBoard.getTop10();
		for(String s:Top10) {
			System.out.println(s);
		}


		//Owen Blake Luke Sam Abella Owen Blake Luke Sam Abella Owen Blake Luke Sam Abella Owen Blake Luke Sam Abella  Owen Blake Luke Sam Abella Owen Blake Luke Sam Abella  Owen Blake Luke Sam Abella Owen Blake Luke Sam Abella Bag Tag Rag  24 9 9 9 9 8 8 7 7 7 Owen Blake Owen Owen Luke Abella Owen Blake Luke Sam Abella  Owen Blake Luke Sam Abella Owen Blake Sam Abella  Owen Blake Luke Sam Abella Owen Blake Luke Sam Abella Bag Tag Rag 24 9 9 9 9 8 8 7 7 7 6 6 6 6 6 6 6 6 5 5 Abella Blake Luke Sam Abella Blake Luke Sam Blake Sam Abella  Owen Blake Luke Sam Abella Owen Blake Luke Sam Abella Bag Tag Rag

		timer.start();
		this.setFocusable(true);     // for keylistener
		this.addKeyListener(this);
	}

	// method: paintComponent
	// description: This method will paint the items onto the graphics panel.  This method is called when the panel is
	//   first rendered.  It can also be called by this.repaint(). You'll want to draw each of your objects.
	// This is the only place that you can draw objects.
	// parameters: Graphics g - This object is used to draw your images onto the graphics panel.
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

		if(gameState ==0) {
			g2.setColor(Color.black);
			g2.fillRect(-20, 0, 2300, 500);
			g2.setFont(new Font("Arial", Font.BOLD, 50));
			g2.setColor(Color.blue);

			g2.drawString("Banana Buisness", 200, 150);
			g2.drawString("Press Space to Begin", 400, 250);
		}

		else if(gameState ==1) {
			background1.draw(this, g);
			background2.draw(this, g);


			sprite.draw(g2, this);
			for (int i =0; i<cans.size();i++) {

				cans.get(i).draw(g2, this);
			}
			for (int i =0; i<fireHydrants.size();i++) {
				fireHydrants.get(i).draw(g2, this);
			}
			g2.setColor(Color.RED);
			Rectangle r = sprite.getBounds();
			g2.draw(r);
		}




	}

	// method:clock
	// description: This method is called by the clocklistener every 5 milliseconds.  You should update the coordinates
	// of one of your characters in this method so that it moves as time changes.  After you update the
	// coordinates you should repaint the panel.
	public void clock(){
		// You can move any of your objects by calling their move methods.
		sprite.move(this);

		if(dead!=true) {
			background1.move();
			background2.move();

			counter++;

			//create trash cans
			if (counter%levelUp==0) {
				cans.add(new Item(background1.getWidth(), (int)(Math.random()*120) + 120, "images/objects/Barrel2.png", 4));
			}
			if(counter%2000==0) {
				levelUp-=80;
			}
			//create fire hydrants
			if (counter%1750==0) {
				fireHydrants.add(new Item(background1.getWidth(), 220, "images/objects/Barrel1.png", 4));
			}

			for (int i = 0; i <cans.size();i++) {

				cans.get(i).move(this);
				if(cans.get(i).x_coordinate<=-60) {
					cans.remove(i);
				}
			}
			for (int i = 0; i <fireHydrants.size();i++) {

				fireHydrants.get(i).move(this);
				if(fireHydrants.get(i).x_coordinate<=-60) {
					fireHydrants.remove(i);
				}
			}
			// You can also check to see if two objects intersect like this. In this case if the sprite collides with the
			// item, the item will get smaller.
			for(Item s: cans) {
				if(sprite.collision(s)) {
					System.out.println("stop");
					sprite.die();
					//dead = true;
				}
			}
			sprite.x_direction = 2;
			if(sprite.gravityActive){
				if(sprite.y_coordinate<=0) {
					sprite.y_coordinate=1;
					sprite.gravity=0;
				}
				sprite.y_coordinate-=sprite.gravity;
				sprite.gravity-=sprite.gravityMultiplier;

				if(sprite.y_coordinate>background1.getHeight()-200) {
					sprite.y_coordinate=background1.getHeight()-200;
					sprite.gravity=3;
					sprite.gravityActive = false;
				}

			}
			else if(sprite.upPressed&&sprite.y_coordinate>0)
				sprite.y_coordinate-=2;
			else if(sprite.downPressed&&sprite.y_coordinate<200)
				sprite.y_coordinate+=2;
			this.repaint();
		}
	}

	// method: keyPressed()
	// description: This method is called when a key is pressed. You can determine which key is pressed using the
	// KeyEvent object.  For example if(e.getKeyCode() == KeyEvent.VK_LEFT) would test to see if
	// the left key was pressed.
	// parameters: KeyEvent e
	@Override
	public void keyPressed(KeyEvent e) {



		if(e.getKeyCode() == KeyEvent.VK_UP) {
			sprite.upPressed = true;

		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN && !(sprite.collision(item) && sprite.getY() < item.getY()))
			sprite.downPressed = true;
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			gameState =1;
			sprite.gravityActive = true;
			sprite.gravityMultiplier = 0.04;
		}
		else if(e.getKeyCode() == KeyEvent.VK_J)
			sprite.jump();
		else if(e.getKeyCode() == KeyEvent.VK_D) {
			playSound("src/sounds/bump.WAV");
			//sprite.die();
		}
	}

	// This function will play the sound "fileName".
	public static void playSound(String fileName) {
		try {
			File url = new File(fileName);
			Clip clip = AudioSystem.getClip();

			AudioInputStream ais = AudioSystem.getAudioInputStream(url);
			clip.open(ais);
			clip.start();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// method: keyTyped()
	// description: This method is called when a key is pressed and released. It basically combines the keyPressed and
	//              keyReleased functions.  You can determine which key is typed using the KeyEvent object.  
	// For example if(e.getKeyCode() == KeyEvent.VK_LEFT) would test to see if the left key was typed.
	// You probably don't want to do much in this method, but instead want to implement the keyPresses and keyReleased methods.
	// parameters: KeyEvent e
	@Override
	public void keyTyped(KeyEvent e) {


	}

	// method: keyReleased()
	// description: This method is called when a key is released. You can determine which key is released using the
	// KeyEvent object.  For example if(e.getKeyCode() == KeyEvent.VK_LEFT) would test to see if
	// the left key was pressed.
	// parameters: KeyEvent e
	@Override
	public void keyReleased(KeyEvent e) {

		if(e.getKeyCode() ==  KeyEvent.VK_UP)
			sprite.upPressed = false;
		if(e.getKeyCode() ==  KeyEvent.VK_DOWN)
			sprite.downPressed = false;
		else if(e.getKeyCode() ==  KeyEvent.VK_SPACE)
			sprite.gravityMultiplier = 0.1;

	}

}