package guiPackage;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import gameFunctionality.Game;


public class Board extends Thread {
	private JFrame frame;
	private boolean soundEffects, soundtrack;
	private Clip clip;
	private Clip clip2;
	private JLabel player, player1, player2orpc;
	private JButton seaButton1[][] = new JButton[10][10]; 
	private JButton seaButton2[][] = new JButton[10][10]; 
	private int i, j;
	private int[] sunkShip = new int[4]; //0 x, 1 y, 2 size, 3 direction
	private String gameMode;

	public Board(JFrame f, boolean sEffects, boolean sTrack, String gameMode) {
		/*με το troposPaixnidiou βλεπω πως επελεξε ο χρηστης να παιξει. 
		 * Αν troposPaixnidiou=
		 * pvp=player1 vs player2
		 * Easy= pc eykolo
		 * Medium= pc normal
		 * Hard= pc dyskolo
		 */

		NewGame.co = new Connect();
		Connect.setGameMode(gameMode);
		this.gameMode = gameMode;

		//μεταφερει την παλια frame
		this.frame = f;
		//τα σβηνει ολα τα προηγουμενα για να φτιαχτει η νεα πλατφορμα
		frame.getContentPane().removeAll();
		frame.repaint();

		try {
			frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(ResourceLoader.load("img/background-battleship.jpg")))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		this.soundEffects=sEffects;
		this.soundtrack=sTrack;

		this.start();

		try {
			Thread.sleep(40);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		//αν στην menu δεν εχει πατηθει το κουμπι ηχου τοτε παιξτον
		if(soundtrack) {
			enableMusic("sound/battleship.wav");
		}

		setSoundEffects(soundEffects);
		setSoundtrack(soundtrack);

		if(gameMode.equals("pvp")) {
			player = new JLabel("<html>PLAYER1<br>PLAYS</html>");
			player1 = new JLabel("PLAYER1 ARENA");
			player2orpc = new JLabel("PLAYER2 ARENA");
		}
		else {
			player = new JLabel("<html>PLAYER<br>PLAYS</html>");
			player1 = new JLabel("PLAYER ARENA");
			player2orpc = new JLabel("PC ARENA");
		}

		player.setForeground(Color.WHITE);
		player.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
		player.setBounds(440, 0, 220, 100);

		player1.setForeground(Color.black);
		player1.setFont(new Font("Times New Roman", Font.BOLD , 40));
		player1.setBounds(50, 0, 500, 80);			

		player2orpc.setForeground(Color.black);
		player2orpc.setFont(new Font("Times New Roman", Font.BOLD , 40));
		player2orpc.setBounds(680, 0, 500, 80);

		//koumpi sound effects
		JButton sounEffectsButton = new JButton();
		sounEffectsButton.setBounds(53, 474, 89, 82);
		sounEffectsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getSoundEffects()){
					try {
						//αλλαζει την εικονα αν πατηθει το κουμπι,και κανει την soundef false
						sounEffectsButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/sound_effects_off.png"))));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					setSoundEffects(false);
				}
				else{
					try {
						//το αντιθετο
						sounEffectsButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/sound_effects_on.png"))));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					setSoundEffects(true);
				}
			}
		});

