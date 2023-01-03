package Main;

import Entity.Entity;
import Entity.Player;
import Tile.TileManager;
import TileInteractive.InteractiveTile;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

//works as game screen
public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 default size of the player character, NPCs and map tiles
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 16x3 (48x48) pixels on the screen. Pretty common for retro games.

    // 16 tiles horizontally, 12 tiles vertically 4:3 ratio
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // FPS
    int FPS = 60;

    // SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Thread gameThread; //it keeps your program running until you stop it

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity[] obj = new Entity[20];
    public Entity[] npc = new Entity[10];
    public Entity[] monster = new Entity[20];
    public InteractiveTile[] iTile = new InteractiveTile[50];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();

    // GAME STATE
    // When you play a game usually it has various game situations - title screen, main gameplay screen or in-game menu screen
    // and depending on the situation the program draws different things on the screen and often receive diff key input
    // Example: You can swing your sword by pressing enter in gameplay state but maybe enter works as confirm in menu screen
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;

    public GamePanel() throws IOException, FontFormatException {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // set the size of this class (JPanel)
        this.setBackground(Color.black);

        // if set true, all the drawings from this component will be done in an offscreen painting buffer
        // enabling this can improve game's rendering performance
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true); // with this, this Main.GamePanel can be "focused" to receive key inputs
    }

    // created this method, so we can add other setup stuff in the future
    public void setupGame(){
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTiles();
        gameState = titleState;
    }

    // passing this(Main.GamePanel) to this thread constructor, instantiate
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
        if (gameState == playState){
            // PLAYER
            player.update();

            // NPC
            for (Entity entity : npc) {
                if (entity != null) {
                    entity.update();
                }
            }

            // MONSTER
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive && !monster[i].dying) {
                        monster[i].update();
                    }
                    if (!monster[i].alive) {
                        monster[i].checkDrop(); // when a monster died we check his drop before we set it null
                        monster[i] = null;
                    }
                }
            }

            // PROJECTILE
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive) {
                        projectileList.get(i).update();
                    }
                    if (!projectileList.get(i).alive) {
                        projectileList.remove(i);
                    }
                }
            }

            // PARTICLES
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    if (particleList.get(i).alive) {
                        particleList.get(i).update();
                    }
                    if (!particleList.get(i).alive) {
                        particleList.remove(i);
                    }
                }
            }

            // INTERACTIVE TILES
            for (InteractiveTile interactiveTile : iTile) {
                if (interactiveTile != null) {
                    interactiveTile.update();
                }
            }
        }

        // We don't update player's information while the game is paused
        if (gameState == pauseState){

        }
    }

    //Graphics - a class that has many functions to draw objects on screen
    // Imagine this is your paintbrush
    public void paintComponent(Graphics g){
        super.paintComponent(g); // parent class of this class (JPanel)

        // Extends the Graphics class to provide more sophisticated control over
        // geometry, coordinate transformations, color management, and text layout
        Graphics2D g2 = (Graphics2D) g;

        // DEBUG
        long drawStart = 0;
        if (keyH.showDebugText){
            drawStart = System.nanoTime();
        }

        // TITLE SCREEN
        if (gameState == titleState){
            ui.draw(g2);
        }
        // OTHERS
        else{
            // TILE
            tileM.draw(g2); // first tiles, it's like a layout

            // INTERACTIVE TILES
            for (InteractiveTile interactiveTile : iTile) {
                if (interactiveTile != null) {
                    interactiveTile.draw(g2);
                }
            }

            // ADD ENTITIES TO THE LIST
            entityList.add(player);

            for (Entity n : npc) {
                if (n != null) {
                    entityList.add(n);
                }
            }

            for (Entity o : obj) {
                if (o != null) {
                    entityList.add(o);
                }
            }

            for (Entity m : monster) {
                if (m != null) {
                    entityList.add(m);
                }
            }

            for (Entity p : projectileList) {
                if (p != null) {
                    entityList.add(p);
                }
            }
            for (Entity p : particleList) {
                if (p != null) {
                    entityList.add(p);
                }
            }

            // SORT
            entityList.sort(Comparator.comparingInt((Entity e) -> e.worldY));

            // DRAW ENTITIES
            for (Entity entity : entityList) {
                entity.draw(g2);
            }

            // EMPTY ENTITY LIST
            entityList.clear();

            // UI
            ui.draw(g2);
        }

        // DEBUG
        if (keyH.showDebugText){
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.WHITE);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            g2.drawString("WorldX" + player.worldX, x, y); y += lineHeight;
            g2.drawString("WorldY" + player.worldY, x, y); y += lineHeight;
            g2.drawString("COL" + (player.worldX + player.solidArea.x)/tileSize, x, y); y += lineHeight;
            g2.drawString("ROW" + (player.worldY + player.solidArea.y)/tileSize, x, y); y += lineHeight;
            g2.drawString("Draw time: "+ passed, x, y);
        }

        g2.dispose(); // dispose of this graphics context and release any system resources that it is using
    }

    // Loop makes the music play non-stop
    public void playMusic(int i){
        music.setFile(i); // we set the file we want to play
        music.play(); // we play
        music.loop(); // we loop
    }

    // Stops the music at all
    public void stopMusic(){
        music.stop();
    }

    // Play sound effect
    // Usually the sound effect is very short, so we don't call the loop here
    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
}
