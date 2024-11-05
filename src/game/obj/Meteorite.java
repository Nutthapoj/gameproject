package game.obj;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import javax.swing.ImageIcon;

public class Meteorite extends  HpRender {
    
    public Meteorite(){
        super(new HP(20, 20));
    this.image = new ImageIcon(getClass().getResource("/game/image/meteorite.png")).getImage();
    Path2D p = new Path2D.Double();
    p.moveTo(0, Meteorite_SIZE); 
    p.lineTo(69, 90);              
    p.lineTo(Meteorite_SIZE - 20, 20); 
    p.lineTo(Meteorite_SIZE - 5, Meteorite_SIZE); 
    p.lineTo(Meteorite_SIZE - 15, Meteorite_SIZE - 13); 
    p.lineTo(30, Meteorite_SIZE - 10); 
    
    meteoriteShap = new Area(p);
}

    
    public static final double Meteorite_SIZE = 50;
    private double x;
    private double y;
    private final float speed = 0.3f;
    private float angle=0;
    private final Image image;
    private final Area meteoriteShap;
    
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
    AffineTransform oldTransform = g2.getTransform();
    g2.translate(x, y);
    AffineTransform tran = new AffineTransform();
    tran.rotate(Math.toRadians(angle + 45), Meteorite_SIZE / 2.6, Meteorite_SIZE / 2.6);
    g2.drawImage(image, tran, null);
    Shape shap = getShape();
    hpRender(g2, shap, y);
    g2.setTransform(oldTransform);
    super.hpRender(g2, getShape(), y);

    
    //g2.setColor(Color.red);
    //g2.draw(shap);
    //g2.draw(shap.getBounds());
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
    
    public Area getShape(){
        AffineTransform afx = new AffineTransform();
        afx.translate(x, y);
        afx.rotate(Math.toRadians(angle),Meteorite_SIZE / 2, Meteorite_SIZE / 2);
        return new Area(afx.createTransformedShape(meteoriteShap));
    }
    
    public boolean check(int width, int height){
        Rectangle size = getShape().getBounds();
        if(x<=-size.getWidth()||y<-size.getHeight()||x>width||y>height){
            return false;
        }else{
            return true;     
        }
    }
    
}
