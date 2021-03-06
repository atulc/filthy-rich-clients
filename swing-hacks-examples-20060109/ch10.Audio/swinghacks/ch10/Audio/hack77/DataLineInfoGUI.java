package swinghacks.ch10.Audio.hack77;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import swinghacks.ch10.Audio.hack76.PCMFilePlayer;

public class DataLineInfoGUI extends JPanel {

	PCMFilePlayer player;
	JButton startButton;

	public DataLineInfoGUI(File f) {
		super();
		try {
			player = new PCMFilePlayer(f);
		} catch (Exception ioe) {
			add(new JLabel("Error: " + ioe.getMessage()));
			return;
		}
		DataLine line = player.getLine();
		// layout
		// line 1: name
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel("File:  " + player.getFile().getName()));
		// line 2: levels
		add(new DataLineLevelMeter(line));
		// line 3: format info as textarea
		AudioFormat format = line.getFormat();
		JTextArea ta = new JTextArea();
		ta.setBorder(new TitledBorder("Format"));
		ta.append("Encoding: " + format.getEncoding().toString() + "\n");
		ta.append("Bits/sample: " + format.getSampleSizeInBits() + "\n");
		ta.append("Channels: " + format.getChannels() + "\n");
		ta.append("Endianness: " + (format.isBigEndian() ? " big " : "little") + "\n");
		ta.append("Frame size: " + format.getFrameSize() + "\n");
		ta.append("Frame rate: " + format.getFrameRate() + "\n");
		add(ta);

		// now start playing
		player.start();
	}

	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(null);
		File file = chooser.getSelectedFile();
		DataLineInfoGUI demo = new DataLineInfoGUI(file);

		JFrame f = new JFrame("JavaSound info");
		f.getContentPane().add(demo);
		f.pack();
		f.setVisible(true);
	}

	class DataLineLevelMeter extends JPanel {
		DataLine line;
		float level = 0.0f;

		public DataLineLevelMeter(DataLine l) {
			line = l;
			Timer timer = new Timer(50, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					level = line.getLevel();
					repaint();
				}
			});
			timer.start();
		}

		public void paint(Graphics g) {
			Dimension d = getSize();
			g.setColor(Color.green);
			int meterWidth = (int) (level * (float) d.width);
			g.fillRect(0, 0, meterWidth, d.height);
		}

	}

}
