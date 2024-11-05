package game.obj;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import javax.swing.*;

public class Player extends HpRender {

    public Player() {
        super(new HP(50, 50));
        this.image = new ImageIcon(getClass().getResource("/game/image/kelly_1.png")).getImage();
        this.image_speed = new ImageIcon(getClass().getResource("/game/image/kelly_speed_1.png")).getImage();;
        Path2D p = new Path2D.Double();
        p.moveTo(0, 0); // Top-left corner of the rectangle
        p.lineTo(PLAYER_SIZE + 60, 0); // Top-right corner
        p.lineTo(PLAYER_SIZE + 60, PLAYER_SIZE + 75); // Bottom-right corner
        p.lineTo(0, PLAYER_SIZE + 75); // Bottom-left corner
        playerShap = new Area(p);
    }
    
    
    
    public  static final double PLAYER_SIZE = 64;
    private double x;
    private double y;
    private final float MAX_SPEED = 1f;
    private float speed = 0f;
    private float angle=0f;
    private final Area playerShap;
    private final Image image;
    private final Image image_speed;
    private boolean speedUp;
    private boolean alive = true;
    
    public void changeLoacation(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public void update(){
        x+=Math.cos(Math.toRadians(angle))*speed;
        y+=Math.sin(Math.toRadians(angle))*speed;
        
    }
    
    public void changeAngle(float angle){
        if(angle<0){
            angle = 359;
        }
        else if(angle > 359){
            angle = 0;
        }
        this.angle = angle;
    }
    
    public void draw(Graphics2D g2){
        AffineTransform oldTransform=g2.getTransform();
        g2.translate(x, y);
        AffineTransform tran = new AffineTransform();
        tran.rotate(Math.toRadians(angle+65),PLAYER_SIZE, PLAYER_SIZE);
        g2.drawImage(speedUp ? image_speed : image, tran, null);
        hpRender(g2, getShape(), y);
        
        g2.setTransform(oldTransform);
        super.hpRender(g2, getShape(), y);
        //test
        //g2.setColor(Color.red);
        //g2.draw(getShape());
        //g2.draw(getShape().getBounds());
    }
    
    public Area getShape(){
        AffineTransform afx = new AffineTransform();
        afx.translate(x, y);
        afx.rotate(Math.toRadians(angle),PLAYER_SIZE / 2, PLAYER_SIZE / 2);
        return new Area(afx.createTransformedShape(playerShap));
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public float getAngle(){
        return angle;
    }
    
    public void speedUp(){
        speedUp = true;
        if(speed>MAX_SPEED){
            speed = MAX_SPEED;
        }else{
            speed += 0.01f;
        }
    }
    
    public void speedDown(){
        speedUp = false;
        if(speed<=0){
            speed = 0;
        }else{
            speed -= 0.003f;
        }
    }
    
    public boolean isAlive(){
        return alive;
    }
    
    public void setAlive(boolean alive){
        this.alive = alive;
    }
    
    public void reset(){
        alive = true;
        resetHP();
        angle = 0;
        speed = 0;
    }
}
   
