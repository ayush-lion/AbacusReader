/**
 * 
 */
package com.app.abacus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Its a bean holding the individual bead information
 * 
 * @author prashant.joshi (198joshi@gmail.com)
 * @version 12-May-2016
 */
public class Beads {

	private long value;
	private int posX;
	private int posY;
	private int sPosY;
	private boolean switchable;
	private boolean highlighter;
	private boolean Up;
	private Color color;
	private boolean showFinger;
	private boolean sShowFinger;
	private int posFingerX;
	private int posFingerY;
	private int sPosFingerY;
	private BufferedImage img = null;
	private BufferedImage switchImg = null;
	
	private BufferedImage abacusRightThumbFingerImgPath = null;
	private BufferedImage abacusRightIndexFingerImgPath = null;
	private BufferedImage abacusLeftThumbFingerImgPath = null;
	private BufferedImage abacusLeftIndexFingerImgPath = null;
	
	private boolean isLeftIndex;
	private boolean isLeftThumb;
	private boolean isRightIndex;
	private boolean isRightThumb;
	
	public void drawBeats(Graphics g) {
		if(!isHighlighter()) {
			if(isSwitchable()) {
				g.drawImage(switchImg, posX+20, sPosY, 40, 40, null);
				if(issShowFinger()) {
					System.out.println("ShowSwitchFinger " + isRightIndex + " : " + isRightThumb + " : " + isLeftIndex + " : " + isLeftThumb);
					if(isRightThumb) {
						g.drawImage(abacusRightThumbFingerImgPath, posFingerX+20, sPosFingerY, 40, 40, null);
					} else if(isRightIndex) {
						g.drawImage(abacusRightIndexFingerImgPath, posFingerX+20, sPosFingerY, 40, 40, null);
					} else if(isLeftThumb) {
						g.drawImage(abacusLeftThumbFingerImgPath, posFingerX-20, sPosFingerY, 40, 40, null);
					} else if(isLeftIndex) {
						g.drawImage(abacusLeftIndexFingerImgPath, posFingerX-20, sPosFingerY, 40, 40, null);
					} else {
						g.drawImage(abacusRightThumbFingerImgPath, posFingerX+20, sPosFingerY, 40, 40, null);
					}
				}
			} else {
				g.drawImage(img, posX+20, posY, 40, 40, null);
				if(isShowFinger()) {
					System.out.println("ShowFinger " + isRightIndex + " : " + isRightThumb + " : " + isLeftIndex + " : " + isLeftThumb);
					if(isRightThumb) {
						g.drawImage(abacusRightThumbFingerImgPath, posFingerX+20, posFingerY, 40, 40, null);
					} else if(isRightIndex) {
						g.drawImage(abacusRightIndexFingerImgPath, posFingerX+20, posFingerY, 40, 40, null);
					} else if(isLeftThumb) {
						g.drawImage(abacusLeftThumbFingerImgPath, posFingerX-20, posFingerY, 40, 40, null);
					} else if(isLeftIndex) {
						g.drawImage(abacusLeftIndexFingerImgPath, posFingerX-20, posFingerY, 40, 40, null);
					} else {
						g.drawImage(abacusRightThumbFingerImgPath, posFingerX+20, posFingerY, 40, 40, null);
					}
				}
			}
		} else {
			g.setColor(Color.CYAN);
			g.fillOval(posX+20, posY, 40, 40);
		}
		
	}
	
