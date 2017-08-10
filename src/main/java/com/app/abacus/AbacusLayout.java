/**
 * 
 */
package com.app.abacus;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Class used to create and handle GUI operations for Abacus
 * 
 * @author prashant.joshi (198joshi@gmail.com)
 * @version 12-May-2016
 */
public class AbacusLayout extends JFrame {

	private static final long serialVersionUID = 1L;
	
	/** Menubar options */
	private JMenuBar menuBar;
	private JMenu menu;
	private JCheckBoxMenuItem robotics;
	private JMenu natural;
	private JCheckBoxMenuItem Sharon;
	private JCheckBoxMenuItem Rachel;
	private JCheckBoxMenuItem Deepa;
	
	
	private JTextField insSheet;
	private JButton fileButton;
	private JButton startButton;
	private JButton killButton;
	private InstructionReader reader;

	private JTextArea ins;
	private AbacusPanel panel;
	private TextPanel tPanel;
	private AbacusInstruction abacusMap;
	
	//private	JTabbedPane tabbedPane;
	private	JPanel		voicePanel1;
	
	private ButtonGroup vGroup = null;
	private JRadioButton voice1 = new JRadioButton("Sharon", true);
    private JRadioButton voice2 = new JRadioButton("Rachel");
    private JRadioButton voice3 = new JRadioButton("Deepa");
	
	int counter;
	ArrayList<Integer[]> resultSet;
	ArrayList<String> listOfActions = null;
	
	private boolean isPlayRobotics;
	private boolean isPlayNatural;
	
	DownloadSoundDataFiles dSndFiles;

	@Override
	protected void finalize() throws Throwable {
		if(reader != null) {
			reader.stopPlayback();
		}
	}
	
