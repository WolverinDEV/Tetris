package dev.wolverinter.tetris.game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * Created by hadenfmar on 14.09.2017.
 */
public class SoundHelper {
    public static void playSound(String name){
        //if(true) return; //We dont want anoying sounds!

        File soundFile = new File("src\\main\\resources\\", name + ".wav");

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();

            clip.open(ais);
            clip.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