	/**
	 * @return the value
	 */
	public long getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(long value) {
		this.value = value;
	}
	/**
	 * @return the posX
	 */
	public int getPosX() {
		return posX;
	}
	/**
	 * @param posX the posX to set
	 */
	public void setPosX(int posX) {
		this.posX = posX;
	}
	/**
	 * @return the posY
	 */
	public int getPosY() {
		return posY;
	}
	/**
	 * @param posY the posY to set
	 */
	public void setPosY(int posY) {
		this.posY = posY;
	}
	/**
	 * @return the up
	 */
	public boolean isUp() {
		return Up;
	}
	/**
	 * @param up the up to set
	 */
	public void setUp(boolean up) {
		Up = up;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @return the sPosY
	 */
	public int getsPosY() {
		return sPosY;
	}

	/**
	 * @param sPosY the sPosY to set
	 */
	public void setsPosY(int sPosY) {
		this.sPosY = sPosY;
	}

	/**
	 * @return the switchable
	 */
	public boolean isSwitchable() {
		return switchable;
	}

	/**
	 * @param switchable the switchable to set
	 */
	public void setSwitchable(boolean switchable) {
		this.switchable = switchable;
	}

	/**
	 * @return the img
	 */
	public BufferedImage getImg() {
		return img;
	}

	/**
	 * @param img the img to set
	 */
	public void setImg(BufferedImage img) {
		this.img = img;
	}

	/**
	 * @return the highlighter
	 */
	public boolean isHighlighter() {
		return highlighter;
	}

	/**
	 * @param highlighter the highlighter to set
	 */
	public void setHighlighter(boolean highlighter) {
		this.highlighter = highlighter;
	}

	/**
	 * @return the switchImg
	 */
	public BufferedImage getSwitchImg() {
		return switchImg;
	}

	/**
	 * @param switchImg the switchImg to set
	 */
	public void setSwitchImg(BufferedImage switchImg) {
		this.switchImg = switchImg;
	}

	/**
	 * @return the showFinger
	 */
	public boolean isShowFinger() {
		return showFinger;
	}

	/**
	 * @param showFinger the showFinger to set
	 */
	public void setShowFinger(boolean showFinger) {
		this.showFinger = showFinger;
	}

	/**
	 * @return the posFingerX
	 */
	public int getPosFingerX() {
		return posFingerX;
	}

	/**
	 * @param posFingerX the posFingerX to set
	 */
	public void setPosFingerX(int posFingerX) {
		this.posFingerX = posFingerX;
	}

	/**
	 * @return the posFingerY
	 */
	public int getPosFingerY() {
		return posFingerY;
	}

	/**
	 * @param posFingerY the posFingerY to set
	 */
	public void setPosFingerY(int posFingerY) {
		this.posFingerY = posFingerY;
	}

	/**
	 * @return the sPosFingerY
	 */
	public int getsPosFingerY() {
		return sPosFingerY;
	}

	/**
	 * @param sPosFingerY the sPosFingerY to set
	 */
	public void setsPosFingerY(int sPosFingerY) {
		this.sPosFingerY = sPosFingerY;
	}

	/**
	 * @return the sShowFinger
	 */
	public boolean issShowFinger() {
		return sShowFinger;
	}

	/**
	 * @param sShowFinger the sShowFinger to set
	 */
	public void setsShowFinger(boolean sShowFinger) {
		this.sShowFinger = sShowFinger;
	}

	/**
	 * @return the isLeftIndex
	 */
	public boolean isLeftIndex() {
		return isLeftIndex;
	}

	/**
	 * @param isLeftIndex the isLeftIndex to set
	 */
	public void setLeftIndex(boolean isLeftIndex) {
		this.isLeftIndex = isLeftIndex;
	}

	/**
	 * @return the isLeftThumb
	 */
	public boolean isLeftThumb() {
		return isLeftThumb;
	}

	/**
	 * @param isLeftThumb the isLeftThumb to set
	 */
	public void setLeftThumb(boolean isLeftThumb) {
		this.isLeftThumb = isLeftThumb;
	}

	/**
	 * @return the isRightIndex
	 */
	public boolean isRightIndex() {
		return isRightIndex;
	}

	/**
	 * @param isRightIndex the isRightIndex to set
	 */
	public void setRightIndex(boolean isRightIndex) {
		this.isRightIndex = isRightIndex;
	}

	/**
	 * @return the isRightThumb
	 */
	public boolean isRightThumb() {
		return isRightThumb;
	}

	/**
	 * @param isRightThumb the isRightThumb to set
	 */
	public void setRightThumb(boolean isRightThumb) {
		this.isRightThumb = isRightThumb;
	}

	/**
	 * @return the abacusRightThumbFingerImgPath
	 */
	public BufferedImage getAbacusRightThumbFingerImgPath() {
		return abacusRightThumbFingerImgPath;
	}

	/**
	 * @param abacusRightThumbFingerImgPath the abacusRightThumbFingerImgPath to set
	 */
	public void setAbacusRightThumbFingerImgPath(
			BufferedImage abacusRightThumbFingerImgPath) {
		this.abacusRightThumbFingerImgPath = abacusRightThumbFingerImgPath;
	}

	/**
	 * @return the abacusRightIndexFingerImgPath
	 */
	public BufferedImage getAbacusRightIndexFingerImgPath() {
		return abacusRightIndexFingerImgPath;
	}

	/**
	 * @param abacusRightIndexFingerImgPath the abacusRightIndexFingerImgPath to set
	 */
	public void setAbacusRightIndexFingerImgPath(
			BufferedImage abacusRightIndexFingerImgPath) {
		this.abacusRightIndexFingerImgPath = abacusRightIndexFingerImgPath;
	}

	/**
	 * @return the abacusLeftThumbFingerImgPath
	 */
	public BufferedImage getAbacusLeftThumbFingerImgPath() {
		return abacusLeftThumbFingerImgPath;
	}

	/**
	 * @param abacusLeftThumbFingerImgPath the abacusLeftThumbFingerImgPath to set
	 */
	public void setAbacusLeftThumbFingerImgPath(
			BufferedImage abacusLeftThumbFingerImgPath) {
		this.abacusLeftThumbFingerImgPath = abacusLeftThumbFingerImgPath;
	}

	/**
	 * @return the abacusLeftIndexFingerImgPath
	 */
	public BufferedImage getAbacusLeftIndexFingerImgPath() {
		return abacusLeftIndexFingerImgPath;
	}

	/**
	 * @param abacusLeftIndexFingerImgPath the abacusLeftIndexFingerImgPath to set
	 */
	public void setAbacusLeftIndexFingerImgPath(
			BufferedImage abacusLeftIndexFingerImgPath) {
		this.abacusLeftIndexFingerImgPath = abacusLeftIndexFingerImgPath;
	}
}
