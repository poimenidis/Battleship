package guiPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Se auth th klash emfanizontai ola ta melh ths omadas
 * kai o rolos poy eixan sthn ylopoihsh ths ergasias
 */
public class Credits {
	JFrame frame;
	JLabel credits;

	public Credits(JFrame f,Clip clip,boolean sEffect,boolean sTrack) {
		
		this.frame = f;
		
		frame.getContentPane().removeAll();	
		frame.validate();
		frame.repaint();
		
		credits = new JLabel("<html>"+"CREDITS<br><br>Ομάδα Προγραμματισμού-Κώδικα: <br>"
				+ "Ντόγας Γεώργιος<br>Παυλίδης Γεώργιος<br>Ποιμενίδης Κωνσταντίνος<br>"
				+ "Φέτο Νικόλαος<br><br> Ομάδα Τεκμηρίωσης:<br>Ελένης Γεώργιος<br>Μπακρατσάς Αθανάσιος<br> "
				+ "Μηνοπούλου Σοφία <br>Τσιλιμπώνη Ευγενία<br><br>Ομάδα Design-Εικόνων:<br>Οθωναίος Κωνσταντίνος"
				+ "<br><br>Project Manager:<br>Ασημακόπουλος Παύλος</html>");
		credits.setForeground(Color.white);
		credits.setBackground(Color.darkGray);
		credits.setOpaque(true);
		credits.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 25));
		credits.setBounds(300, -25, 500, 600);
		
		JButton backToMainMenuButton = new JButton();
		try {
			backToMainMenuButton.setIcon(new ImageIcon(ImageIO.read(ResourceLoader.load("img/button-back_to_main_menu.jpg"))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		backToMainMenuButton.setBounds(50, 498, 143, 65);
		backToMainMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				frame.repaint();

				if(sTrack) {
					clip.stop();
					clip.close();
				}
				

				new MainMenu(frame, sEffect, sTrack);
			}
		});

		frame.getContentPane().add(backToMainMenuButton);
		frame.getContentPane().add(credits);
		frame.setBackground(Color.BLACK);
		frame.validate();
		frame.repaint();
	}
	
	

}
