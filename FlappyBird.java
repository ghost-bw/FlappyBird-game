import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    //images
    Image backgroundImg;
    Image backgroundImg1;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;
    Clip jumpClip;
    Clip hitClip;
    String audioStatusMessage = "Audio ready";

    //bird class
    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    //pipe class
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;  //scaled by 1/6
    int pipeHeight = 512;
    
    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    //game logic
    Bird bird;
    int velocityX = -4; //move pipes to the left speed (simulates bird moving right)
    double velocityY = 0; //move bird up/down speed.
    double gravity = 0.5;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    Timer backgroundSwitchTimer;
    boolean gameOver = false;
    double score = 0;
    boolean useSecondBackground = false;

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg2.png")).getImage();
        backgroundImg1 = new ImageIcon(getClass().getResource("./flappybirdbg1.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        jumpClip = loadClip("jump");
        hitClip = loadClip("hit");

        //bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        //place pipes timer
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              // Code to be executed
              placePipes();
            }
        });
        placePipeTimer.start();
        
        //background switch timer
        backgroundSwitchTimer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                useSecondBackground = !useSecondBackground;
                if (useSecondBackground) {
                    backgroundImg = backgroundImg1;
                } else {
                    backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg2.png")).getImage();
                }
            }
        });
        backgroundSwitchTimer.start();
        
		//game timer
		gameLoop = new Timer(1000/60, this); //how long it takes to start timer, milliseconds gone between frames 
        gameLoop.start();
	}
    
    void placePipes() {
        //(0-1) * pipeHeight/2.
        // 0 -> -128 (pipeHeight/4)
        // 1 -> -128 - 256 (pipeHeight/4 - pipeHeight/2) = -3/4 pipeHeight
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;
    
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);
    
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y  + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }
    
    
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
        //background
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);

        //bird
        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score
        g.setColor(Color.white);

        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString(audioStatusMessage, 10, boardHeight - 10);
        
	}

    public void move() {
        //bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0); //apply gravity to current bird.y, limit the bird.y to top of the canvas

        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5; //0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
                pipe.passed = true;
            }

            if (collision(bird, pipe)) {
                triggerGameOver();
            }
        }

        if (bird.y > boardHeight) {
            triggerGameOver();
        }
    }

    Clip loadClip(String soundName) {
        String[] supportedExtensions = {".wav", ".aiff", ".au"};

        for (String extension : supportedExtensions) {
            Clip clip = loadSupportedClip(soundName + extension);
            if (clip != null) {
                return clip;
            }
        }

        File mp3File = new File(soundName + ".mp3");
        if (!mp3File.exists()) {
            mp3File = new File("flappy-bird-java", soundName + ".mp3");
        }
        if (mp3File.exists()) {
            audioStatusMessage = "MP3 found for " + soundName + ". Convert it to " + soundName + ".wav";
            System.out.println("Found " + mp3File.getName() + ", but MP3 is not supported by this Swing sound setup. Convert it to WAV and keep the same name.");
        } else {
            audioStatusMessage = "Missing " + soundName + ".wav";
            System.out.println("Sound file not found for " + soundName + ". Add " + soundName + ".wav to the game folder.");
        }

        return null;
    }

    Clip loadSupportedClip(String fileName) {
        URL soundUrl = getClass().getResource("./" + fileName);

        try {
            if (soundUrl != null) {
                try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundUrl)) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    audioStatusMessage = "Audio ready";
                    return clip;
                }
            }

            File soundFile = new File(fileName);
            if (!soundFile.exists()) {
                soundFile = new File("flappy-bird-java", fileName);
            }
            if (!soundFile.exists()) {
                return null;
            }

            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile)) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                audioStatusMessage = "Audio ready";
                return clip;
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            audioStatusMessage = "Could not load " + fileName;
            System.out.println("Could not load sound: " + fileName);
            e.printStackTrace();
            return null;
        }
    }

    void playClip(Clip clip) {
        if (clip == null) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    void triggerGameOver() {
        if (!gameOver) {
            gameOver = true;
            playClip(hitClip);
        }
    }

    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
               a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) { //called every x milliseconds by gameLoop timer
        move();
        repaint();
        if (gameOver) {
            placePipeTimer.stop();
            gameLoop.stop();
            backgroundSwitchTimer.stop();
        }
    }  

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (gameOver) {
                //restart game by resetting conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipeTimer.start();
                backgroundSwitchTimer.start();
            }

            velocityY = -9;
            playClip(jumpClip);
        }
    }

    //not needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
