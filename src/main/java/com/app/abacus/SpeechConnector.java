package com.app.abacus;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 * Class used to connect to VAAS Acapela API to generate the URL 
 * contains audio for instruction text
 * 
 * @author prashant.joshi (198joshi@gmail.com)
 * @version 12-May-2016
 */
public class SpeechConnector implements LineListener {
	
	private Clip clip;
	
	private boolean isClipFinished;

	/** Creating a default constructor */
	public SpeechConnector() {}
	
	@Override
	public void update(LineEvent le) {
		LineEvent.Type type = le.getType();
		if (type == LineEvent.Type.STOP) {
			stopClip();
	    }
	}
	
	/* Stops the given audio Clip from playing. */
	public void stopClip() {
		if (clip != null) {
			clip.stop();
			clip.setFramePosition(0);
			synchronized (clip) {
				clip.notifyAll();  // awaken anyone waiting for this Clip
			}
		}
	}
	
	/** Play audio sound using URL  */
	public void playSound(String url) {
		isClipFinished = false;
		AudioInputStream din = null;
	    try {
	        //AudioInputStream in = AudioSystem.getAudioInputStream(new File(url));
	    	AudioInputStream in = AudioSystem.getAudioInputStream(this.getClass().getResourceAsStream(url));
	        if(clip != null) {
	        	clip.stop();
	        }
	        clip = AudioSystem.getClip( );
            clip.open(in);
            clip.start( );
            clip.addLineListener(this);
            try {
				synchronized (clip) {
					clip.wait();  // sleep until notified
				}
			} catch (InterruptedException ie) {}
            clip.close();
	    }
	    catch(Exception e) {
	        e.printStackTrace();
	    }
	    finally {
	        if(din != null) {
	            try { din.close(); } catch(IOException e) { }
	        }
	    }
	}

	/**
	 * @return the clip
	 */
	public Clip getClip() {
		return clip;
	}
	
}
