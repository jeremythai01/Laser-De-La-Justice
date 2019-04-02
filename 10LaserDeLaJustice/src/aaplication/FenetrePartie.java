package aaplication;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/**
 * Cette classe permet demande au joueur s'il veut lancer une nouvelle partie ou charger une partie sauvegarde
 * @author Miora
 *
 */
public class FenetrePartie extends JFrame {

	private JPanel contentPane;
	private FenetreJeu jeu;
	private static boolean isOptionModifie = true;

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
		this.isOptionModifie = isOptionModifie;
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
				jeu = new FenetreJeu(false); // isNouvelle = false
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
				jeu = new FenetreJeu(true);  // isNouvelle = true
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
