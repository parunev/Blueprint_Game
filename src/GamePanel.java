import javax.swing.*;
import java.awt.*;

//works as game screen
public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 default size of the player character, NPCs and map tiles
    final int scale = 3;
    final int tileSize = originalTileSize * scale; // 16x3 (48x48) pixels on the screen. Pretty common for retro games.

    // 16 tiles horizontally, 12 tiles vertically 4:3 ratio
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // FPS
    int FPS = 60;


    KeyHandler keyH = new KeyHandler();
    //it keeps your program running until you stop it
    Thread gameThread;

    //Set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // set the size of this class (JPanel)
        this.setBackground(Color.black);

        // if set true, all the drawings from this component will be done in an offscreen painting buffer
        // enabling this can improve game's rendering performance
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true); // with this, this GamePanel can be "focused" to receive key inputs
    }

    // passing this(GamePanel) to this thread constructor, instantiate
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start(); // automatically calls the run method
    }

    //Game-Loop, core of our game
    @Override
    public void run() {

        // the allocated time for single loop is 0.016
        double drawInterval = 1000000000/FPS; // we can draw the screen 60times per second
        double nextDrawTime = System.nanoTime() + drawInterval; // returns the current value of the running JVM high resolution time source in nanoseconds;

        // as long as this gameThread exists, it repeats the process that is written inside this brackets
        while (gameThread != null){

            // UPDATE: update information such as character position
            update();

            // DRAW: draw the screen with the updated information
            repaint();

            // sleep method, delta method could be used too
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000; // sleep accepts milliseconds and not nano

                if (remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime); // pause the game loop
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // If the FPS is 30, the program does this 30 times per seconds and so on
        }
    }

    public void update(){
        if (keyH.upPressed){
            playerY -= playerSpeed;
        }else if (keyH.downPressed){
            playerY += playerSpeed;
        }else if (keyH.leftPressed){
            playerX -= playerSpeed;
        }else if (keyH.rightPressed){
            playerX += playerSpeed;
        }
    }

    //Graphics - a class that has many functions to draw objects on screen
    // Imagine this is your paintbrush
    public void paintComponent(Graphics g){
        super.paintComponent(g); // parent class of this class (JPanel)

        // Extends the Graphics class to provide more sophisticated control over
        // geometry, coordinate transformations, color management, and text layout
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.WHITE);
        g2.fillRect(playerX,playerY, tileSize, tileSize); // draws a rectangle and fills it with the specified color
        g2.dispose(); // dispose of this graphics context and release any system resources that it is using
    }
}
