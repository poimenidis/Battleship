package guiPackage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class MainMenu {

	private JFrame frame;
	private Clip clip; //ειναι αντικειμενο για τραγουδι
	private boolean soundEffects, soundtrack; //για το soundeffect kai soundtrack


	/*Θα μπορουσα να γραψω extends JFrame ωστε να μην χρησιμοπουμε συνεχως 
	frame αλλα this. Για να μην μπερδευομαστε προτιμησα το frame*/
	public MainMenu(JFrame f, boolean sEffects, boolean sTrack) {
		frame = new JFrame();

		/*αν εχει φτιαχτει ηδη frame (πχ εχω πατησει play χω μπει στο παιχνιδι και μετα θελω να ξαναγυρισω
		στο μενου, τοτε για να μεινουμε στο ιδιο παραθυρο χρησιμοποιουμε το ιδιο frame).
		Αν σβηστει η παρακατω εντολη δεν θα καταχωρηθει τιποτα καινουργιο*/
		if(f != null) {
			frame = f;
		}

		//Εδω καταχωρω την background εικονα
		try {
			frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(ResourceLoader.load("img/background-menu.jpg")))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		/*Πιο κατω εχω φτιαξει κλασεις get και set για το soundtrack και για τα soundeffect. Εδω δεχονται 
		αυτα που εχω περασει στην menu. Αν καλειται απο την main, δεχεται 2 true αλλιως δεχεται
		την προηγουμενη κατασταση της.*/
		setSoundtrack(sTrack);
		setSoundEfffects(sEffects);

		//Παιζουμε το soundtrack του μενου
		if(getSoundtrack()) {
			enableMusic("sound/MenuSong.wav");
		}

		//το logo του παιχνιδιου
		JLabel label = new JLabel();
		try {
			label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/title.jpg"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		label.setBounds(316, 11, 500, 208);

		//start button
		JButton startButton = new JButton();
		try {
			startButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/button-start.jpg"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		startButton.setBounds(403, 230, 313, 140);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//εδω μετακινουμαστε στην κλαση οπου γινεται η επιλογη παιχνιου
				new ChooseGameOptionMenu(frame, getSoundEfffects(), getSoundtrack(), clip);
			}
		});

		//exit button
		JButton exitButton = new JButton();
		try {
			exitButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/button-exit.jpg"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		exitButton.setBounds(485, 480, 150, 85);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "CLOSE GAME",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(reply == JOptionPane.YES_OPTION) {
					//κλεινει το παραθυρο
					System.exit(0);
				}
			}
		});

		//soundeffects button
		JButton soundEffectsButton = new JButton();
		soundEffectsButton.setBounds(53, 274, 89, 82);
		soundEffectsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getSoundEfffects()) {
					try {
						//αλλαζει την εικονα αν πατηθει το κουμπι,και κανει την soundef false
						soundEffectsButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/sound_effects_off.png"))));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					setSoundEfffects(false);
				}
				else {
					try {
						//το αντιθετο
						soundEffectsButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/sound_effects_on.png"))));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					setSoundEfffects(true);
				}
			}
		});
		
		/*Αν εχει απενεργοποιηθει ο ηχος πιο πριν, και μετα πατησουμε απο την gui το κουμπι
		  back to main menu, για να μεινει η εικονα με κοκκινη γραμη
		 γινεται το παρακατω*/
		if(getSoundEfffects()) {
			try {
				soundEffectsButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/sound_effects_on.png"))));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else {
			try {
				soundEffectsButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/sound_effects_off.png"))));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		//soundtrack button
		JButton soundtrackButton = new JButton();
		soundtrackButton.setBounds(945, 274, 89, 69);
		soundtrackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(getSoundtrack()) {
					try {
						soundtrackButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/music_off.png"))));
					} catch (IOException e) {
						e.printStackTrace();
					}
					clip.stop();
					setSoundtrack(false);
				}
				else {
					try {
						soundtrackButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/music_on.png"))));
					} catch (IOException e) {
						e.printStackTrace();
					}
					enableMusic("sound/MenuSong.wav");
					setSoundtrack(true);
				}

			}
		});

		if(getSoundtrack()) {
			try {
				soundtrackButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/music_on.png"))));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else {
			try {
				soundtrackButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/music_off.png"))));
			} catch (IOException e1) {
				e1.printStackTrace();
			}		
		}

		//κουμπι credits
		JButton creditsButton = new JButton();
		try {
			creditsButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/credits.jpg"))));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		creditsButton.setBounds(400, 376, 320, 91);
		creditsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Credits(frame,clip,getSoundEfffects(),getSoundtrack());
			}
		});

		frame.setBounds(100, 100, 1100, 610);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(label);
		frame.getContentPane().add(startButton);

		frame.getContentPane().add(exitButton);
		frame.getContentPane().add(creditsButton);
		frame.getContentPane().add(soundEffectsButton);
		frame.getContentPane().add(soundtrackButton);
		frame.validate();		
	}

	//κλαση που παιζει το soundtrack
	public void enableMusic(String path) {
		try {
			java.net.URL soundurl = getClass().getResource("/"+path);
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(soundurl));

			if(clip != null) {
				new Thread() {
					public void run() {
						synchronized (clip) {
							clip.stop();
							clip.setFramePosition(0);
							clip.loop(Clip.LOOP_CONTINUOUSLY);
						}
					}
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean getSoundEfffects() {
		return soundEffects;
	}

	public void setSoundEfffects(boolean soundEffects) {
		this.soundEffects = soundEffects;
	}

	public boolean getSoundtrack(){
		return soundtrack;
	}

	public void setSoundtrack(boolean soundtrack) {
		this.soundtrack = soundtrack;
	}

	public JFrame getFrame() {
		return frame;
	}

}
