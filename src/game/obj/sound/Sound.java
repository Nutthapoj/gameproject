package game.obj.sound;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    private final URL shoot;
    private final URL destroy;
    private final URL gamemusic;
    private final URL win;

    public Sound() {
        this.shoot = this.getClass().getClassLoader().getResource("game/obj/sound/shoot.wav");
        this.destroy = this.getClass().getClassLoader().getResource("game/obj/sound/destroy.wav");
        this.gamemusic = this.getClass().getClassLoader().getResource("game/obj/sound/2dgamemusic.wav");
        this.win = this.getClass().getClassLoader().getResource("game/obj/sound/win.wav");
    }
    
    public void soundWin(){
        play(win);
    }
    
    public void soundMusic(){
        play(gamemusic);
    }
    
    public void soundShoot(){
        play(shoot);
    }
    
    public void soundDestroy(){
        play(destroy);
    }
    
    private void play(URL url){
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if(event.getType()==LineEvent.Type.STOP){
                        clip.close();
                    }
                 }
            });
            audioIn.close();
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.err.println(e);
        }
    }
    
    
    
}
