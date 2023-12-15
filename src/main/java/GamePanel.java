package main.java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static int DELAY = 100;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 0; // bodyParts for the snake at the beginning of the game
	int applesEaten = 0;
	int appleX; // coordonates for the apple when will appear randomly
	int appleY;
	char direction = 'R'; // R - right, L - left, U - up, D - down
	boolean running = false;
	Timer timer;
	Random random;

	public GamePanel() {
		random = new Random();
		timer = new Timer(DELAY, this);
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		direction = 'R';
		x[0] = 0;
		y[0] = 0;

		newApple();
		DELAY = 100;
		bodyParts = 6;
		applesEaten = 0;
		timer.setDelay(DELAY);
		timer.start();
		running = true;

		System.out.println("i'm start game");
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);

	}

	public void draw(Graphics g) {

		if (running) {

			// Testing purpose only, Draw a grid
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, i * SCREEN_WIDTH, i * UNIT_SIZE);
			}

			// Draw Snake FOOD
			g.setColor(Color.red); // drawing the apple
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // set the apple in the game

			// Draw the SNAKE
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					// SNAKE HEAD
					g.setColor(Color.GREEN);
					//g.fill3DRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE, true);
					g.fillRoundRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE,5, 5);
				} else {
					// SNAKE BODYPARTS
					g.setColor(new Color(10, 90, 0));
					g.fill3DRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE, true);
				}
			}

			// Draw Score on top center of the screen
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metricts = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_HEIGHT - metricts.stringWidth("Score: " + applesEaten)) / 2,
					g.getFont().getSize());

		} else {
			gameOver(g);
		}
	}

	public void newApple() {
		// checks if head collides with body
//		for (int i = bodyParts; i > 0; i--) {
//			if ((x[0] == x[i]) && (y[0] == y[i])) {
//			
//			}else {	}
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		switch (direction) {
		case 'U': {
			y[0] = y[0] - UNIT_SIZE;
			break;
		}
		case 'D': {
			y[0] = y[0] + UNIT_SIZE;
			break;
		}
		case 'L': {
			x[0] = x[0] - UNIT_SIZE;
			break;
		}
		case 'R': {
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + direction);
		}
	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
			DELAY = DELAY - 10;
			if (DELAY >= 50) {
				timer.setDelay(DELAY);
				System.out.println(DELAY);
			}

		}

	}

	public void checkCollisions() {
		// checks if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// checks if head touches left border
		if (x[0] < 0) {
				//running = false;
			x[0] = SCREEN_WIDTH - UNIT_SIZE;
			System.out.println("x{0}: " + x[0] + " y[0]: " + y[0] + "		LEFT");
		}
		// checks if head touches right border
		if (x[0] > SCREEN_WIDTH) {
				//running = false;
			
			x[0] = 0;
			System.out.println("x{0}: " + x[0] + " y[0]: " + y[0]+ "		right");
		}
		// checks if head touches top border
		if (y[0] < 0) {
				//running = false;
			y[0] = SCREEN_HEIGHT - UNIT_SIZE;
			System.out.println("x{0}: " + x[0] + " y[0]: " + y[0]+ "		up");
		}

		// checks if head touches bottom border
		if (y[0] >= SCREEN_HEIGHT) {
				//running = false;
			y[0] = 0;
			System.out.println("x{0}: " + x[0] + " y[0]: " + y[0]+ "		down");
		}
		if (!running) {
			timer.stop();
		}
		if(x[0] == SCREEN_WIDTH) {
			x[0] = 0;
		}if(y[0] == SCREEN_HEIGHT) {
			y[0] = 0;
		}

	}

	public void gameOver(Graphics g) {
		// gameOverText
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metricts1 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_HEIGHT - metricts1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metricts2 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_HEIGHT - metricts2.stringWidth("Score: " + applesEaten)) / 2,
				g.getFont().getSize());

		// add restartGame BTN
		g.setColor(Color.WHITE);
		g.setFont(new Font("Ink Free", Font.BOLD, 20));
		FontMetrics metricts3 = getFontMetrics(g.getFont());
		g.drawString("Press SPACE to restart! :)",
				(SCREEN_HEIGHT - metricts3.stringWidth("Press SPACE to restart! :)")) / 2, 400);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;

			case KeyEvent.VK_SPACE:
				running = true;
				timer.restart();
				repaint();
				startGame();

				break;
			}
		}
	}

}