	public AbacusLayout() {
		this.setLayout(null);
		this.setResizable(false);
		this.setTitle("Abacus. Lets start learning mind math !!!");
		this.setBounds(100, 100, 1050, 720);
		this.setBackground(Color.WHITE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/** Setting up speaker */
		isPlayRobotics = true;
		isPlayNatural = false;
		
		/** Setting up MenuBar */
		setupMenuBar();

		/** Setting up text field to display instruction file path */
		insSheet = new JTextField();
		insSheet.setText("Click load intruction sheet button to the instruction file");
		insSheet.setEnabled(false);
		insSheet.setBounds(10, 10, 600, 40);

		/** Setting up button to load instruction file */
		fileButton = new JButton("Load Instruction Sheet");
		fileButton.setBounds(610, 10, 215, 40);

		fileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel FILES", "xlsx", "Excel");
				jFileChooser.setFileFilter(filter);
				int result = jFileChooser.showOpenDialog(new JFrame());
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jFileChooser.getSelectedFile();
					insSheet.setText(selectedFile.getAbsolutePath());
					
					abacusMap = new AbacusInstruction();
					abacusMap.setFileName(insSheet.getText());
					try {
						abacusMap.mapInstructionsAndActionsInList();
	
						List<String> instructions = abacusMap
								.getListOfInsAndMap();
						
						if(Sharon.isSelected() || Rachel.isSelected() || Deepa.isSelected()) {
							String voice = "sharon22k";
							if(Rachel.isSelected()) {
								voice  = "rachel22k";
							} else if(Deepa.isSelected()) {
								voice = "deepa22k";
							}
							System.out.println(voice);
							dSndFiles = new DownloadSoundDataFiles();
							dSndFiles.setSelectedVoice(voice);
							dSndFiles.setListOfInstructions(instructions);
							dSndFiles.loadInstructions();
							//dSndFiles.downloadSndText();
						}
						
						startButton.setEnabled(true);
						//dSndFiles.printInstructions();
					} catch(IOException ioe) {}
				}
			}
		});

		/** Setting up button to start demonstration */
		startButton = new JButton("Start Demo");
		startButton.setBounds(825, 10, 120, 40);
		startButton.setEnabled(false);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Read Instructions and start demonstration
				try {
					startButton.setEnabled(false);
					
					/** Disabling the instruction file load */
					fileButton.setEnabled(false);
					
					/** Disabling the voice selection pane */
					if(isPlayNatural) {
						voice1.setEnabled(false);
						voice2.setEnabled(false);
						voice3.setEnabled(false);
					}
					
					abacusMap = new AbacusInstruction();
					abacusMap.setFileName(insSheet.getText());
					abacusMap.mapInstructionsAndActionsInList();

					List<String> instructions = abacusMap
							.getListOfInsAndMap();

					
					reader.setInstructions(instructions);
					reader.setDataToRead(ins.getText());
					reader.setDataReadyForRead(true);
					reader.setPlayNatural(isPlayNatural);
					reader.setPlayRobotics(isPlayRobotics);
					reader.startReading();
					
				} catch (IOException e1) {
					JOptionPane
							.showMessageDialog(
									null,
									"Unable to load instructions from file. Please verify the instruction file.",
									"Abacus Instructions",
									JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
			}
		});
		
		/** Setting up the button and action to kill the current Demo */
		killButton = new JButton("Kill Demo");
		killButton.setBounds(945, 10, 90, 40);
		killButton.setEnabled(false);
		killButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.refreshPanel();
				reader.stopPlayback();
				fileButton.setEnabled(true);
				startButton.setEnabled(true);
				
				if(isPlayNatural) {
					voice1.setEnabled(true);
					voice2.setEnabled(true);
					voice3.setEnabled(true);
				}
				
				startButton.setEnabled(false);
			}
		});
		
		/** Setting up voices */
		//setupVoices();
		
		ins = new JTextArea();
		ins.setBounds(7, 70, 1030, 50);
		ins.setEditable(false);
		ins.setVisible(false);

		
		/** Setting up text panel */
		tPanel = new TextPanel();
		tPanel.setBounds(10, 50, 1025, 99);

		/** Setting up abacus panel */
		panel = new AbacusPanel();
		panel.setBounds(10, 150, 1025, 670);
		
		/** Setting up thread class */
		reader = new InstructionReader();
		reader.settPanel(tPanel);
		reader.setPanel(panel);
		reader.setVoiceSelected("sharon22k");

		/** Adding all the components in frame */
		this.add(insSheet);
		this.add(fileButton);
		this.add(startButton);
		this.add(killButton);
		//this.add(voicePanel1);
		this.add(tPanel);
		this.add(ins);
		this.add(panel);
		
		this.setJMenuBar(menuBar);

		this.setVisible(true);

	}
	
	private void setupMenuBar() {
		menuBar = new JMenuBar();
		menu = new JMenu("Voices");
		menu.setMnemonic(KeyEvent.VK_V);
		menuBar.add(menu);
		
		natural = new JMenu("Natural");
		natural.setMnemonic(KeyEvent.VK_N);
		natural.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robotics.setSelected(false);
				isPlayRobotics = false;
				isPlayNatural = true;
				
				/** Disabling the voice selection pane */
				enableNaturalVoices(true);
				
				killButton.setEnabled(true);
			}
		});
		menu.add(natural);
		
		robotics = new JCheckBoxMenuItem("Robotics");
		robotics.setMnemonic(KeyEvent.VK_R);
		robotics.setSelected(true);
		robotics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				natural.setSelected(false);
				isPlayRobotics = true;
				isPlayNatural = false;
				
				/** Disabling the voice selection pane */
				enableNaturalVoices(false);
				
				killButton.setEnabled(false);
			}
		});
		menu.add(robotics);
		
		Sharon = new JCheckBoxMenuItem("Sharon");
		Sharon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robotics.setSelected(false);
				isPlayRobotics = false;
				isPlayNatural = true;
				
				/** Disabling the voice selection pane */
				Rachel.setSelected(false);
				Deepa.setSelected(false);
				
				
				killButton.setEnabled(true);
			}
		});
		natural.add(Sharon);
		
		Rachel = new JCheckBoxMenuItem("Rachel");
		Rachel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robotics.setSelected(false);
				isPlayRobotics = false;
				isPlayNatural = true;
				
				/** Disabling the voice selection pane */
				Sharon.setSelected(false);
				Deepa.setSelected(false);
				
				
				killButton.setEnabled(true);
			}
		});
		natural.add(Rachel);
		
		Deepa = new JCheckBoxMenuItem("Deepa");
		Deepa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				robotics.setSelected(false);
				isPlayRobotics = false;
				isPlayNatural = true;
				
				/** Disabling the voice selection pane */
				Sharon.setSelected(false);
				Rachel.setSelected(false);
				
				
				killButton.setEnabled(true);
			}
		});
		natural.add(Deepa);
	}
	
	private void enableNaturalVoices(boolean enable) {
		Sharon.setEnabled(enable);
		Rachel.setEnabled(enable);
		Deepa.setEnabled(enable);
		
		if(enable) {
			Sharon.setSelected(true);
			Rachel.setSelected(false);
			Deepa.setSelected(false);
		} else {
			Sharon.setSelected(false);
			Rachel.setSelected(false);
			Deepa.setSelected(false);
		}
	}
	
	private void setupVoices() {
		/** Seting up voices panel */
		//tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		//tabbedPane.setBounds(0, 40, 1045, 79);
		
		voicePanel1 = new JPanel();
		voicePanel1.setBounds(0, 40, 1045, 79);
		//voicePanel1.setLayout( new GridLayout(1, 3) );
		
		vGroup = new ButtonGroup();
		vGroup.add(voice1);
		voice1.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent event) {
		    	reader.setVoiceSelected("sharon22k");
		    }
		});
		
		vGroup.add(voice2);
		voice2.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent event) {
		    	reader.setVoiceSelected("rachel22k");
		    }
		});
		
		vGroup.add(voice3);
		voice3.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent event) {
		    	reader.setVoiceSelected("deepa22k");
		    }
		});
		
		//vGroup.getSelection().getActionCommand()
		
		voicePanel1.add(voice1);
		voicePanel1.add(voice2);
		voicePanel1.add(voice3);
		
		/** Dsiabling all the Natural voices disabled by default */
		voice1.setEnabled(false);
		voice2.setEnabled(false);
		voice3.setEnabled(false);
		//voicePanel1.setVisible(true);
		
		//tabbedPane.addTab( "Voice", voicePanel1 );
		//voicePanel1.setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AbacusLayout();
	}

}
