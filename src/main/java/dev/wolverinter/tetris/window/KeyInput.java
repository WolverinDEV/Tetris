package dev.wolverinter.tetris.window;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadenfmar on 12.09.2017.
 */
public class KeyInput {
    protected long[] currentTickStates = new long[KeyEvent.KEY_LAST];
    protected long[] lastTickStates;

    public boolean keyPressed(int key){
        return lastTickStates[key] > 0;
    }

    public boolean keyPressed(int key, long lastTime){
        return keyPressed(key, lastTime, 200);
    }

    public boolean keyPressed(int key, long lastTime, long delay){
        long res = lastTickStates[key];
        if(res == 0) return false;
        if(lastTime < res) return true;
        if(System.currentTimeMillis() - lastTime >= delay) return true;
        return false;
    }

    protected void pushStates(){
        lastTickStates = currentTickStates;
        currentTickStates = new long[KeyEvent.KEY_LAST];
    }

    public List<Integer> pressedKeys(){
        List<Integer> result = new ArrayList<>();
        for(long elm : lastTickStates)
            if(elm > 0) result.add((int) elm);
        return result;
    }
}
