package aaplication;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import son.Bruit;
import javax.swing.UIManager;




/**
 * Cette classe permet demande au joueur s'il veut lancer une nouvelle partie ou charger une partie sauvegarde
 * @author Miora
 *
 */
public class FenetrePartie extends JFrame {

	private JPanel contentPane;
	private FenetreJeu jeu;
	private Bruit son = new Bruit();

	/**
	 * Lancer l'application
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FenetrePartie frame = new FenetrePartie();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creation de la fenetre
	 */
	public FenetrePartie() {
		setTitle("Commencer une partie");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		Dimension ecranDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(ecranDimension.width/2-getSize().width/2, ecranDimension.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblques = new JLabel("Que voulez-vous faire ?");
		lblques.setFont(new Font("Segoe UI Light", Font.PLAIN, 17));
		lblques.setBounds(129, 37, 218, 32);
		contentPane.add(lblques);
		
		JButton btnCharge = new JButton("Charger une partie");
		btnCharge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("file_new");
				String nomFichier = "";
				String userDir = System.getProperty("user.home") + "/Desktop" + "/10LaserDeLaJustice" + "/Chargement";
				JFileChooser fc = new JFileChooser(userDir);
	            int r = fc.showSaveDialog(null); 
	            if (r == JFileChooser.APPROVE_OPTION) { 
	            	File selectedFile = fc.getSelectedFile();
	            	nomFichier = selectedFile.getName();
	            	jeu = new FenetreJeu(false, nomFichier);
	            } 
	            else {
	               JOptionPane.showMessageDialog(null, "Probleme lecture niveau ou sauvegarde, lancement d'une nouvelle partie");
	               jeu = new FenetreJeu(true, nomFichier);
				
	            } 
	        	jeu.setVisible(true);
				//jeu.donneFocusALasceneFinale();
				setVisible(false);
			}
		});
		btnCharge.setBounds(36, 118, 162, 32);
		contentPane.add(btnCharge);
		
		
		
		JButton btnNouv = new JButton("Nouvelle partie");
		btnNouv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("file_new");
				jeu = new FenetreJeu(true, null);  // isNouvelle = true
				jeu.setVisible(true);
				setVisible(false);
			}
		});
		btnNouv.setBounds(234, 118, 162, 32);
		contentPane.add(btnNouv);
	}
}
