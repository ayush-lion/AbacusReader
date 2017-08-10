/**
 * 
 */
package com.app.abacus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
 * Class is responsible to read the instruction and handle the actions
 * on Abacus
 * 
 * @author prashant.joshi (198joshi@gmail.com)
 * @version 12-May-2016
 */
public class InstructionReader implements Runnable {

	private static final String VOICE = "kevin16";
	private VoiceManager vm = null;
	private Voice voice = null;
	private TextPanel tPanel;
	private AbacusPanel panel;
	private Thread readerThread;
	private boolean isDataReadyForRead;
	private String voiceSelected;
	private String dataToRead = null;
	private boolean isExecutionCompleted;
	private List<String> instructions;
	private ArrayList<String> listOfActions = null;
	private SpeechConnector playSound;
	String[] commands = { "AddRod", "MinusRod", "Wait", "HighlightFrame", "HighlightRods", "HighlightBeam", "HighlightLowerBeads", "HighlightUpperBeads", "HighlightDots", "Reset", "Display" };
	String[] listOfHtmlChars = {"<B>","</B>","<b>","</b>","<U>","</U>","<u>","</u>","<I>","</I>","<i>","</i>","<font color=\"red\">","<font color=\"cyan\">","</font>"};
	
	private boolean isPlayRobotics;
	private boolean isPlayNatural;
	
	private AudioInputStream stream;
	
	public InstructionReader() {
		System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
		isDataReadyForRead = false;
		isExecutionCompleted = true;
		isPlayRobotics = false;
		isPlayNatural = false;
		try {
			stream = AudioSystem.getAudioInputStream(
					this.getClass().getResourceAsStream("/com/app/images/Robot_blip-Marianne_Gagnon.wav"));
		} 
		catch (UnsupportedAudioFileException e) {  /*Eating exception*/  }
		catch (IOException e) {  /*Eating exception*/  }
	}
	
	public void startReading() {
		readerThread = new Thread(this);
		readerThread.start();
	}
	
	@Override
	public void run() {
		while(true) {
			if(isDataReadyForRead && isExecutionCompleted) {
				startInstructingStudent();
			}
			/*try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {  Eating exception  }*/
		}
	}
	
	/**
	 * Start demonstrating to viewers
	 */
	protected void startInstructingStudent() {
		int i = 0;
		int counter = 0;
		while(i<instructions.size()) {
			
			String insTxt = instructions.get(i);
			if(!insTxt.startsWith("<action>")) {
				insTxt = insTxt.trim();
				counter++;
			} else {
				insTxt = null;
			}
			
			String actTxt = instructions.get(i + 1);
			if(actTxt.startsWith("<action>")) {
				i = i + 2;
				actTxt = actTxt.trim();
				actTxt = actTxt.substring(8, actTxt.length() - 9);
			} else {
				i = i + 1;
				actTxt = null;
			}
			
			/** Parse actions */
			if(actTxt != null) {
				listOfActions = new ArrayList<String>();
				parseActions(actTxt);
			}
			
			/** Display Instruction */
			if(insTxt != null) {
				ArrayList<String> lines = new ArrayList<String>();
				lines = fragmentText(insTxt, 100);
				tPanel.drawText(lines);
			}
			
			/** Handle highlight action events */
			if(actTxt != null) {
				handleHightlightAction();
			}
			
			/** Reading instruction */
			if(insTxt != null) {
				insTxt = replaceHTMLCharacters(insTxt);
				System.out.println(insTxt);
				if(isPlayRobotics) {
					playRoboticsVoice(insTxt);
				} else if(isPlayNatural) {
					playText(insTxt, counter);
				}
			}
			
			/** Handle actions */
			if(actTxt != null) {
				handleActions();
			}
		}
	}
	
	private String replaceHTMLCharacters(String instruction) {
		for (String htmlChar : listOfHtmlChars) {
			instruction = instruction.replaceAll(htmlChar, "");
		}
		return instruction;
	}
	
	private void playRoboticsVoice(String insTxt) {
		String[] txt = insTxt.split("\n");
		String txtInput = "";
		for (String data : txt) {
			if (!data.trim().equalsIgnoreCase("")) {
				txtInput = txtInput + data + " ";
			}
		}
		try {
			/** Setting up voice manager */
			vm = VoiceManager.getInstance();
			voice = vm.getVoice("kevin16");
			voice.allocate();
			voice.speak(txtInput);
			voice.deallocate();
	    }catch(Exception e){ e.printStackTrace(); }
		finally {
			setDataReadyForRead(false);
		}
	}
	
