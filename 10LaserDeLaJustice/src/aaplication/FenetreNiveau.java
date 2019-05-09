package aaplication;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import son.Bruit;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;

public class FenetreNiveau extends JFrame {

	private JPanel contentPane;
	private JButton btnNiveau3;
	private Dimension ecranDimension;
	private Bruit son = new Bruit();

	/**
	 * Launch the application.
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
					FenetreNiveau frame = new FenetreNiveau();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FenetreNiveau() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 522, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		ecranDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(ecranDimension.width/2-getSize().width/2, ecranDimension.height/2-getSize().height/2);
		
		
		JButton btnNiveau = new JButton("Niveau 1");
		btnNiveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				FenetreJeu jeu = new FenetreJeu(false, "niveau1.niv");
				jeu.setVisible(true);
				setVisible(false);
			}
		});
		btnNiveau.setBounds(10, 111, 89, 33);
		contentPane.add(btnNiveau);
		
		JButton btnNiveau2 = new JButton("Niveau 2");
		btnNiveau2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				FenetreJeu jeu = new FenetreJeu(false, "niveau2.niv");
				jeu.setVisible(true);
				setVisible(false);
			}
		});
		btnNiveau2.setBounds(109, 111, 89, 33);
		contentPane.add(btnNiveau2);
		
		btnNiveau3 = new JButton("Niveau 3");
		btnNiveau3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				son.joue("beep");
				FenetreJeu jeu = new FenetreJeu(false, "niveau3.niv");
				jeu.setVisible(true);
				setVisible(false);
			}
		});
		btnNiveau3.setBounds(208, 111, 89, 33);
		contentPane.add(btnNiveau3);
		
		JButton btnNiveau5 = new JButton("Niveau 5\r\n");
		btnNiveau5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				FenetreJeu jeu = new FenetreJeu(false, "niveau5.niv");
				jeu.setVisible(true);
				setVisible(false);
			}
		});
		btnNiveau5.setBounds(406, 111, 89, 33);
		contentPane.add(btnNiveau5);
		
		JButton btnNiveau4 = new JButton("Niveau 4\r\n");
		btnNiveau4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
			FenetreJeu jeu = new FenetreJeu(false, "niveau4.niv");
			jeu.setVisible(true);
			setVisible(false);
			}
		});
		btnNiveau4.setBounds(307, 111, 89, 33);
		contentPane.add(btnNiveau4);
		
		JButton btnMaster = new JButton("Niveau master");
		btnMaster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				FenetreJeu jeu = new FenetreJeu(false, "Master.niv");
				jeu.setVisible(true);
				setVisible(false);
			}
		});
		btnMaster.setBounds(51, 191, 147, 33);
		contentPane.add(btnMaster);
		
		JButton btnNiveauPersonnalis = new JButton("Niveau personnalis\u00E9");
		btnNiveauPersonnalis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nomFichier = "";
				String userDir = System.getProperty("user.home") + "/Desktop" + "/10LaserDeLaJustice" + "/Niveau";
				JFileChooser fc = new JFileChooser(userDir);
	            int r = fc.showSaveDialog(null); 
	            if (r == JFileChooser.APPROVE_OPTION) { 
	            	File selectedFile = fc.getSelectedFile();
	            	nomFichier = selectedFile.getName();
	            	FenetreJeu jeu = new FenetreJeu(false, nomFichier);
	            	jeu.setVisible(true);
	            }
				setVisible(false);
			}
		});
		btnNiveauPersonnalis.setBounds(307, 191, 162, 33);
		contentPane.add(btnNiveauPersonnalis);
		
		JButton btnRetour = new JButton("Retour");
		btnRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				App10LaserDeLaJustice demar = new App10LaserDeLaJustice();
				demar.setVisible(true);
				setVisible(false);
			}
		});
		btnRetour.setBounds(10, 11, 89, 23);
		contentPane.add(btnRetour);
		
		JLabel lblChoisissezVotreNiveau = new JLabel("Choisissez votre niveau");
		lblChoisissezVotreNiveau.setFont(new Font("Thames", Font.BOLD | Font.ITALIC, 30));
		lblChoisissezVotreNiveau.setBounds(109, 45, 348, 41);
		contentPane.add(lblChoisissezVotreNiveau);
	}
}
