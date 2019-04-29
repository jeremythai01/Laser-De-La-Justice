package aaplication;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
/**
 * Cette fenetre contient les informations concernant les auteurs qui ont fait l'application
 * 
 * @author Arezki
 *
 */
public class FenetreAuteur extends JFrame {

	private JPanel contentPane;

	/**
	 * Lance l'application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FenetreAuteur frame = new FenetreAuteur();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Créer la fenetre
	 */
	public FenetreAuteur() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 982, 644);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnRetour = new JButton("Retour");
		btnRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnRetour.setBounds(10, 6, 89, 23);
		contentPane.add(btnRetour);

		JPanel panelAuteur = new JPanel();
		panelAuteur.setBounds(10, 40, 950, 554);
		contentPane.add(panelAuteur);
		panelAuteur.setLayout(null);

		JLabel lblAuteur = new JLabel("\r\n");
		lblAuteur.setIcon(new ImageIcon(FenetreAuteur.class.getResource("/auteur.JPG")));
		lblAuteur.setBounds(10, 11, 936, 532);
		panelAuteur.add(lblAuteur);

	}
}