	/**
	 * Method used to read the text
	 */
	private void playText(String insTxt, int counter) {
		String[] txt = insTxt.split("\n");
		String txtInput = "";
		for (String data : txt) {
			if (!data.trim().equalsIgnoreCase("")) {
				txtInput = txtInput + data + " \\pau=1000\\ ";
			}
		}

		playSound = new SpeechConnector();
		try {
			//playSound.Speak("sharon22k", txtInput);
			playSound.playSound("/com/app/instructions/voice/" + counter + ".wav");
			setDataReadyForRead(false);
		} catch (IllegalArgumentException e1) { /** Eating exceptions */
		} catch (IllegalStateException e1) { /** Eating exceptions */}
	}
	
	/**
	 * Parse actions from instruction sheet
	 * @param actions
	 */
	private void parseActions(String actions) {
		if (actions != null && actions.trim().length() > 0) {
			String[] actionList = actions.split("\n");

			for (String action : actionList) {
				String act = "";
				for (int i = 0; i < action.length(); i++) {
					if (action.charAt(i) != ' ') {
						act = act + action.charAt(i);
					}
				}
				listOfActions.add(act.toLowerCase());
			}

		}
	}
	
	/**
	 * Method responsible to compile the action and process it 
	 */
	private void handleActions() {
		for (String action : listOfActions) {
			for (String command : commands) {
				String comm = command.toLowerCase();
				if (action.startsWith(comm)
						&& command.equalsIgnoreCase("wait")) {
					Integer waitTime = Integer.parseInt(action.substring(4));
					try {
						TimeUnit.SECONDS.sleep(waitTime);
					} catch (InterruptedException e) { /** eating exception */ }
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("display")) {
					String total = action.substring(7);
					panel.displayTotal(total);
					panel.repaint();
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("AddRod")) {
					int rodNumber = Integer.parseInt(action.substring(6, 7));
					int beadNumber = Integer.parseInt(action.substring(11, 12));
					String parseActionFinger = action.substring(12, 21);
					System.out.println("parseActionFinger  : " + parseActionFinger);
					
					panel.setupAddBeadsBefore(rodNumber, beadNumber, parseActionFinger);
					panel.repaint();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) { e.printStackTrace(); }
					panel.setupAddBeadsAfter(rodNumber, beadNumber, parseActionFinger);
					panel.repaint();
					
					playSound();
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) { e.printStackTrace(); }
					panel.setupAddBeads(rodNumber, beadNumber);
					panel.repaint();
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("MinusRod")) {
					int rodNumber = Integer.parseInt(action.substring(8, 9));
					int beadNumber = Integer.parseInt(action.substring(13,14));

					String parseActionFinger = action.substring(14, 23);
					System.out.println("parseActionFinger  : " + parseActionFinger);
					
					panel.setupMinusBeadsBefore(rodNumber, beadNumber, parseActionFinger);
					panel.repaint();
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) { e.printStackTrace(); }
					panel.setupMinusBeadsAfter(rodNumber, beadNumber, parseActionFinger);
					panel.repaint();
					playSound();
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) { e.printStackTrace(); }
					panel.setupMinusBeads(rodNumber, beadNumber);
					panel.repaint();
					
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("HighlightFrame")) { //HighlightDots
					panel.setHighLightFrame(false);
					panel.repaint();
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("HighlightRods")) {
					panel.setHighLightRods(false);
					panel.repaint();
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("HighlightBeam")) {
					panel.setHighLightBeam(false);
					panel.repaint();
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("HighlightLowerBeads")) {
					panel.highlightLowerBeads(false);
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("HighlightUpperBeads")) {
					panel.highlightUpperBeads(false);
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("HighlightDots")) {
					panel.setHighLightDots(false);
					panel.repaint();
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("Reset")) {
					panel.resetBeads();
					panel.repaint();
				}
			}

		}
	}
	
	/**
	 * Method used to handle the highlight events
	 */
	private void handleHightlightAction() {
		for (String action : listOfActions) {
			for (String command : commands) {
				String comm = command.toLowerCase();
				if (action.startsWith(comm)
						&& command.equalsIgnoreCase("HighlightFrame")) {
					panel.highlightFrame();
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("HighlightRods")) {
					panel.highlightRods();
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("HighlightBeam")) {
					panel.highlightBeam();
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("HighlightLowerBeads")) {
					panel.highlightLowerBeads(true);
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("HighlightUpperBeads")) {
					panel.highlightUpperBeads(true);
				} else if (action.startsWith(comm)
						&& command.equalsIgnoreCase("HighlightDots")) {
					panel.highlightDots();
				}
			}
		}
	}
	
	/**
	 * Method is responsible to wrap the text if it is more than frame width
	 * @param text
	 * @param maxWidth
	 * @return
	 */
	private ArrayList<String> fragmentText(String text, int maxWidth) {
	    ArrayList<String> lines = new ArrayList<String>();
	    String line = "";
	    if (text.length() < maxWidth) {
	    	lines.add(text);
	        return lines;
	    }
	    
	    String[] words = text.split(" ");
	    boolean isAdded = false;
	    for (String word : words) {
	    	String txt = line + word + " ";
	    	int len = txt.length();
	    	if(len >= maxWidth) {
	    		isAdded = true;
	    		lines.add(line);
	    		line = word + " ";
	    		txt = "";
	    	} else {
	    		isAdded = false;
	    		line = txt;
	    	}
		}
	    if(!isAdded) {
	    	lines.add(line);
	    }
	    
	    return lines;
	}

	private void playSound() {
		try {
		    AudioFormat format;
		    DataLine.Info info;
		    Clip clip;

		    if(stream != null) {
			    format = stream.getFormat();
			    info = new DataLine.Info(Clip.class, format);
			    clip = (Clip) AudioSystem.getLine(info);
			    clip.open(stream);
			    clip.start();
		    }
		}
		catch (Exception e) { /** eating exception */ }
	}
	
	/**
	 * Stop playback and thread
	 */
	public void stopPlayback() {
		if(readerThread.isAlive()) {
			readerThread.stop();
			if(getPlaySound()!= null && getPlaySound().getClip() != null) {
				getPlaySound().stopClip();
			}
			
		}
	}
	
	/**
	 * @return the isDataReadyForRead
	 */
	public boolean isDataReadyForRead() {
		return isDataReadyForRead;
	}

	/**
	 * @param isDataReadyForRead the isDataReadyForRead to set
	 */
	public void setDataReadyForRead(boolean isDataReadyForRead) {
		this.isDataReadyForRead = isDataReadyForRead;
	}

	/**
	 * @param dataToRead the dataToRead to set
	 */
	public void setDataToRead(String dataToRead) {
		this.dataToRead = dataToRead;
	}

	/**
	 * @param instructions the instructions to set
	 */
	public void setInstructions(
			List<String> instructions) {
		this.instructions = instructions;
	}

	/**
	 * @param tPanel the tPanel to set
	 */
	public void settPanel(TextPanel tPanel) {
		this.tPanel = tPanel;
	}

	/**
	 * @return the panel
	 */
	public AbacusPanel getPanel() {
		return panel;
	}

	/**
	 * @param panel the panel to set
	 */
	public void setPanel(AbacusPanel panel) {
		this.panel = panel;
	}

	/**
	 * @return the voiceSelected
	 */
	public String getVoiceSelected() {
		return voiceSelected;
	}

	/**
	 * @param voiceSelected the voiceSelected to set
	 */
	public void setVoiceSelected(String voiceSelected) {
		this.voiceSelected = voiceSelected;
	}

	/**
	 * @return the playSound
	 */
	public SpeechConnector getPlaySound() {
		return playSound;
	}

	/**
	 * @param playSound the playSound to set
	 */
	public void setPlaySound(SpeechConnector playSound) {
		this.playSound = playSound;
	}

	/**
	 * @param isPlayRobotics the isPlayRobotics to set
	 */
	public void setPlayRobotics(boolean isPlayRobotics) {
		this.isPlayRobotics = isPlayRobotics;
	}

	/**
	 * @param isPlayNatural the isPlayNatural to set
	 */
	public void setPlayNatural(boolean isPlayNatural) {
		this.isPlayNatural = isPlayNatural;
	}
}
