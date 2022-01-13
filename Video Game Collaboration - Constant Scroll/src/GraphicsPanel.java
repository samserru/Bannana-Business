// Class: GraphicsPanel
// Written by: Mr. Swope
// Date: 1/27/2020
// Description: This class is the main class for this project.  It extends the Jpanel class and will be drawn on
// on the JPanel in the GraphicsMain class.  
//
// Since you will modify this class you should add comments that describe when and how you modified the class.  

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Rectangle;

public class GraphicsPanel extends JPanel implements KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Timer timer; // The timer is used to move objects at a consistent time interval.
	private Background background1; // The background object will display a picture in the background.
	private Background background2; // There has to be two background objects for scrolling.
	private ArrayList<Item> cans;
	private ArrayList<Item> fireHydrants;
	private Sprite sprite; // create a Sprite object
	private int gameState;
	private int counter;
	private ArrayList<Item> banana;
	private int score;
	private boolean isHighScore;
	private int levelUp;
	private int commandNum;
	private boolean indestructable;
	private int counterIndestructable;
	private int lives;
	private String name;
	public LeaderBoard leaderBoard;
	public ImageResource imageResource;
	public ImageIcon[] heartImages;
	private int item;


	// This declares an Item object. You can make a Item display
	// pretty much any image that you would like by passing it
	// the path for the image.


	public GraphicsPanel() throws IOException{

		//allows for us to display the hearts for lives
		imageResource = new ImageResource("images/robot/", 8, 80);
		heartImages = imageResource.getHealth();

		//instatiate bananas
		banana= new ArrayList<>();

		//helps to place new highscores with textfiles on the leaderboard
		isHighScore=false;
		name=null;
		leaderBoard = new LeaderBoard();

		//changes what is displayed on the screen
		gameState = 0;
		commandNum=0;

		//initial score that is displayed on the screen when playing
		score = 1;
		background1 = new Background(); // You can set the background variable equal to an instance of any of  
		background2 = new Background(-background1.getImage().getIconWidth());

		//gives break time to player so one object doesn't automatically kill them
		indestructable = false;
		counterIndestructable=500;

		//allows for the game to get progressively harder
		levelUp=600;

		//starting lives amount
		lives=3;

		// The Item constructor has 4 parameters - the x coordinate, y coordinate
		// the path for the image, and the scale. The scale is used to make the
		// image smaller, so the bigger the scale, the smaller the image will be.
		counter = 0;
		cans = new ArrayList<Item>();
		fireHydrants = new ArrayList<Item>();
		sprite = new Sprite(background1.getWidth()/2-190, background1.getHeight()-200);
		// The Sprite constuctor has two parameter - - the x coordinate and y coordinate

		setPreferredSize(new Dimension(background1.getImage().getIconWidth(),
				background2.getImage().getIconHeight()));  
		// This line of code sets the dimension of the panel equal to the dimensions
		// of the background image.

		timer = new Timer(5, new ClockListener(this));   // This object will call the ClockListener's
		// action performed method every 5 milliseconds once the
		// timer is started. You can change how frequently this
		// method is called by changing the first parameter.


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

		//TITLE SCREEN- what is displayed in the beginning
		if(gameState == 0) {

			g2.setColor(new Color(70,120,90));
			g2.fillRect(-20, 0, 2300, 500);
			g2.setFont(new Font("Arial", Font.BOLD, 30));
			if(isHighScore) {
				g2.setColor(Color.pink);
				g2.drawString("New High Score!", 400, 30);

			}
			g2.setFont(new Font("Arial", Font.BOLD, 60));
			g2.setColor(Color.black);

			g2.drawString("Banana Business", 250, 100);


			g2.setFont(new Font("Arial", Font.BOLD, 30));
			g2.drawString("START", 200, 200);
			if(commandNum==0) {
				g2.drawString(">", 180, 200);
				g2.drawString("<", 305, 200);
			}
			g2.drawString("LEADERBOARD", 200, 250);
			if(commandNum==1) {
				g2.drawString(">", 180, 250);
				g2.drawString("<", 440, 250);
			}
			g2.drawString("QUIT", 200, 300);
			if(commandNum==2) {
				g2.drawString(">", 180, 300);
				g2.drawString("<", 275, 300);
			}
		}

		//GAME- where the display of the actual gameplay occurs
		else if(gameState ==1) {

			//draws backgrounds
			background1.draw(this, g);
			background2.draw(this, g);

			//draws sprite
			sprite.draw(g2, this);

			//draws trash cans
			for (Item b: cans) {

				b.draw(g2, this);
			}

			//draws fireHydrants
			for (Item z: fireHydrants) {
				z.draw(g2, this);
			}
			for (Item z: banana) {
				z.draw(g2, this);
			}

			//hitbox for the robot
			g2.setColor(Color.RED);
			Rectangle r = sprite.getBounds();
			g2.draw(r);

			g2.setColor(Color.ORANGE);
			g2.setFont(new Font("Segoe Script", Font.BOLD + Font.ITALIC, 30));
			g2.drawString(String.valueOf((int)score), 750, 30);

			g2.setColor(Color.red);
			g2.setFont(new Font("Segoe Script", Font.BOLD, 30));

			int x = 20;
			int y = 10;

			//allows heart images to be drawn
			for(int a = 0; a < 3 ; a++) {
				g2.drawImage(heartImages[1].getImage(),x,y,null);

				x+=55;
			}
			x=20;

			//changes herat images during death
			for(int b = 0; b<lives;b++) {
				g2.drawImage(heartImages[0].getImage(),x,y,null);
				x+=55;
			}




		}

		//LEADERBOARD- gamestate which allows leader board to be displayed
		else if(gameState == 2) {
			g2.setColor(new Color(70,120,90));
			g2.fillRect(-20, 0, 2300, 500);

			g2.setColor(Color.black);
			g2.setFont(new Font("Arial", Font.BOLD, 40));
			g2.drawString("LEADERBOARD", 400, 50);
			g2.setFont(new Font("Arial", Font.BOLD, 20));
			g2.drawString("Press ESC to go back", 30, 30);
			try {
				int x = 200;
				int y = 125;
				int count=0;
				//allows textfile with scores to demonstrate output
				ArrayList<String> leaderBoard1 = leaderBoard.getTop10();

				for(int i = 0; i != 2; i++) {
					for(int a = 0; a!=5;a++) {
						g2.drawString(leaderBoard1.get(count), x, y);
						y+=40;
						count++;
					}
					y=125;
					x+=200;
				}

			}catch (IOException e) {e.printStackTrace();}
		}

		//DEATH SCREEN--THE WORST-where the screen ends up when lives run out
		else if(gameState==3) {
			background1.draw(this, g);
			background2.draw(this, g);


			sprite.draw(g2, this);
			for (Item w: cans) {

				w.draw(g2, this);
			}
			for (Item s: fireHydrants) {
				s.draw(g2, this);
				
				
			}
			for (Item s: banana) {
				s.draw(g2, this);}
			
			g2.setColor(Color.RED);
			Rectangle r = sprite.getBounds();
			g2.draw(r);

			g2.setColor(Color.pink);
			g2.setFont(new Font("Segoe Script", Font.BOLD + Font.ITALIC, 30));
			g2.drawString(String.valueOf((int)score), 1250, 30);

			g2.setColor(Color.red);
			g2.setFont(new Font("Segoe Script", Font.BOLD, 30));

			int x = 20;
			int y = 10;
			for(int a = 0; a < 3 ; a++) {
				g2.drawImage(heartImages[1].getImage(),x,y,null);

				x+=55;
			}
			x=20;
			for(int b = 0; b<lives;b++) {
				g2.drawImage(heartImages[0].getImage(),x,y,null);
				x+=55;
			}

			g2.setFont(new Font("Arial", Font.BOLD, 60));
			g2.setColor(Color.red);

			g2.drawString("You Died!", 300, 100);

			g2.setFont(new Font("Arial", Font.BOLD, 30));
			g2.drawString("Press Enter to Continue", 300, 200);



		}


	}

	// method:clock
	// description: This method is called by the clocklistener every 5 milliseconds.  You should update the coordinates
	// of one of your characters in this method so that it moves as time changes.  After you update the
	// coordinates you should repaint the panel.
	public void clock(){

		//while gameplay is running
		if(gameState==1) {


			if(counter%200==0) {

				score++;
				score*=1.02;
			}
			// You can move any of your objects by calling their move methods.
			sprite.move(this);

			//allows backgrounds to move
			background1.move();
			background2.move();

			//what is used to keep track of how often things are created
			counter++;

			//creates trash cans
			if (counter%levelUp==0) {
				item = (int)(Math.random()*(14)+0);
				if(item>=0 && item<=4)
					cans.add(new Item(background1.getWidth(), (int)(Math.random()*90) + 310, "images/objects/Barrel2.png", 4));
				else if(item>=5 && item<=9)
					fireHydrants.add(new Item(background1.getWidth(), background1.getHeight()-100, "images/objects/Barrel1.png", 4));
				else if(item>=10)
					banana.add(new Item(background1.getWidth(), background1.getHeight()/2 -30, "images/objects/Poisen.png", 4));
			}
			if(counter%1000==0) {
				levelUp-=40;
			}
			//deletes fire hydrants
			for (int i = 0; i <cans.size();i++) {

				cans.get(i).move(this);
				if(cans.get(i).x_coordinate<=-60) {
					cans.remove(i);
				}
			}

			for (int i = 0; i <banana.size();i++) {

				banana.get(i).move(this);
				if(banana.get(i).x_coordinate<=-60) {
					banana.remove(i);
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
			counterIndestructable++;


			//collisions code which allows lives and score to change
			for(Item s: fireHydrants) {
				if(sprite.collision(s)&&!indestructable&&counterIndestructable>60) {
					lives--;
					score-=10;
					counterIndestructable=0;
					//sprite.die();	
				}

			}
			for(Item s: banana) {
				if(sprite.collision(s)&&!indestructable&&counterIndestructable>1000) {

					score-=20;
					counterIndestructable=0;

				}
			}
			//collisions code which allows lives to change
			for(Item s: cans) {
				if(sprite.collision(s)&&!indestructable&&counterIndestructable>75) {
					lives--;
					counterIndestructable=0;
					//sprite.die();	
				}

			}
			//what occurs during the death
			if(lives<=0) {

				this.repaint();


				//checks to see if a new highscore should be added
				isHighScore = leaderBoard.checkScore(score);

				if(isHighScore) {

					try {
						name = JOptionPane.showInputDialog(null, "New High Score!\nEnter Your User Name:");
						if(name!=null)
							leaderBoard.addScore(name,score);
						leaderBoard.updateLeaderBoard();

					} catch (HeadlessException | IOException e) {e.printStackTrace();}
				}

				gameState=3;


			}
			//ALWAYS RUNNING--allows our character to always be running
			if(!sprite.gravityActive)
				sprite.x_direction = 2;

			//GRAVITY- the function enabling our character to fall back to their regular
			//running space
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


		}
		else if(gameState==3) {


			// You can move any of your objects by calling their move methods.
			sprite.move(this);


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
			if (counter%1830==0) {
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


			//ALWAYS RUNNING
			if(!sprite.gravityActive)
				sprite.x_direction = 2;

			//GRAVITY
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
		}

		this.repaint();
	}



	//resets the game - everything below is a reiteration of previous code, just implemented once again
	public void reset() {
		commandNum=0;
		score = 1;
		background1 = new Background(); // You can set the background variable equal to an instance of any of  
		background2 = new Background(-background1.getImage().getIconWidth());
		indestructable = false;
		levelUp=200;
		lives=3;

		// The Item constructor has 4 parameters - the x coordinate, y coordinate
		// the path for the image, and the scale. The scale is used to make the
		// image smaller, so the bigger the scale, the smaller the image will be.
		counter = 0;
		cans = new ArrayList<Item>();
		fireHydrants = new ArrayList<Item>();
		sprite = new Sprite(background1.getWidth()/2-190, background1.getHeight()-200);
		// The Sprite constuctor has two parameter - - the x coordinate and y coordinate

		setPreferredSize(new Dimension(background1.getImage().getIconWidth(),
				background2.getImage().getIconHeight()));  
	}

	// method: keyPressed()
	// description: This method is called when a key is pressed. You can determine which key is pressed using the
	// KeyEvent object.  For example if(e.getKeyCode() == KeyEvent.VK_LEFT) would test to see if
	// the left key was pressed.
	// parameters: KeyEvent e
	@Override
	public void keyPressed(KeyEvent e) {
		if(gameState==0) {
			if(e.getKeyCode() == KeyEvent.VK_UP) {
				commandNum--;
				if(commandNum==-1) 
					commandNum=2;
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				commandNum++;
				if(commandNum==3) 
					commandNum=0;

			}

			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				if(commandNum==0) {
					gameState=1;
					isHighScore = false;
				}
				else if(commandNum==1) {
					gameState=2;
				}
				else if(commandNum==2) {
					System.exit(0);
				}
			}

		}
		//allows a constant gravity multiplier during the gamestate for regular play
		else if(gameState==1) {
			if(e.getKeyCode() == KeyEvent.VK_UP) {
				sprite.jump();
				sprite.gravityActive = true;
				sprite.gravityMultiplier = 0.04;
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				if(!sprite.gravityActive)
					sprite.isSliding=true;
			}
		}
		// sets gamestate of 2 back to the title screen
		else if(gameState==2) {
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				gameState = 0;
			}
		}
		//brings everything back to the title screen
		else if(gameState==3) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				reset();
				gameState=0;
			}
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
			sprite.gravityMultiplier = 0.1;
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			sprite.isSliding=false;
		}

	}

}