		if(getSoundEffects()) {
			try {
				sounEffectsButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/sound_effects_on.png"))));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else {
			try {
				sounEffectsButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/sound_effects_off.png"))));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		//koumpi soundtrack 
		JButton soundtrackButton = new JButton();
		soundtrackButton.setBounds(945, 474, 89, 69);
		soundtrackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(getSoundtrack()) {
					try {
						soundtrackButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/music_off.png"))));
					} catch (IOException e) {
						e.printStackTrace();
					}
					clip.stop(); //σταματαω τον ηχο για να μην παιζεται ταυτοχρονα με τον καινουργιο
					setSoundtrack(false);
				}
				else {
					try {
						soundtrackButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/music_on.png"))));
					} catch (IOException e) {
						e.printStackTrace();
					}
					enableMusic("sound/battleship.wav");
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

		JButton backToMainMenuButton = new JButton();
		try {
			backToMainMenuButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/button-back_to_main_menu.jpg"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		backToMainMenuButton.setBounds(473, 498, 143, 65);
		backToMainMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int reply = JOptionPane.showConfirmDialog(null, "Are you sure you to close the game?", "CLOSE GAME",  
						JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);

				if(reply == JOptionPane.YES_OPTION) {
					frame.getContentPane().removeAll();
					frame.repaint();

					if(getSoundtrack()) {
						clip.stop();
					}

					new MainMenu(frame, getSoundEffects(), getSoundtrack());
				}
			}
		});

		frame.getContentPane().add(player);
		frame.getContentPane().add(player1);
		frame.getContentPane().add(player2orpc); 
		frame.getContentPane().add(sounEffectsButton);
		frame.getContentPane().add(soundtrackButton);
		frame.getContentPane().add(backToMainMenuButton);
		frame.validate();
	}

	//κλαση που παιζει τα soundeffects
	public void sound(String path, int delay, int numberOfLoops) {
		if(soundEffects) {
			for(int i = 0; i < numberOfLoops; i++) {
				new Thread() {
					public void run() {
						try {

							java.net.URL soundurl = getClass().getResource("/"+path);
							clip2 = AudioSystem.getClip();
							clip2.open(AudioSystem.getAudioInputStream(soundurl));
							clip2.start();
							Thread.sleep(clip2.getMicrosecondLength());
						} catch (Exception e) {
						}
					}
				}.start();

				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setSoundEffects(boolean soundEffects) {
		this.soundEffects = soundEffects;
	}

	public boolean getSoundEffects() {
		return soundEffects;
	}

	public boolean getSoundtrack(){
		return soundtrack;
	}

	public void setSoundtrack(boolean soundtrack) {
		this.soundtrack = soundtrack;
	}

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



	public void run() {		
		new Game().start();
		// First Arena
		for(i = 0; i < 10; i++) {
			for(j = 0; j < 10; j++) {
				seaButton1[i][j] = new JButton();
				try {
					seaButton1[i][j].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-sea.jpg"))));
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				seaButton1[i][j].setBounds(40 + i * 38, 70 + j * 38, 40, 40);
				seaButton1[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String temp;

						if(gameMode.equals("pvp")) {
							temp = "<html>PLAYER2<br>PLAYS</html>";
						}
						else {
							temp = "<html>PC<br>PLAYS</html>";
						}

						if(player.getText().equals(temp)) {
							JButton button = (JButton)e.getSource();

							int x = button.getBounds().x;
							int y = button.getBounds().y;

							x = (x - 40) / 38;
							y = (y - 70) / 38;
							if(gameMode.equals("pvp")) {
								NewGame.co.setHit(x, y);

								try {
									sleep(40);
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
							else {

								if(Connect.getGameStatus()){
									NewGame.co.setControl(true);

									int hit[] = NewGame.co.getPcHit();
									x = hit[0];
									y = hit[1];
								}else{
									Connect.setStatusHit(2);
								}
							}
							if(Connect.getGameStatus()){
								if(Connect.getStatusHit() == -1) { //hit success
									sound("sound/explosion.wav", 1, 1);

									try {
										seaButton1[x][y].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-shot.jpg"))));
									} catch (IOException e1) {
										e1.printStackTrace();
									}

									if(!gameMode.equals("pvp")) {
										Timer timer = new Timer();
										timer.schedule(new TimerTask() {
											public void run() {
												seaButton1[1][1].doClick();
											}
										}, 1300);
									}
								}
								else if(Connect.getStatusHit() == 1){ //hit miss
									try {
										seaButton1[x][y].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
									} catch (IOException e1) {
										e1.printStackTrace();
									}	
									sound("sound/Video_Game_Splash-Ploor-699235037.wav", 1, 1);

									if(gameMode.equals("pvp"))
										player.setText("<html>PLAYER1<br>PLAYS</html>");
									else{
										player.setText("<html>PLAYER<br>PLAYS</html>");
									}
								}
								else if(Connect.getStatusHit() == 3) {
									sound("sound/Shotgun_Blast-Jim_Rogers-1914772763.wav", 1, 1);

									sunkShip = Connect.getSunkShip();

									int xx = sunkShip[0]; //x axis
									int yy = sunkShip[1]; //y axis
									int zz = sunkShip[2]; //size

									if(sunkShip[3] == 0) { //horizontal direction
										for(int k = 0; k < zz; k++){
											frame.remove(seaButton1[xx + k][yy]);

											if(yy != 0) {
												try {
													seaButton1[xx + k][yy - 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
												} catch (IOException e1) {
													e1.printStackTrace();
												}

												for(ActionListener act : seaButton1[xx + k][yy - 1].getActionListeners()) {
													if(seaButton1[1][1] != seaButton1[xx + k][yy - 1] || gameMode.equals("pvp")) {
														seaButton1[xx + k][yy - 1].removeActionListener(act);
													}
												}
											}

											if(yy != 9){
												try {
													seaButton1[xx + k][yy + 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
												} catch (IOException e1) {
													e1.printStackTrace();
												}

												for(ActionListener act : seaButton1[xx + k][yy + 1].getActionListeners()) {
													if(seaButton1[1][1] != seaButton1[xx + k][yy + 1] || gameMode.equals("pvp")) {
														seaButton1[xx + k][yy + 1].removeActionListener(act);
													}
												}
											}
										}

										if(xx != 0) {
											if(yy != 0) {
												try {
													seaButton1[xx - 1][yy - 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
												} catch (IOException e1) {
													e1.printStackTrace();
												}

												for(ActionListener act : seaButton1[xx - 1][yy - 1].getActionListeners()) {
													if(seaButton1[1][1] != seaButton1[xx - 1][yy - 1] || gameMode.equals("pvp")) {
														seaButton1[xx - 1][yy - 1].removeActionListener(act);
													}
												}
											}

											try {
												seaButton1[xx - 1][yy].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton1[xx - 1][yy].getActionListeners()) {
												if(seaButton1[1][1] != seaButton1[xx - 1][yy] || gameMode.equals("pvp"))
													seaButton1[xx - 1][yy].removeActionListener(act);
											}

											if(yy != 9) {
												try {
													seaButton1[xx - 1][yy + 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
												} catch (IOException e1) {
													e1.printStackTrace();
												}

												for(ActionListener act : seaButton1[xx - 1][yy + 1].getActionListeners()) {
													if(seaButton1[1][1] != seaButton1[xx - 1][yy + 1] || gameMode.equals("pvp")) {
														seaButton1[xx-1][yy+1].removeActionListener(act);
													}
												}
											}
										}

										if(xx + zz != 10) {
											if(yy != 0) {
												try {
													seaButton1[xx + zz][yy - 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
												} catch (IOException e1) {
													e1.printStackTrace();
												}

												for(ActionListener act : seaButton1[xx + zz][yy - 1].getActionListeners()) {
													if(seaButton1[1][1] != seaButton1[xx + zz][yy - 1] || gameMode.equals("pvp")) {
														seaButton1[xx + zz][yy - 1].removeActionListener(act);
													}
												}
											}

											try {
												seaButton1[xx+zz][yy].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton1[xx + zz][yy].getActionListeners()) {
												if(seaButton1[1][1] != seaButton1[xx + zz][yy] || gameMode.equals("pvp")) {
													seaButton1[xx + zz][yy].removeActionListener(act);
												}
											}

											if(yy != 9) {
												try {
													seaButton1[xx + zz][yy + 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
												} catch (IOException e1) {
													e1.printStackTrace();
												}

												for(ActionListener act : seaButton1[xx + zz][yy + 1].getActionListeners()) {
													if(seaButton1[1][1] != seaButton1[xx + zz][yy + 1] || gameMode.equals("pvp")) {
														seaButton1[xx + zz][yy + 1].removeActionListener(act);
													}
												}
											}
										}

										JLabel label = new JLabel();
										if(zz == 2) {
											try {
												label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship2horizontal.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}
										}
										else if(zz == 3) {
											try {
												label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship3horizontal.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}
										}
										else if(zz == 4) {
											try {
												label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship4horizontal.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}
										}
										else {
											try {
												label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship5horizontal.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}
										}
										label.setBounds(xx * 38 + 40, yy * 38 + 70 , 40 * zz, 40);
										frame.getContentPane().add(label);
									}
									else { //vertical direction
										for(int k = 0; k < zz; k++){
											frame.remove( seaButton1[xx][yy + k]);

											if(xx != 0) {
												try {
													seaButton1[xx - 1][yy + k].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
												} catch (IOException e1) {
													e1.printStackTrace();
												}

												for(ActionListener act : seaButton1[xx - 1][yy + k].getActionListeners()) {
													if(seaButton1[1][1] != seaButton1[xx - 1][yy + k] || gameMode.equals("pvp")) {
														seaButton1[xx - 1][yy + k].removeActionListener(act);
													}
												}
											}

											if(xx != 9) {
												try {
													seaButton1[xx + 1][yy + k].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
												} catch (IOException e1) {
													e1.printStackTrace();
												}

												for(ActionListener act : seaButton1[xx + 1][yy + k].getActionListeners()) {
													if(seaButton1[1][1] != seaButton1[xx + 1][yy + k] || gameMode.equals("pvp")) {
														seaButton1[xx + 1][yy + k].removeActionListener(act);
													}
												}
											}
										}

										if(yy != 0) {
											if(xx != 0) {
												try {
													seaButton1[xx - 1][yy - 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
												} catch (IOException e1) {
													e1.printStackTrace();
												}

												for(ActionListener act : seaButton1[xx - 1][yy - 1].getActionListeners()) {
													if(seaButton1[1][1] != seaButton1[xx - 1][yy - 1] || gameMode.equals("pvp")) {
														seaButton1[xx - 1][yy - 1].removeActionListener(act);
													}
												}
											}

											try {
												seaButton1[xx][yy - 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton1[xx][yy - 1].getActionListeners()) {
												if(seaButton1[1][1] != seaButton1[xx][yy - 1] || gameMode.equals("pvp")) {
													seaButton1[xx][yy-1].removeActionListener(act);
												}
											}

											if(xx != 9) {
												try {
													seaButton1[xx + 1][yy - 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
												} catch (IOException e1) {
													e1.printStackTrace();
												}

												for(ActionListener act : seaButton1[xx + 1][yy - 1].getActionListeners()) {
													if(seaButton1[1][1] != seaButton1[xx+1][yy-1] || gameMode.equals("pvp")) {
														seaButton1[xx + 1][yy - 1].removeActionListener(act);
													}
												}
											}
										}

										if(yy + zz != 10) {
											if(xx!=0) {
												try {
													seaButton1[xx - 1][yy + zz].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
												} catch (IOException e1) {
													e1.printStackTrace();
												}

												for(ActionListener act : seaButton1[xx - 1][yy + zz].getActionListeners()) {
													if(seaButton1[1][1] != seaButton1[xx - 1][yy + zz] || gameMode.equals("pvp")) {
														seaButton1[xx - 1][yy + zz].removeActionListener(act);
													}
												}
											}

											try {
												seaButton1[xx][yy + zz].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton1[xx][yy + zz].getActionListeners()) {
												if(seaButton1[1][1] != seaButton1[xx][yy + zz] || gameMode.equals("pvp")) {
													seaButton1[xx][yy + zz].removeActionListener(act);
												}
											}

											if(xx != 9) {
												try {
													seaButton1[xx + 1][yy + zz].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
												} catch (IOException e1) {
													e1.printStackTrace();
												}

												for(ActionListener act : seaButton1[xx + 1][yy + zz].getActionListeners()) {
													if(seaButton1[1][1] != seaButton1[xx + 1][yy + zz] || gameMode.equals("pvp")) {
														seaButton1[xx + 1][yy + zz].removeActionListener(act);
													}
												}
											}
										}

										JLabel label = new JLabel();
										if(zz == 2) {
											try {
												label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship2vertical.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}
										}
										else if(zz == 3) {
											try {
												label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship3vertical.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}
										}
										else if(zz == 4) {
											try {
												label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship4vertical.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}
										}
										else {
											try {
												label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship5vertical.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}
										}
										label.setBounds(xx * 38 + 40, yy * 38 + 63 , 40, 40 * zz);
										frame.getContentPane().add(label);
									}

									frame.repaint();

									if( !gameMode.equals("pvp")){
										Timer timer = new Timer();
										timer.schedule(new TimerTask() {
											public void run() {
												seaButton1[1][1].doClick();
											}
										}, 1300);
									}
								}

							}else{
								frame.getContentPane().removeAll();
								frame.repaint();

								if(getSoundtrack()) {
									clip.stop();
								}

								if(gameMode.equals("pvp")) {
									new WinnerScreen(frame, getSoundEffects(), getSoundtrack(), true, Connect.getWinner());
								}
								else {
									if(Connect.getWinner().equals("Computer")) {
										new WinnerScreen(frame, getSoundEffects(), getSoundtrack(), false, "PLAYER");
									}
									else {
										new WinnerScreen(frame, getSoundEffects(), getSoundtrack(), true, "PLAYER");
									}
								}
							}

							if(seaButton1[1][1] != seaButton1[x][y] || gameMode.equals("pvp")) {
								seaButton1[x][y].removeActionListener(this);
							}
						}
					}
				});

				frame.getContentPane().add(seaButton1[i][j]);
			}
		}

		// Second Arena 
		for(i = 0; i < 10; i++) {
			for(j = 0;j < 10; j++){
				seaButton2[i][j] = new JButton();
				try {
					seaButton2[i][j].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-sea.jpg"))));
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				seaButton2[i][j].setBounds(660 + i * 38, 70 + j * 38, 40, 40);
				seaButton2[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String temp;

						if(gameMode.equals("pvp")) {
							temp = "<html>PLAYER1<br>PLAYS</html>";
						}
						else {
							temp = "<html>PLAYER<br>PLAYS</html>";
						}

						if(player.getText().equals(temp)){
							JButton button = (JButton)e.getSource();

							int x = button.getBounds().x;
							int y = button.getBounds().y;

							x = (x - 660) / 38;
							y = (y - 70) / 38;

							NewGame.co.setHit(x, y);

							try {
								sleep(40);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}

							if(Connect.getStatusHit() == -1){ //hit success
								sound("sound/explosion.wav", 1, 1);
								try {
									seaButton2[x][y].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-shot.jpg"))));
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
							else if(Connect.getStatusHit() == 1){ //hit miss
								try {
									seaButton2[x][y].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
								} catch (IOException e1) {
									e1.printStackTrace();
								}	
								sound("sound/Video_Game_Splash-Ploor-699235037.wav", 1 ,1);

								if(gameMode.equals("pvp")) {
									player.setText("<html>PLAYER2<br>PLAYS</html>");
								}
								else {
									player.setText("<html>PC<br>PLAYS</html>");

									if(player.getText().equals("<html>PC<br>PLAYS</html>")) {
										Timer timer = new Timer();
										timer.schedule(new TimerTask() {
											public void run() {
												seaButton1[1][1].doClick();
											}
										}, 1300);
									}
								}
							}
							else if(Connect.getStatusHit() == 3){
								sound("sound/Shotgun_Blast-Jim_Rogers-1914772763.wav",1,1);

								sunkShip = Connect.getSunkShip();

								int xx = sunkShip[0]; //x axis
								int yy = sunkShip[1]; //y axis
								int zz = sunkShip[2]; //size

								if(sunkShip[3] == 0){ //horizontal direction
									for(int k = 0;k < zz; k++){
										frame.remove(seaButton2[xx + k][yy]);

										if(yy != 0) {
											try {
												seaButton2[xx + k][yy - 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton2[xx + k][yy - 1].getActionListeners()) {
												seaButton2[xx + k][yy - 1].removeActionListener(act);
											}
										}

										if(yy != 9) {
											try {
												seaButton2[xx + k][yy + 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton2[xx + k][yy + 1].getActionListeners()) {
												seaButton2[xx + k][yy + 1].removeActionListener(act);
											}
										}
									}

									if(xx != 0) {
										if(yy != 0) {
											try {
												seaButton2[xx - 1][yy - 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton2[xx - 1][yy - 1].getActionListeners()) {
												seaButton2[xx - 1][yy - 1].removeActionListener(act);
											}
										}

										try {
											seaButton2[xx - 1][yy].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
										} catch (IOException e2) {
											e2.printStackTrace();
										}

										for(ActionListener act : seaButton2[xx - 1][yy].getActionListeners()) {
											seaButton2[xx - 1][yy].removeActionListener(act);
										}

										if(yy != 9) {
											try {
												seaButton2[xx - 1][yy + 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton2[xx - 1][yy + 1].getActionListeners()) {
												seaButton2[xx - 1][yy + 1].removeActionListener(act);
											}
										}
									}

									if(xx + zz != 10) {
										if(yy != 0) {
											try {
												seaButton2[xx + zz][yy - 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton2[xx + zz][yy - 1].getActionListeners()) {
												seaButton2[xx + zz][yy - 1].removeActionListener(act);
											}
										}

										try {
											seaButton2[xx + zz][yy].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
										} catch (IOException e2) {
											e2.printStackTrace();
										}

										for(ActionListener act : seaButton2[xx + zz][yy].getActionListeners()) {
											seaButton2[xx + zz][yy].removeActionListener(act);
										}

										if(yy != 9) {
											try {
												seaButton2[xx + zz][yy + 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton2[xx + zz][yy + 1].getActionListeners()) {
												seaButton2[xx + zz][yy + 1].removeActionListener(act);
											}
										}
									}

									JLabel label = new JLabel();
									if(zz == 2) {
										try {
											label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship2horizontal.jpg"))));
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
									else if(zz == 3) {
										try {
											label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship3horizontal.jpg"))));
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
									else if(zz == 4) {
										try {
											label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship4horizontal.jpg"))));
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
									else {
										try {
											label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship5horizontal.jpg"))));
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
									label.setBounds(xx * 38 + 653, yy * 38 + 70, 40 * zz, 40);
									frame.getContentPane().add(label);
								}
								else { //vertical direction
									for(int k = 0; k < zz; k++) {
										frame.remove(seaButton2[xx][yy + k]);

										if(xx != 0) {
											try {
												seaButton2[xx - 1][yy + k].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton2[xx - 1][yy + k].getActionListeners()) {
												seaButton2[xx - 1][yy + k].removeActionListener(act);
											}
										}

										if(xx != 9 ){
											try {
												seaButton2[xx + 1][yy + k].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton2[xx + 1][yy + k].getActionListeners()) {
												seaButton2[xx + 1][yy + k].removeActionListener(act);
											}
										}
									}

									if(yy != 0) {
										if(xx != 0) {
											try {
												seaButton2[xx - 1][yy - 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton2[xx - 1][yy - 1].getActionListeners()) {
												seaButton2[xx - 1][yy - 1].removeActionListener(act);
											}
										}

										try {
											seaButton2[xx][yy - 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
										} catch (IOException e2) {
											e2.printStackTrace();
										}

										for(ActionListener act : seaButton2[xx][yy - 1].getActionListeners()) {
											seaButton2[xx][yy - 1].removeActionListener(act);
										}

										if(xx != 9) {
											try {
												seaButton2[xx + 1][yy - 1].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton2[xx + 1][yy - 1].getActionListeners()) {
												seaButton2[xx + 1][yy - 1].removeActionListener(act);
											}
										}
									}

									if(yy + zz != 10) {
										if(xx != 0) {
											try {
												seaButton2[xx - 1][yy + zz].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton2[xx - 1][yy + zz].getActionListeners()) {
												seaButton2[xx - 1][yy + zz].removeActionListener(act);
											}
										}

										try {
											seaButton2[xx][yy + zz].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
										} catch (IOException e2) {
											e2.printStackTrace();
										}

										for(ActionListener act : seaButton2[xx][yy + zz].getActionListeners()) {
											seaButton2[xx][yy + zz].removeActionListener(act);
										}

										if(xx != 9) {
											try {
												seaButton2[xx + 1][yy + zz].setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/table-miss.jpg"))));
											} catch (IOException e1) {
												e1.printStackTrace();
											}

											for(ActionListener act : seaButton2[xx + 1][yy + zz].getActionListeners()) {
												seaButton2[xx + 1][yy + zz].removeActionListener(act);
											}
										}
									}

									JLabel label = new JLabel();
									if(zz == 2) {
										try {
											label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship2vertical.jpg"))));
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
									else if(zz == 3) {
										try {
											label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship3vertical.jpg"))));
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
									else if(zz == 4) {
										try {
											label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship4vertical.jpg"))));
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
									else {
										try {
											label.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/ship5vertical.jpg"))));
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
									label.setBounds(xx * 38 + 660, yy * 38 + 63 , 40, 40 * zz);
									frame.getContentPane().add(label);

								}

								frame.repaint();
							}

							if(!Connect.getGameStatus()){
								frame.getContentPane().removeAll();
								frame.repaint();


								if(getSoundtrack()) {
									clip.stop();

								}

								if(gameMode.equals("pvp")) {
									new WinnerScreen(frame, getSoundEffects(), getSoundtrack(), true, Connect.getWinner());
								}
								else {
									if(Connect.getWinner().equals("Computer")) {
										new WinnerScreen(frame, getSoundEffects(), getSoundtrack(), false, "PLAYER");
									}
									else {
										new WinnerScreen(frame, getSoundEffects(), getSoundtrack(), true, Connect.getWinner());
									}
								}
							}

							seaButton2[x][y].removeActionListener(this);
						}
					}
				});


				frame.getContentPane().add(seaButton2[i][j]);
			}
		}

		frame.revalidate();
		frame.repaint();
	}

}