package dev.wolverinter.tetris.game;

import lombok.RequiredArgsConstructor;

/**
 * Created by hadenfmar on 08.09.2017.
 */
@RequiredArgsConstructor
public class TimingHelper {
    private final long delay;
    private long lastExecute;

    public boolean shouldExecute(){
        return lastExecute == 0 || (System.currentTimeMillis() - delay > lastExecute);
    }

    public void triggerExecute(){
        lastExecute = System.currentTimeMillis();
    }
}
