package com.quangtd.qmazes.data.model;

import android.graphics.Bitmap;


public class Animation {
    private Bitmap[] frames;
    private int currentFrame;
    private int numFrames;
    private int count;
    private int delay;
    private int timesPlayed;
    private int[] delays;

    public Animation() {
        timesPlayed = 0;
    }

    public void setFrames(Bitmap[] frames) {
        this.frames = frames;
        currentFrame = 0;
        count = 0;
        timesPlayed = 0;
        delay = 2;
        numFrames = frames.length;
    }

    public void setFrames(Bitmap[] frames, int[] delays) {
        this.frames = frames;
        this.delays = delays;
        currentFrame = 0;
        count = 0;
        timesPlayed = 0;
        numFrames = frames.length;
    }

    public void setFrame(int i) {
        currentFrame = i;
    }

    public void update() {
        synchronized (frames) {
            if (delays == null) {
                if (delay == -1) {
                    return;
                }
                count++;
                if (count == delay) {
                    currentFrame++;
                    count = 0;
                }
                if (currentFrame == numFrames) {
                    currentFrame = 0;
                    timesPlayed++;
                }
            } else {
                count++;
                if (count == delays[currentFrame]) {
                    currentFrame++;
                    count = 0;
                }
                if (currentFrame == numFrames) {
                    currentFrame = 0;
                    timesPlayed++;
                }
            }

        }

    }

    public int getFrame() {
        return currentFrame;
    }

    public Bitmap getImage() {
        return frames[currentFrame];
    }

    public boolean hasPlayedOnce() {
        return timesPlayed > 0;
    }

    public boolean hasPlayed(int i) {
        return timesPlayed == i;
    }

    public Bitmap[] getFrames() {
        return frames;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getNumFrames() {
        return numFrames;
    }

    public void setNumFrames(int numFrames) {
        this.numFrames = numFrames;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getTimesPlayed() {
        return timesPlayed;
    }

    public void setTimesPlayed(int timesPlayed) {
        this.timesPlayed = timesPlayed;
    }

    public int[] getDelays() {
        return delays;
    }

    public void setDelays(int[] delays) {
        this.delays = delays;
    }
}
