/**
 * 
 */
package com.app.abacus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.app.abacus.Beads;

/**
 * Class responsible for generating the Abacus
 * 
 * @author prashant.joshi (198joshi@gmail.com)
 * @version 12-May-2016
 */
public class AbacusPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	Beads[][] beads = new Beads[17][5];
	private static final Color BEADS_COLOR = Color.CYAN;
	private static final Color BEADS_SWITCH_COLOR = Color.RED;
	private static final Color BEADS_VALUE_COLOR = Color.CYAN;
	private static final Color MARKER_COLOR = Color.WHITE;
	private String earthBeadImgPath = "/com/app/images/earthdown.jpg";
	private String heavenBeadImgPath = "/com/app/images/earthup.jpg";
	private String beadDividerImgPath = "/com/app/images/wood.jpg";
	private String abacusFrameImgPath = "/com/app/images/wooden-beaded-frame.jpg";
	private String abacusHFrameImgPath = "/com/app/images/wodden-frame-1414550.jpg";
	
	private String abacusRightThumbFingerImgPath = "/com/app/images/rightThumbFinger.png";
	private String abacusRightIndexFingerImgPath = "/com/app/images/rightIndexFinger.png";
	private String abacusLeftThumbFingerImgPath = "/com/app/images/leftThumbFinger.png";
	private String abacusLeftIndexFingerImgPath = "/com/app/images/leftIndexFinger.png";
	
	private boolean isHighLightFrame;
	private boolean isHighLightRods;
	private boolean isHighLightBeam;
	private boolean isHighLightDots;
	
	private boolean isDisplayTotal;
	private String total;
	
	private String[] beadValueArray = {" 10G","  1G", "100T"," 10T","  1T", "100B", " 10B", "  1B", "100M", " 10M", "  1M", "100K"," 10K","1000"," 100"," 10 ","  1 "};
	
	BufferedImage earthBeadImage = null;
	BufferedImage heavenBeadImage = null;
	BufferedImage beadDivider = null;
	BufferedImage abacusFrame = null;
	
	BufferedImage abacusRightThumbFinger = null;
	BufferedImage abacusRightIndexFinger = null;
	BufferedImage abacusLeftThumbFinger = null;
	BufferedImage abacusLeftIndexFinger = null;

	public AbacusPanel() {
		try {
			/** Setting up Abacus images */
			earthBeadImage = ImageIO.read(this.getClass().getResourceAsStream(earthBeadImgPath));
			heavenBeadImage = ImageIO.read(this.getClass().getResourceAsStream(heavenBeadImgPath));
			beadDivider = ImageIO.read(this.getClass().getResourceAsStream(beadDividerImgPath));
			abacusFrame = ImageIO.read(this.getClass().getResourceAsStream(abacusHFrameImgPath));
			
			abacusRightThumbFinger = ImageIO.read(this.getClass().getResourceAsStream(abacusRightThumbFingerImgPath));
			abacusRightIndexFinger = ImageIO.read(this.getClass().getResourceAsStream(abacusRightIndexFingerImgPath));
			abacusLeftThumbFinger = ImageIO.read(this.getClass().getResourceAsStream(abacusLeftThumbFingerImgPath));
			abacusLeftIndexFinger = ImageIO.read(this.getClass().getResourceAsStream(abacusLeftIndexFingerImgPath));
			
			createBeads();
			
			/** Setting up highlighters */
			isHighLightFrame = false;
			isHighLightRods = false;
			isHighLightDots = false;
			isHighLightBeam = false;
			
			this.setLayout(null);
			} catch (IOException e) { /** eating exception */ }
		
	}
	
	public void refreshPanel() {
		/** Setting up highlighters */
		isHighLightFrame = false;
		isHighLightRods = false;
		isHighLightDots = false;
		
		resetBeads();
		
		this.repaint();
		
	}
	
	public void resetBeads() {
		for(int i = 0; i < 17; i++) {
			for (int j = 0; j < 5; j++) {
				beads[i][j].setColor(AbacusPanel.BEADS_COLOR);
				beads[i][j].setSwitchable(false);
				beads[i][j].setHighlighter(false);
				beads[i][j].setsShowFinger(false);
				beads[i][j].setShowFinger(false);
			}
		}
	}
	
	public void setupBeadsBefore(Integer[] rep) {
		resetBeads();
		int size = 16;
		for(int i = rep.length - 1; i >= 0; i--) {
			int val = rep[i] - 1;
			if(val == 5) {
				beads[size][4].setShowFinger(true);
			} else if(val < 5) {
				int counter = 3;
				for(int j = val - 1; j >= 0; j--) {
					beads[size][counter--].setShowFinger(true);
				}
			} else {
				beads[size][4].setShowFinger(true);
				int rest = val - 5;
				int counter = 3;
				for(int j = rest - 1; j >= 0; j--) {
					beads[size][counter--].setShowFinger(true);
				}
			}
			size--;
		}
	}
	
	public void setupBeadsAfter(Integer[] rep) {
		resetBeads();
		int size = 16;
		for(int i = rep.length - 1; i >= 0; i--) {
			int val = rep[i] - 1;
			if(val == 5) {
				beads[size][4].setColor(AbacusPanel.BEADS_SWITCH_COLOR);
				beads[size][4].setSwitchable(true);
			} else if(val < 5) {
				int counter = 3;
				for(int j = val - 1; j >= 0; j--) {
					beads[size][counter].setColor(AbacusPanel.BEADS_SWITCH_COLOR);
					beads[size][counter--].setSwitchable(true);
				}
			} else {
				beads[size][4].setColor(AbacusPanel.BEADS_SWITCH_COLOR);
				beads[size][4].setSwitchable(true);
				int rest = val - 5;
				int counter = 3;
				for(int j = rest - 1; j >= 0; j--) {
					beads[size][counter].setColor(AbacusPanel.BEADS_SWITCH_COLOR);
					beads[size][counter--].setSwitchable(true);
				}
			}
			size--;
		}
	}
	
	public void resetAndSetBeadFingerImage(Beads bead, String parseActionFinger) {
		bead.setRightIndex(false);
		bead.setRightThumb(false);
		bead.setLeftIndex(false);
		bead.setLeftThumb(false);
		
		if(parseActionFinger.equalsIgnoreCase("uselindex")) {
			bead.setLeftIndex(true);
		} else if(parseActionFinger.equalsIgnoreCase("uselthumb")) {
			bead.setLeftThumb(true);
		} else if(parseActionFinger.equalsIgnoreCase("userindex")) {
			bead.setRightIndex(true);
		} else if(parseActionFinger.equalsIgnoreCase("userthumb")) {
			bead.setRightThumb(true);
		}
	}
	
	public void setupAddBeadsBefore(int rodNum, int beadNumber, String parseActionFinger) {
		//resetBeads();
		int size = 18 - rodNum;
		if(beadNumber == 5) {
			beads[size-1][4].setShowFinger(true);
			beads[size-1][4].setsShowFinger(false);
			resetAndSetBeadFingerImage(beads[size-1][4], parseActionFinger);
		} else {
			beads[size-1][beadNumber - 1].setShowFinger(true);
			beads[size-1][beadNumber - 1].setsShowFinger(false);
			resetAndSetBeadFingerImage(beads[size-1][beadNumber - 1], parseActionFinger);
		}
	}
	
	public void setupAddBeadsAfter(int rodNum, int beadNumber, String parseActionFinger) {
		//resetBeads();
		int size = 18 - rodNum;
		if(beadNumber == 5) {
			beads[size-1][4].setShowFinger(false);
			beads[size-1][4].setsShowFinger(true);
			beads[size-1][4].setSwitchable(true);
			resetAndSetBeadFingerImage(beads[size-1][4], parseActionFinger);
		} else {
			beads[size-1][beadNumber - 1].setShowFinger(false);
			beads[size-1][beadNumber - 1].setsShowFinger(true);
			beads[size-1][beadNumber - 1].setSwitchable(true);
			resetAndSetBeadFingerImage(beads[size-1][beadNumber - 1], parseActionFinger);
		}
	}
	
	public void setupAddBeads(int rodNum, int beadNumber) {
		//resetBeads();
		int size = 18 - rodNum;
		if(beadNumber == 5) {
			beads[size-1][4].setShowFinger(false);
			beads[size-1][4].setsShowFinger(false);
			beads[size-1][4].setSwitchable(true);
		} else {
			beads[size-1][beadNumber - 1].setShowFinger(false);
			beads[size-1][beadNumber - 1].setsShowFinger(false);
			beads[size-1][beadNumber - 1].setSwitchable(true);
		}
	}
	
	public void setupMinusBeadsBefore(int rodNum, int beadNumber, String parseActionFinger) {
		//resetBeads();
		int size = 18 - rodNum;
		if(beadNumber == 5) {
			beads[size-1][4].setsShowFinger(true);
			beads[size-1][4].setShowFinger(false);
			resetAndSetBeadFingerImage(beads[size-1][4], parseActionFinger);
		} else {
			beads[size-1][beadNumber - 1].setsShowFinger(true);
			beads[size-1][beadNumber - 1].setShowFinger(false);
			resetAndSetBeadFingerImage(beads[size-1][beadNumber - 1], parseActionFinger);
		}
	}
	
	public void setupMinusBeadsAfter(int rodNum, int beadNumber, String parseActionFinger) {
		//resetBeads();
		int size = 18 - rodNum;
		if(beadNumber == 5) {
			beads[size-1][4].setsShowFinger(false);
			beads[size-1][4].setShowFinger(true);
			beads[size-1][4].setSwitchable(false);
			resetAndSetBeadFingerImage(beads[size-1][4], parseActionFinger);
		} else {
			beads[size-1][beadNumber - 1].setsShowFinger(false);
			beads[size-1][beadNumber - 1].setShowFinger(true);
			beads[size-1][beadNumber - 1].setSwitchable(false);
			resetAndSetBeadFingerImage(beads[size-1][beadNumber - 1], parseActionFinger);
		}
	}
	
	public void setupMinusBeads(int rodNum, int beadNumber) {
		//resetBeads();
		int size = 18 - rodNum;
		if(beadNumber == 5) {
			beads[size-1][4].setsShowFinger(false);
			beads[size-1][4].setShowFinger(false);
			beads[size-1][4].setSwitchable(false);
		} else {
			beads[size-1][beadNumber - 1].setsShowFinger(false);
			beads[size-1][beadNumber - 1].setShowFinger(false);
			beads[size-1][beadNumber - 1].setSwitchable(false);
		}
	}
	
	public void displayTotal(String total) {
		isDisplayTotal = true;
		this.total = total;
	}
	
	/**
	 * Setting up marker data
	 */
	private void setupMarkerData(Graphics g) {
		int counterX = 110;
		for(int i=1; i < 17; i++) {
			if(i % 3 == 0) {
				g.setColor(AbacusPanel.MARKER_COLOR);
				g.fillOval(counterX, 145, 10, 10);
			}
			counterX += 50;
		}
	}
	
	/**
	 * Setup Beads Data
	 */
	private void createBeads() {
		int ePosX = 75;
		int hPosX = 75;
		int hPosy = 42;
		
		for (int j = 0; j < 17; j++) {
			int ePosY = 305;
			int sY = 155;
			for(int i = 0; i < 4; i++) {
				beads[j][i] = new Beads();
				beads[j][i].setPosX(ePosX);
				beads[j][i].setPosY(ePosY);
				beads[j][i].setsPosY(sY);
				
				beads[j][i].setPosFingerX(ePosX+20);
				beads[j][i].setPosFingerY(ePosY+10);
				beads[j][i].setsPosFingerY(sY+10);
				
				beads[j][i].setRightThumb(true);
				beads[j][i].setRightIndex(false);
				beads[j][i].setLeftThumb(false);
				beads[j][i].setLeftIndex(false);
				
				beads[j][i].setAbacusRightThumbFingerImgPath(abacusRightThumbFinger);
				beads[j][i].setAbacusRightIndexFingerImgPath(abacusRightIndexFinger);
				beads[j][i].setAbacusLeftThumbFingerImgPath(abacusLeftThumbFinger);
				beads[j][i].setAbacusLeftIndexFingerImgPath(abacusLeftIndexFinger);
				
				beads[j][i].setValue(100000000);
				beads[j][i].setSwitchable(false);
				beads[j][i].setColor(AbacusPanel.BEADS_COLOR);
				beads[j][i].setImg(earthBeadImage);
				beads[j][i].setSwitchImg(heavenBeadImage);
				beads[j][i].setHighlighter(false);
				ePosY = ePosY + 40;
				sY = sY + 40;
			}
			
			beads[j][4] = new Beads();
			beads[j][4].setPosX(hPosX);
			beads[j][4].setPosY(hPosy);
			beads[j][4].setsPosY(100);
			
			beads[j][4].setPosFingerX(hPosX+20);
			beads[j][4].setPosFingerY(hPosy+10);
			beads[j][4].setsPosFingerY(110);
			
			beads[j][4].setRightThumb(true);
			beads[j][4].setRightIndex(false);
			beads[j][4].setLeftThumb(false);
			beads[j][4].setLeftIndex(false);
			
			beads[j][4].setAbacusRightThumbFingerImgPath(abacusRightThumbFinger);
			beads[j][4].setAbacusRightIndexFingerImgPath(abacusRightIndexFinger);
			beads[j][4].setAbacusLeftThumbFingerImgPath(abacusLeftThumbFinger);
			beads[j][4].setAbacusLeftIndexFingerImgPath(abacusLeftIndexFinger);
			
			beads[j][4].setValue(500000000);
			beads[j][4].setSwitchable(false);
			beads[j][4].setColor(AbacusPanel.BEADS_COLOR);
			beads[j][4].setImg(earthBeadImage);
			beads[j][4].setSwitchImg(heavenBeadImage);
			beads[j][4].setHighlighter(false);
			
			hPosX = hPosX + 50;
			ePosX = ePosX + 50;
		}
	} 
	
	/**
	 * Draw Abacus Board
	 */
	private void drawAbacus(Graphics g) {
		//Draw Poles
		int counterX = 110;
		for(int i=0;i<17;i++) {
			g.drawImage(beadDivider,counterX, 42, 10, 425, null);
			counterX = counterX + 50;
		}
		
		//Draw Divider
		//g.drawImage(beadDivider, 70, 140, this.getWidth() - 135, 20, null);
		g.drawImage(beadDivider, 60, 140, this.getWidth() - 125, 20, null);
		
		//Draw Marker
		setupMarkerData(g);
		
		/** Draw beads value */
		counterX = 100;
		for (String bead : beadValueArray) {
			g.setColor(AbacusPanel.BEADS_VALUE_COLOR);
			g.drawString(bead, counterX, 495);
			counterX = counterX + 50;
		}
		
		//DrawBeads
		for (int i = 0; i < 17; i++) {
			for (int j = 0; j < 5; j++) {
				beads[i][j].drawBeats(g);
			}
		}
		
		
	}
	
	/**
	 * Method used to highlight abacus frame
	 */
	public void highlightFrame() {
		isHighLightFrame = true;
		this.repaint();
	}
	
	/**
	 * Method used to highlight abacus rods
	 */
	public void highlightRods() {
		isHighLightRods = true;
		this.repaint();
	}
	
	/**
	 * Method used to highlight abacus rods
	 */
	public void highlightBeam() {
		isHighLightBeam = true;
		this.repaint();
	}
	

	
	/**
	 * Method used to highlight abacus beads
	 */
	public void highlightDots() {
		isHighLightDots = true;
		this.repaint();
	}
	
	/**
	 * Method used to highlight abacus beads
	 */
	public void highlightLowerBeads(boolean highlight) {
		if(highlight) {
			for (int i = 0; i < 17; i++) {
				for (int j = 0; j < 4; j++) {
					beads[i][j].setHighlighter(true);
				}
			}
		} else {
			for (int i = 0; i < 17; i++) {
				for (int j = 0; j < 4; j++) {
					beads[i][j].setHighlighter(false);
				}
			}
		}
		this.repaint();
	}
	
	/**
	 * Method used to highlight abacus beads
	 */
	public void highlightUpperBeads(boolean highlight) {
		if(highlight) {
			for (int i = 0; i < 17; i++) {
				beads[i][4].setHighlighter(true);
			}
		} else {
			for (int i = 0; i < 17; i++) {
				beads[i][4].setHighlighter(false);
			}
		}
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		/** Draw Abacus Borad */
		//g.drawImage(abacusFrame,-50,-30, this.getWidth() + 95, this.getHeight() - 105, null);
		g.drawImage(abacusFrame,0,5, this.getWidth(), this.getHeight() - 165, null);
		
		if(isDisplayTotal) {
			g.setColor(Color.WHITE);
			g.drawString("Total : " + total, this.getWidth() - 150, 20);
			g.setColor(Color.BLACK);
		}
		
		/** Draw Abacus */
		drawAbacus(g);
		
		if(isHighLightFrame) {
			g.setColor(Color.CYAN);
			g.fillRect(10, 10, 20, 490);
			g.fillRect(990, 10, 20, 490);
			
			g.fillRect(10, 10, 990, 20);
			g.fillRect(10, 480, 990, 20);
		} else if(isHighLightRods) {
			int counterX = 110;
			for(int i=0;i<17;i++) {
				g.setColor(Color.CYAN);
				g.fillRect(counterX, 40, 10, 425);
				counterX = counterX + 50;
			}
		} else if(isHighLightBeam) {
			g.setColor(Color.CYAN);
			//g.drawImage(beadDivider, 60, 140, this.getWidth() - 125, 20, null);
			g.fillRect(60, 140, this.getWidth() - 125, 20);
		} else if(isHighLightDots) {
			int counterX = 110;
			for(int i=1; i < 17; i++) {
				if(i % 3 == 0) {
					g.setColor(Color.CYAN);
					g.fillOval(counterX, 145, 10, 10);
				}
				counterX += 50;
			}
		}
	}
	
	private void waitForSeconds(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {/** Eating Exception */}
	}

	/**
	 * @param isHighLightFrame the isHighLightFrame to set
	 */
	public void setHighLightFrame(boolean isHighLightFrame) {
		this.isHighLightFrame = isHighLightFrame;
	}

	/**
	 * @param isHighLightRods the isHighLightRods to set
	 */
	public void setHighLightRods(boolean isHighLightRods) {
		this.isHighLightRods = isHighLightRods;
	}

	/**
	 * @param isHighLightDots the isHighLightDots to set
	 */
	public void setHighLightDots(boolean isHighLightDots) {
		this.isHighLightDots = isHighLightDots;
	}

	/**
	 * @param isHighLightBeam the isHighLightBeam to set
	 */
	public void setHighLightBeam(boolean isHighLightBeam) {
		this.isHighLightBeam = isHighLightBeam;
	}
	
	
}
