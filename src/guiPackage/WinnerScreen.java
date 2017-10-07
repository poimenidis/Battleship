package guiPackage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class WinnerScreen {

	private JFrame frame;
	private Clip clip;
	private JLabel player;
	private boolean winOrLose;

	public WinnerScreen(JFrame f, boolean sEffects, boolean sTrack, boolean winorlose, String winner) {
		frame = f;
		frame.validate();

		MouseMoveScale mms = new MouseMoveScale();
		mms.setDoubleBuffered(true);

		setWinOrLose(winorlose);

		if(sTrack){
			if(getwinorlose()) {
				enableMusic("sound/Battle_Crowd_Celebration.wav"); 
			}
			else {
				enableMusic("sound/boo.wav"); 
			}
		}

		player = new JLabel("<html>"+winner+"<br>WIN!!!:)</html>");
		player.setForeground(Color.red);
		player.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
		player.setBounds(450, 80, 400, 400);

		frame.setContentPane(mms);
		frame.getContentPane().add(player);
		frame.setBackground(Color.BLACK);
		frame.validate();
		frame.repaint();

		Timer timer = new Timer(10000, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(sTrack) {
					clip.stop();
				}

				frame.getContentPane().removeAll();
				frame.repaint();

				new MainMenu(frame,sEffects,sTrack);
			}
		});

		timer.setRepeats(false); // Only execute once
		timer.start();
	}

	public class MouseMoveScale extends JComponent {

		private static final long serialVersionUID = 1L;

		private Rectangle2D.Float myRect0 = new Rectangle2D.Float(0, 0, 383, 285);
		private Rectangle2D.Float myRect1 = new Rectangle2D.Float(0, 285, 383, 285);
		private Rectangle2D.Float myRect2 = new Rectangle2D.Float(383, 0, 318, 185);
		private Rectangle2D.Float myRect3 = new Rectangle2D.Float(383, 385, 318, 185);
		private Rectangle2D.Float myRect4 = new Rectangle2D.Float(700, 0, 383, 285);
		private Rectangle2D.Float myRect5 = new Rectangle2D.Float(700, 285, 383, 285);
		private Rectangle2D.Float myRect6 = new Rectangle2D.Float(0, 0, 1100, 570);
		private Rectangle2D.Float myRect7 = new Rectangle2D.Float(450, 410, 170, 170);

		public void paint(Graphics g) {
			super.paint(g);

			Graphics2D g2d = (Graphics2D) g;

			if(getwinorlose()) {
				g2d.fill(myRect0);
				g2d.fill(myRect1);
				g2d.fill(myRect2);
				g2d.fill(myRect3);
				g2d.fill(myRect4);
				g2d.fill(myRect5);

				java.net.URL imgURL1 = getClass().getResource("/img/results-won.gif");
				 Image img1 = Toolkit.getDefaultToolkit().getImage(imgURL1);
				

				g2d.drawImage(img1, (int)myRect0.x, (int)myRect0.y, (int)myRect0.width, (int)myRect0.height, null);
				g2d.drawImage(img1, (int)myRect1.x, (int)myRect1.y, (int)myRect1.width, (int)myRect1.height, null);
				g2d.drawImage(img1, (int)myRect2.x, (int)myRect2.y, (int)myRect2.width, (int)myRect2.height, null);
				g2d.drawImage(img1, (int)myRect3.x, (int)myRect3.y, (int)myRect3.width, (int)myRect3.height, null);
				g2d.drawImage(img1, (int)myRect4.x, (int)myRect4.y, (int)myRect4.width, (int)myRect4.height, null);
				g2d.drawImage(img1, (int)myRect5.x, (int)myRect5.y, (int)myRect5.width, (int)myRect5.height, null);

				repaint();
			}
			else {
				
				
				java.net.URL imgURL2 = getClass().getResource("/img/results-lost.gif");
				 Image img2 = Toolkit.getDefaultToolkit().getImage(imgURL2);
				 
				 java.net.URL imgURL3 = getClass().getResource("/img/results-you_lose_msg.jpg");
				 Image img3 = Toolkit.getDefaultToolkit().getImage(imgURL3);
				
				

				g2d.fill(myRect6);
				g2d.fill(myRect7);
				g2d.drawImage(img2, (int)myRect6.x, (int)myRect6.y, (int)myRect6.width, (int)myRect6.height, null);
				g2d.drawImage(img3, (int)myRect7.x, (int)myRect7.y, (int)myRect7.width, (int)myRect7.height, null);

				repaint();
			}
		}

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

	public void setWinOrLose(boolean winOrLose){
		this.winOrLose = winOrLose;
	}

	public boolean getwinorlose(){
		return winOrLose;
	}

}