/**
 * 
 */
package com.app.abacus;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Class is responsible for display the instruction text
 * 
 * @author prashant.joshi (198joshi@gmail.com)
 * @version 12-May-2016
 */
public class TextPanel extends JPanel {
	
	ArrayList<String> txtToPrint = new ArrayList<String>();
	Font txtFont = null;
	BufferedImage img = null;
	JEditorPane editorPane = new JEditorPane();
	JScrollPane scroller = null;
	
	public TextPanel() {
		txtFont = new Font( "Arial", Font.BOLD, 18 );
		
		this.setLayout(null);
		
		try {
			img = ImageIO.read(this.getClass().getResourceAsStream("/com/app/images/wodden-frame.jpg"));
		} catch (IOException e) { /** eating exception */ }
		
		
		editorPane.setEditable(false);
		editorPane.setContentType("text/html");
		editorPane.setBounds(20, 20, 980, 60);
		scroller = new JScrollPane(editorPane);
		scroller.setBorder(null);
		this.add(editorPane);
	}
	
	public void drawText(ArrayList<String> txt) {
		txtToPrint = txt;
		String showTxt = "";
		if(!txtToPrint.isEmpty()) {
			for (String line : txtToPrint) {
				showTxt = showTxt + line;
			}
			
		}
		editorPane.setText(showTxt);
		//this.repaint();
	}
	
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(img, 0, 10, this.getWidth(), this.getHeight() - 20, null);
		editorPane.setText("Instructions Text...");
	}
	
	public static void main(String[] args) {
		new TextPanel();
	}

}