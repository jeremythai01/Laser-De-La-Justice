package aaplication;

import java.awt.EventQueue;
import java.awt.Font;
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



/**
 * Cette classe permet demande au joueur s'il veut lancer une nouvelle partie ou charger une partie sauvegarde
 * @author Miora
 *
 */
public class FenetrePartie extends JFrame {

	private JPanel contentPane;
	private FenetreJeu jeu;

	/**
	 * Lancer l'application
	 */
	public static void main(String[] args) {
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
				String nomFichier = "";
				String userDir = System.getProperty("user.dir");
				JFileChooser fc = new JFileChooser(userDir + "/Laser de la justice" );
	            int r = fc.showSaveDialog(null); 
	            if (r == JFileChooser.APPROVE_OPTION) { 
	            	File selectedFile = fc.getSelectedFile();
	            	nomFichier = selectedFile.getName();
	            } 
	            else {
	               JOptionPane.showMessageDialog(null, "Probleme lecture niveau ou sauvegarde, lancement d'une nouvelle partie");
	            } 
				jeu = new FenetreJeu(false, nomFichier);
				jeu.isNouvelle(false);
				jeu.setVisible(true);
				jeu.donneFocusALasceneFinale();
				setVisible(false);
			}
		});
		btnCharge.setBounds(36, 118, 162, 32);
		contentPane.add(btnCharge);
		
		
		
		JButton btnNouv = new JButton("Nouvelle partie");
		btnNouv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jeu = new FenetreJeu(true, null);  // isNouvelle = true
				jeu.isNouvelle(true);
				jeu.setNouveauOption(true);
				jeu.setVisible(true);
				jeu.donneFocusALasceneFinale();
				setVisible(false);
			}
		});
		btnNouv.setBounds(234, 118, 162, 32);
		contentPane.add(btnNouv);
	}
}
