package game.component;

import java.util.List;
import game.obj.Bullet;
import game.obj.Effect;
import game.obj.Meteorite;
import game.obj.Player;
import game.obj.Timer;
import game.obj.sound.Sound;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class PanelGame extends JComponent {

    private Graphics2D g2;
    private BufferedImage image;
    private BufferedImage backgroundImage;
    private BufferedImage sky2Image;
    private int width;
    private int height;
    private Thread thread;
    private boolean start = true;
    private Key key;
    private int shotTime;

    //Game Fps     
    private final int FPS = 60;
    private final int TARGET_TIME = 1000000000 / FPS;
    //Game Object 
    private Sound sound;
    private Player player;
    private List<Bullet> bullets;
    private List<Meteorite> meteorites;
    private List<Effect> boomEffects;
    private int score = 0;
    private Timer gameTimer;
    

    public void start() {
        width = getWidth();
        height = getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Load the background image from the package
        try {
            URL imageUrl = getClass().getResource("/game/image/sky.png");
            if (imageUrl != null) {
                backgroundImage = ImageIO.read(imageUrl);
            } else {
                System.err.println("Background image not found!");
            }
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }
        
        try {
            URL imageUrl = getClass().getResource("/game/image/sky2.png");
            if (imageUrl != null) {
                sky2Image = ImageIO.read(imageUrl);
            } else {
                System.err.println("Second background image not found!");
            }
        } catch (IOException e) {
            System.err.println("Error loading second background image: " + e.getMessage());
        }
        

        gameTimer = new Timer(); 
        gameTimer.startTimer();  
        
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    long startTime = System.nanoTime();
                    drawBackground();
                    drawGame();
                    render();
                    long time = System.nanoTime() - startTime;
                    if (time < TARGET_TIME) {
                        long sleep = (TARGET_TIME - time) / 1000000;
                        sleep(sleep);
                    }
                }
            }
        });
       
        initObjectGame();
        initKeyboard();
        initBullets();
        thread.start();
        sound.soundMusic();
    }
    
    private void addMeteorite(){
        Random ran = new Random();
        int locationY = ran.nextInt(height-50)+25;
        Meteorite meteorite = new Meteorite();
        meteorite.changeLoacation(0, locationY);
        meteorite.changeAngle(0);
        meteorites.add(meteorite);
        int locationY2 = ran.nextInt(height-50)+25;
        Meteorite meteorite2 = new Meteorite();
        meteorite2.changeLoacation(width, locationY2);
        meteorite2.changeAngle(180);
        meteorites.add(meteorite2);
        
    }

    private void initObjectGame(){
        sound = new Sound();
        player = new Player();
        player.changeLoacation(150, 150);
        meteorites = new ArrayList<>(); 
        boomEffects = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(start){
                    addMeteorite();
                    sleep(3000);
                }
            }
        }).start();
        
    }
    
    private void resetGame(){
        score = 0;
        meteorites.clear();
        bullets.clear();
        player.changeLoacation(150, 150);
        player.reset();
        gameTimer.resetTimer();
    }
    
    private void initKeyboard(){
        key = new Key();
        requestFocus();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_A){
                    key.setKey_left(true);
                }else if(e.getKeyCode()==KeyEvent.VK_D){
                    key.setKey_right(true);
                }else if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    key.setKey_space(true);
                }else if(e.getKeyCode()==KeyEvent.VK_J){
                    key.setKey_j(true);
                }else if(e.getKeyCode()==KeyEvent.VK_K){
                    key.setKey_k(true);
                }else if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    key.setKey_enter(true);
            }}

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_A){
                    key.setKey_left(false);
                }else if(e.getKeyCode()==KeyEvent.VK_D){
                    key.setKey_right(false);
                }else if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    key.setKey_space(false);
                }else if(e.getKeyCode()==KeyEvent.VK_J){
                    key.setKey_j(false);
                }else if(e.getKeyCode()==KeyEvent.VK_K){
                    key.setKey_k(false);
                }else if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    key.setKey_enter(false);
            }}
        });
        new Thread(new Runnable() {
    @Override
    public void run() {
        float s = 0.5f;
        while (start) {
            if (player.isAlive() && gameTimer.getTimeRemaining() > 0) {
                float angle = player.getAngle();
                if (key.isKey_left()) {
                    angle -= s;
                }
                if (key.isKey_right()) {
                    angle += s;
                }
                if (key.isKey_j() || key.isKey_k()) {
                    if (shotTime == 0) {
                        if (key.isKey_j()) {
                            bullets.add(0, new Bullet(player.getX(), player.getY(), player.getAngle(), 5, 3f));
                        } else {
                            bullets.add(0, new Bullet(player.getX(), player.getY(), player.getAngle(), 10, 3f));
                        }
                        sound.soundShoot();
                    }
                    shotTime++;
                    if (shotTime == 15) {
                        shotTime = 0;
                    }
                } else {
                    shotTime = 0;
                }
                if (key.isKey_space()) {
                    player.speedUp();
                } else {
                    player.speedDown();
                }
                player.update();
                player.changeAngle(angle);
            } else {
                // เพิ่มการเช็คเวลาหมด
                if (key.isKey_enter() && (gameTimer.getTimeRemaining() <= 0 || !player.isAlive())) {
                    resetGame();
                }
            }

            // การอัพเดท meteorites และ bullet เหมือนเดิม
            for (int i = 0; i < meteorites.size(); i++) {
                Meteorite meteorite = meteorites.get(i);
                if (meteorite != null) {
                    meteorite.update();
                    if (!meteorite.check(width, height)) {
                        meteorites.remove(meteorite);
                    } else {
                        if (player.isAlive()) {
                            checkPlayer(meteorite);
                        }
                    }
                }
            }
            sleep(5);
        }
    }
}).start();
    }
    
    private void initBullets(){
        bullets = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(start){
                    for(int i=0;i<bullets.size();i++){
                        Bullet bullet = bullets.get(i);
                        if(bullet != null){
                            bullet.update();
                            checkBullets(bullet);
                            if(!bullet.check(width, height)){
                                bullets.remove(bullet);
                            }
                        }else{
                            bullets.remove(bullet);
                        }
                    }
                    for(int i=0; i<boomEffects.size();i++){
                        Effect boomEffect = boomEffects.get(i);
                        if(boomEffect != null){
                            boomEffect.update();
                            if(!boomEffect.check()){
                                boomEffects.remove(boomEffect);
                            }
                        }else{
                            boomEffects.remove(boomEffect);
                        }
                    }
                    sleep(1);
                }
            }
        }).start();
    }
    
    private void checkBullets(Bullet bullet){
        for(int i=0; i<meteorites.size(); i++){
            Meteorite meteorite = meteorites.get(i);
            if(meteorite != null){
                Area area = new Area(bullet.getShape());
                area.intersect(meteorite.getShape());
                if(!area.isEmpty()){
                    boomEffects.add(new Effect(bullet.getCenterX(), bullet.getCenterY(), 3, 5, 60, 0.5f, new Color(150, 75, 0) ));
                    if(!meteorite.updateHP(bullet.getSize())){
                        score += 10;
                        meteorites.remove(meteorite);
                        sound.soundDestroy();
                        double x = meteorite.getX() + meteorite.Meteorite_SIZE / 2;
                        double y = meteorite.getY() + meteorite.Meteorite_SIZE / 2;
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(150, 75, 0) ));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(150, 75, 0) ));
                        boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(150, 75, 50) ));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(150, 75, 0) ));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(150, 75, 0) ));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(150, 75, 0) ));
                    }
                    //meteorites.remove(meteorite);
                    bullets.remove(bullet);
                }
            }
        }
    }
    
    private void checkPlayer(Meteorite meteorite){
            if(meteorite != null){
                Area area = new Area(player.getShape());
                area.intersect(meteorite.getShape());
                if(!area.isEmpty()){
                    double meteoriteHP = meteorite.getHP();
                    if(!meteorite.updateHP(player.getHP())){
                        meteorites.remove(meteorite);
                        sound.soundDestroy();
                        double x = meteorite.getX() + meteorite.Meteorite_SIZE / 2;
                        double y = meteorite.getY() + meteorite.Meteorite_SIZE / 2;
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(150, 75, 0) ));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(150, 75, 0) ));
                        boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(150, 75, 50) ));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(150, 75, 0) ));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(150, 75, 0) ));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(150, 75, 0) ));
                    }
                    if(!player.updateHP(meteoriteHP)){
                        player.setAlive(false);
                        
                        
                        sound.soundDestroy();
                        double x = meteorite.getX() + meteorite.Meteorite_SIZE / 2;
                        double y = meteorite.getY() + meteorite.Meteorite_SIZE / 2;
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(150, 75, 0) ));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.1f, new Color(150, 75, 0) ));
                        boomEffects.add(new Effect(x, y, 10, 10, 100, 0.3f, new Color(150, 75, 50) ));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(150, 75, 0) ));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(150, 75, 0) ));
                        boomEffects.add(new Effect(x, y, 5, 5, 75, 0.05f, new Color(150, 75, 0) ));
                    }
                }
            }
        
    }
    
    private void drawBackground() {
    if (gameTimer.getTimeRemaining() <= 30) {
        if (sky2Image != null) {
            g2.drawImage(sky2Image, 0, 0, width, height, null); // Draw the second background image
        } else {
            g2.setColor(new Color(30, 30, 30));
            g2.fillRect(0, 0, width, height);
        }
    } else {
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, width, height, null); // Draw the original background image
        } else {
            g2.setColor(new Color(30, 30, 30));
            g2.fillRect(0, 0, width, height);
        }
    }
}


    private void drawGame() {
        
        if (score >= 200) {
        String winText = "Kelly's House Save!!";
        
        g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D r2 = fm.getStringBounds(winText, g2);

        double textWidth = r2.getWidth();
        double textHeight = r2.getHeight();
        double x = (width - textWidth) / 2;
        double y = (height - textHeight) / 2;

        g2.setColor(Color.BLACK);
        g2.drawString(winText, (int) x, (int) y + fm.getAscent());

        return;  
    }
        
        if(player.isAlive()){
            player.draw(g2);
        }
        g2.setColor(Color.WHITE);
        g2.setFont(getFont().deriveFont(Font.BOLD, 20f));
        g2.drawString("Time Left: " + gameTimer.getTimeRemaining(), width - 150, 30);

        for(int i=0;i<bullets.size();i++){
            Bullet bullet = bullets.get(i);
            if(bullet!=null){
                bullet.draw(g2);
            }
        }
        for(int i=0; i<meteorites.size();i++){
            Meteorite meteorite = meteorites.get(i);
            if (meteorite != null){
                meteorite.draw(g2);
            }
        }
        for(int i=0; i<boomEffects.size(); i++){
            Effect boomEffect = boomEffects.get(i);
            if(boomEffect != null){
                boomEffect.draw(g2);
            }
        }
        g2.setColor(Color.WHITE);
        g2.setFont(getFont().deriveFont(Font.BOLD,15f));
        g2.drawString("Score : "+score, 10, 20);
        
        if (player.isAlive() && gameTimer.getTimeRemaining() > 0) {
        player.draw(g2);
    } else {
        String text = "GAME OVER";
        String textKey = "Press enter to Restart . . .";

        g2.setFont(getFont().deriveFont(Font.BOLD, 50f));
        FontMetrics fm = g2.getFontMetrics();
        Rectangle2D r2 = fm.getStringBounds(text, g2);

        double textWidth = r2.getWidth();
        double textHeight = r2.getHeight();
        double x = (width - textWidth) / 2;
        double y = (height - textHeight) / 2;

        g2.drawString(text, (int) x, (int) y + fm.getAscent());
        g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
        fm = g2.getFontMetrics();
        r2 = fm.getStringBounds(textKey, g2);

        textWidth = r2.getWidth();
        textHeight = r2.getHeight();
        x = (width - textWidth) / 2;
        y = (height - textHeight) / 2;

        g2.drawString(textKey, (int) x, (int) y + fm.getAscent() + 50);
    }

    g2.setColor(Color.WHITE);
    g2.setFont(getFont().deriveFont(Font.BOLD, 20f));
    g2.drawString("Time Left: " + gameTimer.getTimeRemaining(), width - 150, 30);

    for (int i = 0; i < bullets.size(); i++) {
        Bullet bullet = bullets.get(i);
        if (bullet != null) {
            bullet.draw(g2);
        }
    }

    for (int i = 0; i < meteorites.size(); i++) {
        Meteorite meteorite = meteorites.get(i);
        if (meteorite != null) {
            meteorite.draw(g2);
        }
    }

    for (int i = 0; i < boomEffects.size(); i++) {
        Effect boomEffect = boomEffects.get(i);
        if (boomEffect != null) {
            boomEffect.draw(g2);
        }
    }

    g2.setColor(Color.WHITE);
    g2.setFont(getFont().deriveFont(Font.BOLD, 15f));
    g2.drawString("Score : " + score, 10, 20);
}

    private void render() {
        Graphics g = getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }

    private void sleep(long speed) {
        try {
            Thread.sleep(speed);
        } catch (InterruptedException ex) {
            System.err.println(ex);
        }
    }
}



    
 