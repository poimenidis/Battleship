package guiPackage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import gameFunctionality.Game;

public class ShipPlacementMenu {

	private JFrame frame;
	private Rectangle2D.Float[] myRect ;

	public ShipPlacementMenu(JFrame f, boolean sEffects, boolean sTrack, String gameMode, Clip clip, int numberOfPlayers) {
		frame = f;
		frame.getContentPane().removeAll();
		frame.repaint();

		//ola ta grafika
		MouseMoveScale mms = new MouseMoveScale();
		mms.setDoubleBuffered(true);

		//edw anaferei ton paixth pou topothetei ta ploia
		JLabel player;
		if(numberOfPlayers == 0) {
			player = new JLabel("PLAYER");
		}
		else if(numberOfPlayers == 1) {
			player = new JLabel("PLAYER1");
		}
		else  {
			player = new JLabel("PLAYER2");
		}
		player.setForeground(Color.red);
		player.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
		player.setBounds(770, 388, 241, 121);

		JButton backToMainMenuButton = new JButton();
		try {
			backToMainMenuButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/button-back_to_main_menu.jpg"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		backToMainMenuButton.setBounds(665, 498, 143, 65);
		backToMainMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				frame.repaint();

				if(sTrack) {
					clip.stop();
					clip.close();
				}

				new MainMenu(frame, sEffects, sTrack);
			}
		});

		JButton helpButton = new JButton();
		try {
			helpButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/button-help.jpg"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		helpButton.setBounds(900, 80, 138, 46);
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog( new JFrame(), "Drag the ships with the mouse to place them on the board.\n"
						+ "To change the direction of a ship from horizontal to vertical(and the opposite) scroll the mouse.\n"
						+ "Between the ships there must be a gap of at least one rectangle.", "Help",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		//κουμπι που αφου επελεξε ο χρηστης πως θελει να παιξει,ξεκινας το παιχνιδι
		JButton nextButton = new JButton();
		try {
			nextButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/button-go.jpg"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		nextButton.setBounds(885, 498, 130, 60);
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < 6; i++){
					Connect.setCoordinates(i, getMyRectColumn(myRect[i]), getMyRectRow(myRect[i]), isMyRectHorizontal(myRect[i]));
				}

				if(player.getText() == "PLAYER2") {
					Game.setPositions(1);
				}
				else {
					Game.setPositions(0);
				}

				//an den metaferthoyme ksana sthn topothethsh ploiwn gia ton allon paixth,h mousiki stamata
				if(Connect.getPlacementStatus()) {
					if(sTrack && numberOfPlayers != 1) {
						clip.stop();
						clip.close();
					}

					// edw elegxw an tha metaferthoyme ksana sthn klasi topothetisi, gia na topothetisi 
					// o deyteros paixteis ta ploia tou,h tha metaferthoyme kateytheian sto paixnidi eite 
					// giati paizoyme me ton pc eite giati exoyme mpei hdh 2 fores sthn klash ayth
					if(numberOfPlayers == 1) {
						new ShipPlacementMenu(f, sEffects, sTrack, gameMode, clip, 2);
					}
					else {
						new Board(frame, sEffects, sTrack, gameMode);
					}

				}
				else {
					// kaloume thn Connect.setPlacementStatus gia na midenisoume thn
					// lan8asmenh katastash ths prohgoumenhs prospa8eis
					Connect.setPlacementStatus(true);
					JOptionPane.showMessageDialog( new JFrame(), "Put all the ships in the table, and make sure\n that there "
							+ "is at least one space between them.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		frame.setContentPane(mms);		
		frame.getContentPane().add(player);
		frame.getContentPane().add(backToMainMenuButton);
		frame.getContentPane().add(helpButton);
		frame.getContentPane().add(nextButton);
		frame.setBackground(Color.DARK_GRAY);
		frame.validate();
	}

	class MouseMoveScale extends JComponent {

		private static final long serialVersionUID = 1L;

		//edw kataskevazontai ta ploia toy paixnidioy
		{
			myRect = new Rectangle2D.Float[6];
			myRect[0] = new Rectangle2D.Float(700, 0*60+3, 2*56, 50);
			myRect[1] = new Rectangle2D.Float(700, 1*60+2, 2*56, 50);
			myRect[2] = new Rectangle2D.Float(700, 2*60+1, 3*56, 50);
			myRect[3] = new Rectangle2D.Float(700, 3*60-1, 3*56, 50);
			myRect[4] = new Rectangle2D.Float(700, 4*60-2, 4*56, 50);
			myRect[5] = new Rectangle2D.Float(700, 5*60-3, 5*56, 50); 
		}

		private static final int ROWS = 10;
		private static final int COLUMNS = 10;

		MovingAdapter ma = new MovingAdapter();

		public MouseMoveScale() {
			addMouseMotionListener(ma);
			addMouseListener(ma);
			addMouseWheelListener(new ScaleHandler());
		}
		
		//edw ftiaxnontai ta grafika ths klashs(ploia background eikones ktl)
		public void paint(Graphics g) {
			super.paint(g);

			Graphics2D g2d = (Graphics2D) g;

			java.net.URL imgURL = getClass().getResource("/img/ocean.gif");
			Image background = Toolkit.getDefaultToolkit().getImage(imgURL);


			g.drawImage(background, 0, 0, 579, 579, this);

			int sqSize = this.getHeight() / ROWS;

			for(int i = 0; i < ROWS; i++) { 
				for(int j = 0; j < COLUMNS; j++) {
					int x = i * sqSize;
					int y = j * sqSize;

					g.setColor(Color.black);
					g.drawRect(x, y, 57, 57);
				}
			}	

			java.net.URL imgURL2 = getClass().getResource("/img/ship-horizontal.jpg");
			Image shipHorizontal = Toolkit.getDefaultToolkit().getImage(imgURL2);

			java.net.URL imgURL3 = getClass().getResource("/img/ship-vertical.jpg");
			Image shipVertical = Toolkit.getDefaultToolkit().getImage(imgURL3);




			for(int i = 0; i < 6; i++) {

				g2d.fill(myRect[i]);

				if(myRect[i].width > myRect[i].height) {
					g2d.drawImage(shipVertical, (int)myRect[i].x, (int)myRect[i].y, 
							(int)myRect[i].width, (int)myRect[i].height, null);
				}
				else {
					g2d.drawImage(shipHorizontal, (int)myRect[i].x, (int)myRect[i].y, 
							(int)myRect[i].width, (int)myRect[i].height, null);
				}

				repaint();
			}
		}
		
		//  oi parakato klaseis einai gia tis leitoyrgies toy pontikoy panw sta ploia..
		//  px na ta sernei to pontiki kai na ta kanei apo orizontia<->katheta

		class MovingAdapter extends MouseAdapter {
			private int x;
			private int y;

			public void mousePressed(MouseEvent e) {
				x = e.getX();
				y = e.getY();
			}

			public void mouseDragged(MouseEvent e) {
				int dx = e.getX() - x;
				int dy = e.getY() - y;
				int i = -1;

				for(int k = 0; k < 6 ; k++) {
					if (myRect[k].getBounds2D().contains(x, y)) {
						i = k;
						break;
					}
				}

				if(i != -1) {
					if(dy % 29 != 0) {
						dy -= dy % 29;
					}
					if(dy % 58 != 0) {
						dy -= 29;
					}
					if(dx % 29 != 0) {
						dx -= dx % 29;
					}
					if(dx % 58 != 0) {
						dx += 29;
					}

					myRect[i].x += dx;
					myRect[i].y += dy;

					if(myRect[i].x < 1) {
						myRect[i].x = 1;
					}

					if(myRect[i].y + myRect[i].height > 525) {
						myRect[i].y = 576 - myRect[i].height;
					}

					if(myRect[i].x + myRect[i].width > 1100) {
						myRect[i].x = 1100 - myRect[i].width;
					}

					if(myRect[i].y < 1) {
						myRect[i].y = 1;
					}

					repaint();
				}

				x += dx;
				y += dy;
			}
		}

		//me to scroll tou pontikiou gia na allazei apo orizontio<->katheto to ploio
		class ScaleHandler implements MouseWheelListener {
			public void mouseWheelMoved(MouseWheelEvent e) {
				int x = e.getX();
				int y = e.getY();
				float temp;

				if(e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
					for(int i = 0; i < 6; i++) {
						if (myRect[i].getBounds2D().contains(x, y)) {
							temp = myRect[i].width;
							myRect[i].width = myRect[i].height;
							myRect[i].height = temp;

							if(myRect[i].y + myRect[i].height > 525) {
								myRect[i].y=576-myRect[i].height;
							}

							repaint();
						}
					}
				}
			}
		}

	}

	public int getMyRectRow(Rectangle2D.Float myRect) {
		return (int)(myRect.y) / 56;
	}

	public int getMyRectColumn(Rectangle2D.Float myRect) {
		return (int)(myRect.x) / 56;
	}

	public boolean isMyRectHorizontal(Rectangle2D.Float myRect) {
		return myRect.height < myRect.width;
	}

	public int getMtRectSize(Rectangle2D.Float myRect) {
		if(isMyRectHorizontal(myRect)) {
			return (int)myRect.getWidth()/56;
		}
		else {
			return (int)myRect.getHeight()/56;
		}
	}

	static Image iconToImage(Icon icon) {
		if (icon instanceof ImageIcon) {
			return ((ImageIcon)icon).getImage();
		} else {
			int w = icon.getIconWidth();
			int h = icon.getIconHeight();
			GraphicsEnvironment ge = 
					GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gd.getDefaultConfiguration();
			BufferedImage image = gc.createCompatibleImage(w, h);
			Graphics2D g = image.createGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.dispose();
			return image;
		}
	}


}
