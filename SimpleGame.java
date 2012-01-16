// Kaitlin Smith
// 0645771
// SFWR ENG 4HC3 - Assignment 4
// Code is based on a tutorial found at 
// http://javaboutique.internet.com/tutorials/Java_Game_Programming/index.html.
// A simple game in which you need to click the ball before it touches the
// bottom of the screen.

import java.applet.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

public class SimpleGame extends Applet implements Runnable {

    Random rand = new Random();
    int ySpeed; // game speed, difficulty
    int radius = 20;
    int windowX = 800;
    int windowY = 300;
    int xPos = rand.nextInt(windowX + radius);
    int yPos = 0;
    int lives = 5;
    int score = 0;
    int mode;
    boolean gameActive = false;
    private Image dbImage;
    private Graphics dbg;
    Font title = new Font("serif", Font.BOLD, 40);
    Font normal = new Font("serif", Font.PLAIN, 20);

    public boolean checkClick(int mouseX, int mouseY) {
        double x = mouseX - xPos;
        double y = mouseY - yPos;

        double distance = Math.sqrt((x * x) + (y * y));

        if (distance < 15) {
            score++;
            return true;
        } else {
            return false;
        }
    }

    public boolean keyDown(Event e, int key) {
        if (!gameActive) {
            if (key == KeyEvent.VK_1) {
                ySpeed = 3;
                gameActive = true;
                lives = 5;
                score = 0;
            } else if (key == KeyEvent.VK_2) {
                ySpeed = 7;
                gameActive = true;
                lives = 5;
                score = 0;
            } else if (key == KeyEvent.VK_3) {
                ySpeed = 12;
                gameActive = true;
                lives = 5;
                score = 0;
            }                
        }
        return true;
    }

    public void init() {
        setBackground(Color.LIGHT_GRAY);
        setSize(windowX, windowY);
    }

    public void start() {
        Thread th = new Thread(this);
        th.start();
    }

    public boolean mouseDown(Event e, int x, int y) {
        if (checkClick(x, y)) {
            yPos = 0;
            xPos = rand.nextInt(windowX + radius);
        }
        return true;
    }

    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        while (true) {
            // Make sure the ball is always visible
            if (xPos > windowX) {
                xPos = xPos - radius;
            }
            
            // Reset the ball position when it reaches the bottom of the window
            if (yPos - radius > windowY) {
                yPos = 0;
                xPos = rand.nextInt(windowX + radius);
                lives -= 1;
            }
            // Move the ball downward - Java uses down as the positive Y axis!
            yPos += ySpeed;
            repaint();

            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                // Do nothing
            }

            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        }
    }

    public void update(Graphics g) {
        // Initialize the buffer
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
        }
        // Clear the background
        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

        // Draw elements in the background
        dbg.setColor(getForeground());
        paint(dbg);

        // Draw the image
        g.drawImage(dbImage, 0, 0, this);
    }

    public void paint(Graphics g) {
        // If the player still has lives remaining
        if (!gameActive && lives == 5) {
            g.setColor(Color.ORANGE);
            g.setFont(title);
            g.drawString("BALL DROP", 300, 30);

            g.setColor(Color.BLACK);
            g.setFont(normal);
            g.drawString("Click the ball before it hits the ground.", 260, 60);
            g.drawString("If the ball hits the ground, you lose a life!", 250, 80);
            g.drawString("Please choose a difficulty by pressing the corresponding keyboard number:", 5, 120);
            g.drawString("1: Easy", 5, 140);
            g.drawString("2: Intermediate", 5, 160);
            g.drawString("3: Difficult", 5, 180);
        } else if (lives > 0 && gameActive) {
            g.setColor(Color.ORANGE);
            g.fillOval(xPos - radius, yPos - radius, 2 * radius, 2 * radius);
            g.setColor(Color.BLACK);
            g.setFont(normal);
            g.drawString("Lives: " + lives, 5, 20);
            g.drawString("Score: " + score, 5, 40);
        } else if (lives < 0) {
            gameActive = false;
            
            g.setColor(Color.BLACK);
            g.setFont(normal);
            g.drawString("Game over! Your final score is: " + score, 300, 20);
            g.drawString("Please choose a difficulty to play again:", 5, 120);
            g.drawString("1: Easy", 5, 140);
            g.drawString("2: Intermediate", 5, 160);
            g.drawString("3: Difficult", 5, 180);
        }
    }
}